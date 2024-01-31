package vimedia.service.ReportApp.controller.tools;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import vimedia.service.ReportApp.TelegramBot.Bot;
import vimedia.service.ReportApp.model.report.Facility;
import vimedia.service.ReportApp.model.report.User;
import vimedia.service.ReportApp.model.report.Views;
import vimedia.service.ReportApp.model.tools.Event;
import vimedia.service.ReportApp.model.tools.Tool;
import vimedia.service.ReportApp.repo.report.FacilityRepo;
import vimedia.service.ReportApp.repo.report.UserRepo;
import vimedia.service.ReportApp.repo.tools.EventRepo;
import vimedia.service.ReportApp.repo.tools.ToolRepo;
import vimedia.service.ReportApp.service.MyUserDetails;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/event")
public class EventController {
    private final EventRepo eventRepo;
    private final UserRepo userRepo;
    private final ToolRepo toolRepo;
    private final Bot bot;
    private final FacilityRepo facilityRepo;

    @Autowired
    public EventController(EventRepo eventRepo, UserRepo userRepo, ToolRepo toolRepo, Bot bot, FacilityRepo facilityRepo) {
        this.eventRepo = eventRepo;
        this.userRepo = userRepo;
        this.toolRepo = toolRepo;
        this.bot = bot;
        this.facilityRepo = facilityRepo;
    }


    // Получаем все события
    @GetMapping
    @JsonView(Views.IdName.class)
    public List<Event> list() {
        return eventRepo.findAll().stream().sorted(new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                return o1.getName().toUpperCase().compareTo(o2.getName().toUpperCase());
            }
        }).collect(Collectors.toList()); // сортировка
    }

    // Получаем события по пользователю
    @GetMapping("/user")
    @JsonView(Views.IdName.class)
    public List<Event> listOfEventByUserId(@AuthenticationPrincipal MyUserDetails myUserDetails) {

        // Находим пользователя
        User user = userRepo.findByName(myUserDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден!"));


        return eventRepo.findByUser(user).stream().sorted(new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                return o1.getName().toUpperCase().compareTo(o2.getName().toUpperCase());
            }
        }).collect(Collectors.toList()); // сортировка
    }

    // Получаем события по пользователю
    @GetMapping("/user/moved/{id}")
    @JsonView(Views.IdName.class)
    public Event listOfEventByUserIdAndToolIdMoved(@PathVariable("id") Tool tool,
                                                   @AuthenticationPrincipal MyUserDetails myUserDetails) {

        // Находим пользователя
        User user = userRepo.findByName(myUserDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден!"));

        Event event = eventRepo.findByTool(tool).stream().sorted(new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                return -o1.getId() + o2.getId();
            }
        }).filter(e -> e.getTool().equals(tool))
                .filter(e -> (e.getName().equals("Передан")))
                .findFirst().orElse(null); //

        return event;
    }

    // Получаем события по пользователю и инструменту
    @GetMapping("/user/{id}")
    @JsonView(Views.IdName.class)
    public List<Event> listOfEventByUserIdAndToolId(@PathVariable("id") Tool tool,
                                                    @AuthenticationPrincipal MyUserDetails myUserDetails) {

        // Находим пользователя
        User user = userRepo.findByName(myUserDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден!"));

        return eventRepo.findByUser(user).stream().sorted(new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                return - o1.getId() + o2.getId();
            }
        }).filter(e -> e.getTool().equals(tool)).collect(Collectors.toList()); // сортировка
    }

    // Получаем события по пользователю и инструменту
    @GetMapping("/user/moving/{id}")
    @JsonView(Views.IdName.class)
    public List<Event> listOfEventByUserIdAndToolIdMoving(@PathVariable("id") Tool tool) {

        return tool.getEvents().stream().sorted(new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                return - o1.getId() + o2.getId();
            }
        }).filter(e -> (e.getTool().equals(tool) && e.getName().equals("Направлен на передачу")))
                .collect(Collectors.toList());
    }

    // Получаем одно событие
    @Transactional
    @GetMapping("{id}")
    @JsonView(Views.IdName.class)
    public Event getOne(@PathVariable("id") Event event) {
        return event;
    }


    // Создание события
    @PostMapping
    @JsonView(Views.IdName.class)
    public Event create(@RequestBody Event event, @AuthenticationPrincipal MyUserDetails myUserDetails) {
        event.setEventTimeAndDate(LocalDateTime.now());

        // Находим пользователя
        User user = userRepo.findByName(myUserDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден!"));

        // Вставляем его id в пользователя для события
        user.setId(user.getId());


        System.out.println("tool from event  " + event.getTool());
        System.out.println("tool ID from event  " + event.getTool().getId());

        int toolId = event.getTool().getId();

        // Находим инструмент c таким же ID, если есть, далее ставим ему отправителя
        Optional<Tool> tool = toolRepo.findById(toolId);

        System.out.println("toooool ->   " + toolRepo.findByUser(user).toString());

        // Проверяем есть ли с таким id уже направление на передачу
        Optional<Event> lastEvent = eventRepo.findLastMovingByTool(event.getTool());

        System.out.println("tool " + tool);

        if (tool.isPresent()) {
            if (tool.get().getUser() != null) {
                event.setUser(tool.get().getUser()); // если инструмент за кем то закреплен, то событие формируется им, как отправителем

                if (!tool.get().getUser().getName().equals(user.getName())) {
                    event.setToUser(tool.get().getUser().getName()); //  остается за другим, если это не инструмент чувака который совершает действие и это не перемещение
                };
            } else {
                // Закрепляем инструмент за текущим пользователем, если у он ни за кем не закреплен
                tool.get().setUser(user);
                event.setUser(user);
            }

            boolean alreadyDone = false; // Переменная обозначающая есть ли уже перемещение

            if (lastEvent.isPresent()) {
                if (lastEvent.get().getTool() != null)
                alreadyDone = lastEvent.get().getTool().getId() == tool.get().getId();
                System.out.println("Request for sending already exist! -> " + lastEvent);
            }

            System.out.println("Relocation already exist! -> " + alreadyDone);

            // Чат бот телеграм передача инструмента
            if (!alreadyDone) {
                //Создаем объект класса SendMessage - наш будущий ответ пользователю
                SendMessage outMess = new SendMessage();

                // Находим пользователя для отправки
                User toUser = userRepo.findByName(event.getToUser()).orElseThrow(
                        () -> new UsernameNotFoundException("Пользователь для отправки не найден не найден!"));

                String chatId = toUser.getTelegramId();

                String response = "Вам направили инструмент " + event.getTool().getName() + " от " + user.getName()
                        + " " + "http://reports.vimedia.ru/mainTableTools";

                // Если есть артикул
                if (event.getTool().getArticle() != null) {
                    response = "Вам направили инструмент " + event.getTool().getName() + " " + event.getTool().getArticle()
                            + " от " + user.getName() + " " + "http://reports.vimedia.ru/mainTableTools";
                }

                // Если есть объект откуда направили
                if (event.getTool().getFacility() != null) {
                    response = "Вам направили инструмент " + event.getTool().getName() + event.getTool().getArticle()
                            + " от " + user.getName() + " из "
                            + event.getTool().getFacility().getName() + " " + "http://reports.vimedia.ru/mainTableTools";
                }

                //Добавляем в наше сообщение id чата, а также наш ответ
                if (chatId != null) {
                    outMess.setChatId(chatId);
                }
                outMess.setText(response);

//                // Клавиатура вылазит только в случае наличия направления на передачу
//                if (event.getName().equals("Направлен на передачу")) {
//                    outMess.setReplyMarkup(bot.getToolsKeyboard());
//                }

                //Отправка в чат
                try {
                    if (chatId != null && !chatId.equals(""))

                        bot.execute(outMess);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            System.out.println(event);

            // Только в том сучае если нет текущего перемещения
            if (!alreadyDone) {
                return eventRepo.save(event);
            } else return null;
        } else return null;
    }

    // Редактирование события
    @PutMapping("{id}")
    @JsonView(Views.IdName.class)
    public Event update(@PathVariable("id") Event eventFromDB, // из базы данных
                        @RequestBody Event event ) { // от пользователя

        BeanUtils.copyProperties(event,eventFromDB,"id"); // заменяет поля кроме id

        return eventRepo.save(eventFromDB);
    }

    // Редактирование события  api/event/confirmtool/
    @PutMapping("/confirmtool/{id}")
    @JsonView(Views.IdName.class)
    @Transactional
    public Event confirm(@PathVariable("id") Tool tool, // из базы данных
                         @RequestBody Event event, @AuthenticationPrincipal MyUserDetails myUserDetails) { // от пользователя

        // Находим пользователя
        User user = userRepo.findByName(myUserDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден!"));

        if (user != null) {
            // Вставляем его id в пользователя для отчета
            user.setId(user.getId());

            // Назначается
            if (!event.getName().equals("Отклонен")) {
                event.setUser(user);
            }
        }

        // Находим последнее событие "Направлен на передачу"
        Event eventFromDB = eventRepo.findLastMovingByTool(tool).orElse(null);

        if (eventFromDB != null) {
            BeanUtils.copyProperties(event, eventFromDB, "id", "image",
                    "fromFacility", "toFacility",
                    "user", "tool", "toUser"); // заменяет поля кроме

            // Чат бот телеграм передача инструмента
            sendConfirmNotification(eventFromDB,  tool);

            String toFacility = eventFromDB.getToFacility();

            if (!toFacility.equals("")) {
                // Указываем объект для перемещения в соответствующий инструмент
                Facility facility = facilityRepo.findByName(eventFromDB.getToFacility()).orElse(null);

                System.out.println("facility");
                System.out.println(facility);

                if (facility != null) {
                    System.out.println("status --->  " + tool.getStatus());

                    return eventRepo.save(eventFromDB);
                } else return null;
            } else return null;
        } else
            return null;
    }

    @DeleteMapping("{id}")
    @JsonView(Views.IdName.class)
    public void delete(@PathVariable("id") Event event) {
        eventRepo.delete(event);
    }

    public String toWord(Integer typeOfWork) {
        String typeOfWorkByWord;
        switch (typeOfWork) {
            case 1 : typeOfWorkByWord = "Монтаж";
                break;
            case 2 : typeOfWorkByWord = "Проект";
                break;
            case 3 : typeOfWorkByWord = "Менеджмент";
                break;
            case 4 : typeOfWorkByWord = "Сборка";
                break;
            case 5 : typeOfWorkByWord = "ПНР";
                break;
            case 6 : typeOfWorkByWord = "Сервис";
                break;
            case 7 : typeOfWorkByWord = "Отпуск";
                break;
            case 8 : typeOfWorkByWord = "Больничный";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + typeOfWork);
        }
        return typeOfWorkByWord;
    }

    private void sendConfirmNotification(Event event, Tool tool) {
        String type = event.getName();
        String typeForMessage = "";

        if (type.equals("Передан")) {
            typeForMessage = "принят";
        } else if ((type.equals("Отклонен"))) {
            typeForMessage = "отклонен";
        }

        if (!typeForMessage.equals("")) {
            //Создаем объект класса SendMessage - наш будущий ответ пользователю
            SendMessage outMess = new SendMessage();

            // Находим пользователя для отправки
            User fromUser = userRepo.findByName(event.getUser().getName()).orElseThrow(
                    () -> new UsernameNotFoundException("Пользователь для передачи не найден!"));

            String chatId = fromUser.getTelegramId();

            String response = "Инструмент " + tool.getName() + " " + typeForMessage + " " + " вашим коллегой " + event.getToUser();

            // Если есть артикул
            if (tool.getArticle() != null) {
                response = "Инструмент " + tool.getName() +  " "  + tool.getArticle() + " " + typeForMessage +
                        " вашим коллегой " + event.getToUser();
            }

            // Если есть объект откуда направили
            if (tool.getFacility() != null) {
                response = "Инструмент " + tool.getName() +  " "  + tool.getArticle() + " " + typeForMessage +
                        " вашим коллегой " + event.getToUser() + ", из " + tool.getFacility().getName();
            }

            //Добавляем в наше сообщение id чата, а также наш ответ
            if (chatId != null) {
                outMess.setChatId(chatId);
                outMess.setText(response);
            }

            //Отправка в чат
            try {
                if (chatId != null && !chatId.equals(""))
                    bot.execute(outMess);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


}
