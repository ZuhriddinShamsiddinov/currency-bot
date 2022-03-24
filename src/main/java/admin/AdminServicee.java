package admin;

import bot.TestBot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.SneakyThrows;
import model.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdminServicee {

    @SneakyThrows
    public static void ExcelWriter(Update update, String chatId) {
        File file1 = new File("src/main/resources/users.json");

        BufferedReader bufferedReader = new BufferedReader(new FileReader(file1));
        Gson gson = new Gson();

        List<List<User>> list = gson.fromJson(bufferedReader, new TypeToken<List<List<User>>>() {
        }.getType());
        List<User> userList = list.get(0);

        File file = new File("src/main/resources/UserList.xlsx");
        FileOutputStream outputStream = new FileOutputStream(file);


        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Users");

        XSSFRow row = sheet.createRow(0);

        row.createCell(0).setCellValue("CHAT ID");
        row.createCell(1).setCellValue("FIRSTNAME");
        row.createCell(2).setCellValue("PHONE");

        int number = 0;
        for (User user : userList) {
            XSSFRow row1 = sheet.createRow(number + 1);
            row1.createCell(0).setCellValue(user.getChatId());
            row1.createCell(1).setCellValue(user.getUsername());
            row1.createCell(2).setCellValue(user.getPhone());
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            number++;
        }
        workbook.write(outputStream);
        outputStream.close();
    }

    @SneakyThrows
    public SendDocument SendDocument(Update update, String chatId) {
        File file = new File("src/main/resources/UserList.xlsx");
        InputFile inputFile = new InputFile(file);
        return SendDocument.builder()
                .chatId(chatId)
                .document(inputFile)
                .build();
    }
    public static SendDocument SendDocumentStory(Update update, String chatId) {
        File file = new File("src/main/resources/story.xlsx");
        InputFile inputFile = new InputFile(file);
        return SendDocument.builder()
                .chatId(chatId)
                .document(inputFile)
                .build();
    }


    @SneakyThrows
    public SendDocument SendDocumentPDFList(Update update, String chatId) {
        File file = new File("D:\\PDP  LESSONS\\Java lessons\\MONTH_4\\course-work\\ onlineShop-bot\\src\\main\\resources\\currency.pdf");
        InputFile inputFile = new InputFile(file);
        return SendDocument.builder()
                .chatId(chatId)
                .document(inputFile)
                .build();
    }

    public SendMessage help(Update update, String chatId) {
        return SendMessage.builder()
                .text("/start orqali botni ishga tushurasiz\n/admin orqali siz admin bo'lishingiz mumkin")
                .chatId(chatId)
                .build();
    }

    public SendMessage buttons(Update update, String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Assalomu Alaykum Admin \uD83D\uDC4B" + update.getMessage().getFrom().getFirstName());
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);

        List<KeyboardRow> rows = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();

        KeyboardButton button1 = new KeyboardButton("history\uD83D\uDCC3");
        KeyboardButton button2 = new KeyboardButton("users\uD83D\uDCC3");
        KeyboardButton button3 = new KeyboardButton("currencies\uD83D\uDCC3");
        KeyboardButton button4 = new KeyboardButton("News");
        KeyboardButton button5 = new KeyboardButton("Reclama");

        row2.add(button2);
        row2.add(button3);

        row1.add(button1);
        row1.add(button4);
        row1.add(button5);

        rows.add(row1);
        rows.add(row2);
        replyKeyboardMarkup.setKeyboard(rows);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        return sendMessage;
    }

    public static void registerStory(Update update, String orginal, String target, String date, double rateTo, double rateFrom) {
        if (DatabaseStory.story.isEmpty()) {
            Story story = new Story();
            story.setUserName(update.getMessage().getFrom().getFirstName());
            story.setDate(date);
            story.setOriginal(orginal);
            story.setTarget(target);
            story.setValuefrom(rateFrom);
            story.setValueto(rateTo * rateFrom);

            DatabaseStory.story.add(story);
            DatabaseStory.map.putIfAbsent(Constant2.USER, Collections.singletonList(DatabaseStory.story));
            DatabaseStory.writeJson(Constant2.USER);
        } else {
            Story story = new Story();
            story.setUserName(update.getMessage().getFrom().getFirstName());
            story.setDate(date);
            story.setOriginal(orginal);
            story.setTarget(target);
            story.setValuefrom(rateFrom);
            story.setValueto(rateTo * rateFrom);

            DatabaseStory.story.add(story);
            DatabaseStory.map.putIfAbsent(Constant2.USER, Collections.singletonList(DatabaseStory.story));
            DatabaseStory.readJson();
            DatabaseStory.writeJson(Constant2.USER);
        }
    }

    public static void ExcelStoryWriter(Update update) throws IOException {
        File file1 = new File("src/main/resources/story.json");

        BufferedReader bufferedReader = new BufferedReader(new FileReader(file1));
        Gson gson = new Gson();

        List<List<Story>> list = gson.fromJson(bufferedReader, new TypeToken<List<List<Story>>>() {
        }.getType());
        List<Story> userList = list.get(0);

        File file = new File("src/main/resources/story.xlsx");
        FileOutputStream outputStream = new FileOutputStream(file);


        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Story");

        XSSFRow row = sheet.createRow(0);

        row.createCell(0).setCellValue("NAME");
        row.createCell(1).setCellValue("DATE");
        row.createCell(2).setCellValue("ORIGINAL");
        row.createCell(3).setCellValue("TARGET");
        row.createCell(4).setCellValue("FROM");
        row.createCell(5).setCellValue("TO");

        int number = 0;
        for (Story user : userList) {
            XSSFRow row1 = sheet.createRow(number + 1);
            row1.createCell(0).setCellValue(user.getUserName());
            row1.createCell(1).setCellValue(user.getDate());
            row1.createCell(2).setCellValue(user.getOriginal());
            row1.createCell(3).setCellValue(user.getTarget());
            row1.createCell(4).setCellValue(user.getValuefrom());
            row1.createCell(5).setCellValue(user.getValueto());


            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            sheet.autoSizeColumn(5);

            number++;
        }
        workbook.write(outputStream);
        outputStream.close();
    }

    public SendPhoto sendReclama(Update update, String chatId) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setPhoto(new InputFile(new File("D:\\PDP  LESSONS\\Java lessons\\MONTH_4\\course-work\\ onlineShop-bot\\src\\main\\resources\\photo.jpg")));
        sendPhoto.setCaption("""
                Xabaringiz bor, bugun Astrum Academy'da 3D sohalariga oid ochiq dars tashkillashtiryapmiz. Ochiq darsga kela olmaganlar quyidagi Instagram sahifasi orqali yangiliklardan xabardor boʻlib turishlari mumkin.

                LINK \uD83D\uDC49 instagram.com/aires_agency""");
        return sendPhoto;
    }


    @SneakyThrows
    public void news(Update update, String chatId) {
        StringBuilder builder = new StringBuilder();
        Connection connection = Jsoup.connect("https://kun.uz/uz/news/category/tehnologia");
        Document document = connection.get();
        Elements dataList = document.getElementsByClass("small-news__content");

        Elements titleList = document.getElementsByClass("small-news__title");
        for (Element element : dataList) {
            for (Element element1 : element.getElementsByTag("a")) {
                builder.append("\uD83D\uDCA5"+element1.text() + "\n\n");
            }
        }
        for (User user : Database.users) {
            String chatID = user.getChatId();
            new TestBot().execute(SendMessage.builder()
                    .chatId(chatID)
                    .text(builder.toString())
                    .build());

        }
    }

    @SneakyThrows
    public void sendReklama() {
        for (User user : Database.users) {
            String chatId = user.getChatId();
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(chatId);
            sendPhoto.setCaption("Xabaringiz bor, bugun Astrum Academy'da 3D sohalariga oid ochiq dars tashkillashtiryapmiz. Ochiq darsga kela olmaganlar quyidagi Instagram sahifasi orqali yangiliklardan xabardor boʻlib turishlari mumkin.\n" +
                    "LINK \uD83D\uDC49 instagram.com/aires_agency");
            sendPhoto.setPhoto(new InputFile(new File("D:\\PDP  LESSONS\\Java lessons\\MONTH_4\\course-work\\ onlineShop-bot\\src\\main\\resources\\photo.jpg")));
            new TestBot().execute(sendPhoto);
        }

    }

    public SendMessage help(String chatId, Update update) {
        return SendMessage.builder()
                .text("/start orqali botni ishga tushurasiz\n\n/converter orqali valyutani hisoblashingiz mumkin")
                .chatId(chatId)
                .build();

    }

    public SendMessage start(Update update, String chatId) {

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setResizeKeyboard(true);

        List<KeyboardRow> rowList = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardButton button = new KeyboardButton();
        button.setRequestContact(true);
        button.setText("Raqamni yuborish");
        row.add(button);
        rowList.add(row);
        registerUser(update, chatId);
        replyKeyboardMarkup.setKeyboard(rowList);
        String text;
        if (update.getMessage().getFrom().getFirstName() != null) {
            text = update.getMessage().getFrom().getFirstName();
        } else {
            text = "";
        }

        return SendMessage.builder()
                .text("Xush kelibsiz! " + text + " Registerdan o'ting!")
                .replyMarkup(replyKeyboardMarkup)
                .chatId(String.valueOf(update.getMessage().getChatId()))
                .build();
    }

    public void registerUser(Update update, String chatId) {
        boolean hasUser = false;
        if (!Database.users.isEmpty()) {
            for (User user : Database.users) {
                if (user.getChatId().equals(chatId)) {
                    hasUser = true;
                }
            }
            if (!hasUser) {
                User user = new User();
                user.setChatId(chatId);
                String username;
                if (!update.getMessage().getFrom().getFirstName().isEmpty()) {
                    username = update.getMessage().getFrom().getFirstName();
                } else {
                    username = Constant2.NO_INFO;
                }
                user.setUsername(username);
                Database.users.add(user);
                Database.map.putIfAbsent(Constant2.USER, Collections.singletonList(Database.users));
                Database.writeJson(Constant2.USER);
            }
        } else {
            User user = new User();
            user.setChatId(chatId);
            user.setUsername(!update.getMessage().getFrom().getFirstName().isEmpty()
                    ? update.getMessage().getFrom().getFirstName() : Constant2.NO_INFO);
            Database.users.add(user);
            Database.map.putIfAbsent(Constant2.USER, Collections.singletonList(Database.users));
            Database.writeJson(Constant2.USER);
        }
    }

}
