package service;


import com.itextpdf.text.DocumentException;
import model.Constant;
import model.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.util.Optional;

public interface Service {

    Optional<Double> parseDuble(String messageText);
    String getCurrencyButton(Constant saved, Constant current);
    SendMessage natija(Message messagetext,Update update);
    SendMessage handleMessage(Message message);
    EditMessageReplyMarkup handleCallback(CallbackQuery callbackQuery);
    void registerUser(Update update, String chatId);
    User getUser(String chatId);
    SendMessage shereContact(Update update,String chatId) throws IOException, DocumentException;
    SendMessage Contact(Update update, String chatId) throws IOException;
    SendMessage ConverterButton(Update update,String chatId);
}
