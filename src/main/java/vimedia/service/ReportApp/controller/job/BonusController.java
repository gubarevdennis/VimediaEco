package vimedia.service.ReportApp.controller.job;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import vimedia.service.ReportApp.model.bonus.Bonus;
import vimedia.service.ReportApp.model.report.*;
import vimedia.service.ReportApp.repo.bonus.BonusRepo;
import vimedia.service.ReportApp.repo.job.JobRepo;
import vimedia.service.ReportApp.service.MyUserDetails;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bonus")
public class BonusController {
    private final BonusRepo bonusRepo;
    private final JobRepo jobRepo;

    @Autowired
    public BonusController(BonusRepo bonusRepo, JobRepo jobRepo) {
        this.bonusRepo = bonusRepo;
        this.jobRepo = jobRepo;
    }

    // Получаем все
    @GetMapping
    @JsonView(Views.IdName.class)
    public List<Bonus> list() {
        return bonusRepo.findAll().stream().sorted(new Comparator<Bonus>() {
            @Override
            public int compare(Bonus o1, Bonus o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        }).collect(Collectors.toList()); // сортировка; // сортировка
    }

    // Получаем одно
    @GetMapping("{id}")
    @JsonView(Views.IdName.class)
    public Bonus getOne(@PathVariable("id") Bonus bonus) {
        return bonus;
    }

    // Получаем по работе
    @GetMapping("/job/{id}")
    @JsonView(Views.IdName.class)
    public List<Bonus> getByJob(@PathVariable("id") Job job) {

         Job jobForSending = jobRepo.findById(job.getId()).orElseThrow(() -> new UsernameNotFoundException("Работа не найдена!"));

        return jobForSending.getBonuses();
    }

    // Получаем по пользователю
    @GetMapping("/user/{id}")
    @JsonView(Views.IdName.class)
    public List<Bonus> getByUser(@PathVariable("id") User user) {
        return user.getBonuses();
    }


    // Создание
    @PostMapping
    @JsonView(Views.IdName.class)
    public Bonus create(@RequestBody Bonus bonus) {


//        // Если не указан объект, но указан подобъект мы сами ищем соответствующий объект и добавляем в работу
//        //       if (job.getName() != null) // фильтруем первое добавление
//        if ((job.getFacility() == null) && (job.getSubFacility() != null))
//        {
//            Optional<SubFacility> subFacility = subFacilityRepo.findById(job.getSubFacility().getId());
//            subFacility.ifPresent(facility -> job.setFacility(facility.getFacility()));
//        }
//
//        //Отправка в чат
//        // Находим пользователя
//        User user = userRepo.findByName(myUserDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден!"));
//
//        // Находим пользователя для отправки
//        List<User> toUsers = job.getUsers();
//
//        if (toUsers != null) {
//            Optional<User> toUserFiltered = toUsers.stream().filter(u -> u.getName() == null).findAny();
//
//            if (toUserFiltered.isPresent()) {
//                Optional<User> toUserFilteredFromDB = userRepo.findById(toUserFiltered.get().getId().intValue());
//
//                sendTGMessageForJob(toUserFilteredFromDB, job, user);
//            }
//        }

        return bonusRepo.save(bonus);
    }

    // Редактирование
    @PutMapping("{id}")
    @JsonView(Views.IdName.class)
    public Bonus update(@PathVariable("id") Bonus bonusFromDB, // из базы данных
                      @RequestBody Bonus bonus, @AuthenticationPrincipal MyUserDetails myUserDetails) { // от пользователя

        BeanUtils.copyProperties(bonus,bonusFromDB,"id", "bonuses"); // заменяет поля кроме id

//        //Отправка в чат
//        // Находим пользователя
//        User user = userRepo.findByName(myUserDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден!"));
//
//        // Находим пользователя для отправки
//        List<User> toUsers = job.getUsers();
//        if (toUsers != null) {
//            Optional<User> toUserFiltered = toUsers.stream().filter(u -> u.getName() == null).findAny();
//
//            if (toUserFiltered.isPresent()) {
//                Optional<User> toUserFilteredFromDB = userRepo.findById(toUserFiltered.get().getId().intValue());
//
//                sendTGMessageForJob(toUserFilteredFromDB, job, user);
//            }
//        }

        return bonusRepo.save(bonusFromDB);
    }

    private void sendTGMessageForJob(Optional<User> toUser, Job job, User user) {
//        if (toUser.isPresent()) {
//            System.out.println(toUser);
//            //Создаем объект класса SendMessage - наш будущий ответ пользователю
//            SendMessage outMess = new SendMessage();
//
//            String chatId = toUser.get().getTelegramId();
//            System.out.println("u.getTelegramId()" + toUser.get().getTelegramId());
//            //Добавляем в наше сообщение id чата, а также наш ответ
//            if (chatId != null) {
//                outMess.setChatId(chatId);
//            }
//            if (job.getSubFacility() != null) {
//                outMess.setText(user.getName() + " назначил вас ответственным за проведение работы " + job.getName().toLowerCase() + " на объекте " + job.getFacility().getName() + ", " + String.valueOf(job.getSubFacility().getName()) +
//                        ". Бонусная база за данную работу составляет " + Math.round(job.getBudget() * (job.getBonus() / 100) * (job.getMarginPercentage() / 100) * 0.8) + "р. Удачи, бро!" + "http://reports.vimedia.ru/bonus");
//            } else {
//                outMess.setText(user.getName() + " назначил вас ответственным за проведение работы " + job.getName().toLowerCase() + " на объекте " + job.getFacility().getName() +
//                        ". Бонусная база за данную работу составляет " + Math.round(job.getBudget() * (job.getBonus() / 100) * (job.getMarginPercentage() / 100) * 0.8) + "р. Удачи, бро!" + "http://reports.vimedia.ru/bonus");
//            }
//            try {
//                if (chatId != null && !chatId.equals(""))
//                    bot.execute(outMess);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }

    @DeleteMapping("{id}")
    @JsonView(Views.IdName.class)
    public void delete(@PathVariable("id") Bonus bonus) {
        bonusRepo.delete(bonus);
    }
}
