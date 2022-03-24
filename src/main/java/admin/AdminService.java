package admin;

import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

public interface AdminService {

    SendMessage welcome(Update update, String chatId);

    void newsReklama() throws TelegramApiException;

    SendDocument ConvertatsiyaList(CallbackQuery callbackQuery);

    SendDocument SendDocument(Update update, String chatId);

    void newsSend() throws IOException, TelegramApiException;

    SendPhoto newsReklama(Update update,String chatId);


}
