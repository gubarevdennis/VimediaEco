package vimedia.service.ReportApp.controller.job;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import vimedia.service.ReportApp.TelegramBot.Bot;
import vimedia.service.ReportApp.model.report.*;
import vimedia.service.ReportApp.repo.job.JobRepo;
import vimedia.service.ReportApp.repo.report.SubFacilityRepo;
import vimedia.service.ReportApp.repo.report.UserRepo;
import vimedia.service.ReportApp.service.MyUserDetails;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/job")
public class JobController {
    private final JobRepo jobRepo;
    private final UserRepo userRepo;
    private final SubFacilityRepo subFacilityRepo;
    private final Bot bot;

    @Autowired
    public JobController(JobRepo jobRepo, UserRepo userRepo, SubFacilityRepo subFacilityRepo, Bot bot) {
        this.userRepo = userRepo;
        this.jobRepo = jobRepo;
        this.subFacilityRepo = subFacilityRepo;
        this.bot = bot;
    }


    // Получаем все
    @GetMapping
    @JsonView(Views.IdName.class)
    public List<Job> list() {
        return jobRepo.findAll().stream().filter(j -> j.getName() != null).sorted(new Comparator<Job>() {
            @Override
            public int compare(Job o1, Job o2) {
                return o1.getName().toUpperCase().compareTo(o2.getName().toUpperCase());
            }
        }).collect(Collectors.toList()); // сортировка
    }

    // Получаем одно
    @GetMapping("{id}")
    @JsonView(Views.IdName.class)
    public Job getOne(@PathVariable("id") Job job) {
        return job;
    }

    // Получаем по объекту
    @GetMapping("/facility/{id}")
    @JsonView(Views.IdName.class)
    public List<Job> getByFacility(@PathVariable("id") Facility facility) {
        return facility.getJobs();
    }

    // Получаем по подобъекту
    @GetMapping("/subFacility/{id}")
    @JsonView(Views.IdName.class)
    public List<Job> getBySubFacility(@PathVariable("id") SubFacility subFacility) {
        return subFacility.getJobs();
    }

    // Получаем по пользователю
    @GetMapping("/user/{id}")
    @JsonView(Views.IdName.class)
    public List<Job> getByUser(@PathVariable("id") User user) {
        return user.getJobs();
    }


    // Создание
    @PostMapping
    @JsonView(Views.IdName.class)
    public Job create(@RequestBody Job job, @AuthenticationPrincipal MyUserDetails myUserDetails) {

        if (job.getAutoBonus() == null) {
            job.setAutoBonus(0);
        }

        // Если не указан объект, но указан подобъект мы сами ищем соответствующий объект и добавляем в работу
 //       if (job.getName() != null) // фильтруем первое добавление
            if ((job.getFacility() == null) && (job.getSubFacility() != null))
            {
                Optional<SubFacility> subFacility = subFacilityRepo.findById(job.getSubFacility().getId());
                subFacility.ifPresent(facility -> job.setFacility(facility.getFacility()));
            }

        //Отправка в чат
        // Находим пользователя
        User user = userRepo.findByName(myUserDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден!"));

        // Находим пользователя для отправки
        List<User> toUsers = job.getUsers();

        if (toUsers != null) {
            Optional<User> toUserFiltered = toUsers.stream().filter(u -> u.getName() == null).findAny();

            if (toUserFiltered.isPresent()) {
                Optional<User> toUserFilteredFromDB = userRepo.findById(toUserFiltered.get().getId().intValue());

                sendTGMessageForJob(toUserFilteredFromDB, job, user);
            }
        }

        return jobRepo.save(job);
    }

    // Редактирование
    @PutMapping("{id}")
    @JsonView(Views.IdName.class)
    public Job update(@PathVariable("id") Job jobFromDB, // из базы данных
                      @RequestBody Job job, @AuthenticationPrincipal MyUserDetails myUserDetails) { // от пользователя

        BeanUtils.copyProperties(job,jobFromDB,"id"); // заменяет поля кроме id


        System.out.println("job");
        System.out.println(job);

        //Отправка в чат
        // Находим пользователя
        User user = userRepo.findByName(myUserDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден!"));

        // Находим пользователя для отправки
        List<User> toUsers = job.getUsers();
        if (toUsers != null) {
            Optional<User> toUserFiltered = toUsers.stream().filter(u -> u.getName() == null).findAny();

            if (toUserFiltered.isPresent()) {
                Optional<User> toUserFilteredFromDB = userRepo.findById(toUserFiltered.get().getId().intValue());

                sendTGMessageForJob(toUserFilteredFromDB, job, user);
            }
        }

        return jobRepo.save(jobFromDB);
    }

    private void sendTGMessageForJob(Optional<User> toUser, Job job, User user) {
        if (toUser.isPresent()) {
            System.out.println(toUser);
            //Создаем объект класса SendMessage - наш будущий ответ пользователю
            SendMessage outMess = new SendMessage();

            String chatId = toUser.get().getTelegramId();
            System.out.println("u.getTelegramId()" + toUser.get().getTelegramId());
            //Добавляем в наше сообщение id чата, а также наш ответ
            if (chatId != null) {
                outMess.setChatId(chatId);
            }
            if (job.getSubFacility() != null && job.getBudget() != null && job.getBonus() != null && job.getMarginPercentage() != null && job.getFacility() != null ) {
                outMess.setText(user.getRole() + " " + user.getName() + " назначил вас ответственным за проведение работы " + job.getName().toLowerCase() + " на объекте " + job.getFacility().getName() + ", " + String.valueOf(job.getSubFacility().getName()) +
                        ". Бонусная база за данную работу составляет " + Math.round(job.getBudget() * (job.getBonus() / 100) * (job.getMarginPercentage() / 100) * 0.8) + "р. Удачи, бро!" + "http://reports.vimedia.ru/bonus");
            } else {
                outMess.setText(user.getRole() + " " + user.getName() + " назначил вас ответственным за проведение работы " + job.getName().toLowerCase() + " на объекте " + job.getFacility().getName() +
                        ". Бонусная база за данную работу составляет " + Math.round(job.getBudget() * (job.getBonus() / 100) * (job.getMarginPercentage() / 100) * 0.8) + "р. Удачи, бро!" + "http://reports.vimedia.ru/bonus");
            }
            try {
                if (chatId != null && !chatId.equals(""))
                    bot.execute(outMess);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @DeleteMapping("{id}")
    @JsonView(Views.IdName.class)
    public void delete(@PathVariable("id") Job toolSet) {
        jobRepo.delete(toolSet);
    }
}
