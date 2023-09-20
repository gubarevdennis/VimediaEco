package vimedia.service.ReportApp.TelegramBot;

import jakarta.persistence.FetchType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import vimedia.service.ReportApp.model.Report;
import vimedia.service.ReportApp.model.User;
import vimedia.service.ReportApp.repo.UserRepo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class Bot extends TelegramLongPollingBot {
    //создаем две константы, присваиваем им значения токена и имя бота соответсвтенно
    //вместо звездочек подставляйте свои данные
    private final String BOT_TOKEN = "6446999228:AAGWn1Hf99j_2-0i4jJ9CwRbTWH5_351Ao4";
    private final String BOT_NAME = "VimediaCRMBot";
    private final Storage storage;
    private final UserRepo userRepo;
    private String chatId;
    private User user;
    private Timer telegramTimer;
    private MyTimerTask telegramTimerTask;
    private Boolean authenticated;

    @Autowired
    public Bot(Storage storage, UserRepo userRepo,
               TelegramBotsApi telegramBotsApi) throws TelegramApiException {
        this.storage = storage;
        this.userRepo = userRepo;

        this.telegramTimer = new Timer();
        this.telegramTimerTask = new MyTimerTask();

        telegramBotsApi.registerBot(this);
        this.startTimer();
    }


    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try{
            if(update.hasMessage() && update.getMessage().hasText())
            {
                //Извлекаем из объекта сообщение пользователя
                Message inMess = update.getMessage();
                //Достаем из inMess id чата пользователя
                String chatId = inMess.getChatId().toString();
                //Получаем текст сообщения пользователя, отправляем в написанный нами обработчик
                String response = parseMessage(inMess.getText(), chatId);

                //Получаем id пользователя
                long id_Utente = update.getMessage().getFrom().getId();
                //Получаем userName пользователя
                String userName = update.getMessage().getFrom().getUserName();
                //Получаем firstName пользователя
                String firstName = update.getMessage().getFrom().getFirstName();
                //Получаем lastName пользователя
                String lastName = update.getMessage().getFrom().getLastName();

                //Создаем объект класса SendMessage - наш будущий ответ пользователю
                SendMessage outMess = new SendMessage();

                //Добавляем в наше сообщение id чата, а также наш ответ
                outMess.setChatId(chatId);
                outMess.setText(response);

                //Отправка в чат
                execute(outMess);
            }
        } catch (TelegramApiException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public String parseMessage(String textMsg, String chatId) throws UnsupportedEncodingException {
        String response;

        //Сравниваем текст пользователя с нашими командами, на основе этого формируем ответ
        if(textMsg.equals("/start")) {
            response = "Привет коллега! Меня зовут ViBro, я твой бро. " +
                    "Я создан для того, чтобы помогать тебе. Давай познакомимся..." +
                    "Набирай свою фамилию и имя, точно как в приложении";
        }
        else if(userRepo.findByName(textMsg).isPresent() && userRepo.findByTelegramId(chatId).isEmpty()) {
//            String textEncoded = new String(textMsg.getBytes(StandardCharsets.UTF_8), "windows-1251");
//            System.out.println(textEncoded);
            User user = userRepo.findByName(textMsg).get();

            if(user.getTelegramId() == null || user.getTelegramId().equals("")) {
                user.setTelegramId(chatId);
            }

            userRepo.save(user);

            response = "Я тебя узнал бро, " + textMsg +
                    "! Рад знакомству! Буду присылать тебе напоминания, если ты забудешь отправить отчет. ";
        }
        else if(textMsg.startsWith("отчет")) {
            response="Отчет отправлен! Спасибо за работу!";
        }
        else if (userRepo.findByTelegramId(chatId).isEmpty()) {
            response = "Извини, не признал, бро. Попробуй еще. Помни - точно как в приложении!";
        } else response = "Рад был помочь, бро!";
        return response;
    }

    @Transactional()
    public void sendNotification() {
        List<User> users = userRepo.findAll();

        users.forEach(u -> {
            //Создаем объект класса SendMessage - наш будущий ответ пользователю
            SendMessage outMess = new SendMessage();

            List<Report> reports = u.getReports();
            if (u.getTelegramId() != null) {

                Optional<Report> report = reports
                        .stream()
                        .filter(r -> r.getReportDay().equals(LocalDate.now().minusDays(1)))
                        .findAny();

                Optional<Report> reportOlder = reports
                        .stream()
                        .filter(r -> r.getReportDay().equals(LocalDate.now().minusDays(2)))
                        .findAny();

                // Вчерашний день
                LocalDate localDate = LocalDate.now().minusDays(1);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM uuuu");
                String formattedString = localDate.format(formatter);

                //Добавляем в наше сообщение id чата а также наш ответ
                String response = "Коллега, доброе утро! Напоминаю тебе, что ты не заполнил отчет за вчерашний день! Это было "+
                        formattedString + ". Пожалуйста, сделай это сегодня. Спасибо! http://reports.vimedia.ru/";

                if (reportOlder.isEmpty()) {
                    response = response + "Коллега, доброе утро! Напоминаю, что ты не заполнил отчет за позавчера! Это было "+
                            formattedString + ". Пожалуйста, сделай это сегодня. Спасибо! http://reports.vimedia.ru/";
                }

                if (reportOlder.isEmpty() && report.isEmpty()) {
                    response = "Безалаберность, бро! Напоминаю, что ты не заполнил отчет два раза подряд! Последний "+
                            formattedString + ". Пожалуйста, сделай это сегодня. Спасибо! http://reports.vimedia.ru/";
                }

                String telegramId = u.getTelegramId();

                outMess.setChatId(telegramId);

                outMess.setText(response);
                try {
                    //Отправка в чат
                    if (report.isEmpty() && !telegramId.equals(""))
                        execute(outMess);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Таймер на отправку уведомлений каждый день
    public void startTimer(){
        Date nowDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(nowDate);
        c.add(Calendar.DATE, 1);
        nowDate = c.getTime();

        Date date = new Date(nowDate.getYear(),nowDate.getMonth(),nowDate.getDate(), 10, 0);

        // 1000 - время в мс, через которое будет запущена задача
        telegramTimer.schedule(telegramTimerTask, date, 86400000); //86400000
    }

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            // Если не заполнил вчерашний отчет, то высылаем напоминание
            sendNotification();
        }
    }

}
