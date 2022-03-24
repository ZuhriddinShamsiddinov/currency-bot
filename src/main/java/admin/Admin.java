package admin;


import bot.TestBot;
import lombok.SneakyThrows;
import model.Database;
import model.InlineUtil;
import model.User;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;


public class Admin implements AdminService {
    @Override
    public SendMessage welcome(Update update, String chatId) {

        return SendMessage.builder()
                .text("Hush kelibsiz!\n quyidagi amaliyotlarni birini tanlang?")
                .chatId(chatId)
                .replyMarkup(InlineUtil.inlineKeyboardMarkup(
                        InlineUtil.listList(
                                InlineUtil.rowlist(
                                        InlineUtil.button("Userlar royhatti ", "#userslist"),
                                        InlineUtil.button("Convertatsiyalar royhati", "#converlist")
                                ),
                                InlineUtil.rowlist(
                                        InlineUtil.button("Valyutalar kursi", "#valyutalist"),
                                        InlineUtil.button("Yangiliklar jonatish", "#newssend")
                                ),
                                InlineUtil.rowlist(
                                        InlineUtil.button("Reklama jonatish", "#reklama"))))).build();
    }

    @Override
    public SendDocument SendDocument(Update update, String chatId) {
        File file = new File("src/main/java/api/model2/users.xlsx");
        InputFile inputFile = new InputFile(file);
        return SendDocument.builder()
                .chatId(chatId)
                .document(inputFile)
                .build();
    }

    @Override
    public void newsSend() throws IOException, TelegramApiException {

        StringBuilder builder = new StringBuilder();
        Connection connection = Jsoup.connect("https://kun.uz/uz/news/category/sport");
        Document document = connection.get();
        Elements dataList = document.getElementsByClass("small-news__content");

        Elements titleList = document.getElementsByClass("small-news__title");
        for (Element element : dataList) {
            for (Element element1 : element.getElementsByTag("a")) {
                builder.append(element1.text() + "\n");
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

    @Override
    public SendPhoto newsReklama(Update update, String chatId) {
        return null;
    }

    @SneakyThrows
    @Override
    public void newsReklama() {
        for (User user : Database.users) {
            String chatId = user.getChatId();
            new TestBot().execute(SendPhoto.builder()
                    .chatId(chatId)
                    .photo(new InputFile(new File("D:\\PDP  LESSONS\\Java lessons\\MONTH_4\\course-work\\ onlineShop-bot\\src\\main\\resources\\photo.jpg")))
                    .caption("""
                            Xabaringiz bor, bugun Astrum Academy'da 3D sohalariga oid ochiq dars tashkillashtiryapmiz. Ochiq darsga kela olmaganlar quyidagi Instagram sahifasi orqali yangiliklardan xabardor boʻlib turishlari mumkin.

                            LINK  \uD83D\uDC49  instagram.com/aires_agency""")
                    .build());
        }
    }


    @Override
    public SendDocument ConvertatsiyaList(CallbackQuery callbackQuery) {
        File file = new File("D:\\PDP  LESSONS\\Java lessons\\MONTH_4\\course-work\\currency-bot\\src\\main\\resources\\kurs.pdf");
        InputFile inputFile = new InputFile(file);
        return SendDocument.builder()
                .chatId(callbackQuery.getMessage().getChatId().toString())
                .document(inputFile)
                .build();
    }
}
