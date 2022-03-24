package service;

import admin.AdminServicee;
import com.itextpdf.text.DocumentException;
import lombok.SneakyThrows;
import model.*;
import model.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class BotService implements Service {
    @Override
    public Optional<Double> parseDuble(String messageText) {
        try {
            return Optional.of(Double.parseDouble(messageText));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public String getCurrencyButton(Constant saved, Constant current) {
        return saved == current ? current + "✅" : current.name();
    }

    @SneakyThrows
    @Override
    public SendMessage natija(Message message, Update update) {
        String messageText = message.getText();
        Optional<Double> value = parseDuble(messageText);
        Constant originalCurrency = CurrencyModServise.getOriginalCurrency(message.getChatId());
        Constant targetCurrency = CurrencyModServise.getTargetCurrency(message.getChatId());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = format.format(new Date());
        double ratio = CurrencyConversionService.getConversionRatio(originalCurrency, targetCurrency);
        AdminServicee.registerStory(update, originalCurrency.name(), targetCurrency.name(), date, ratio, value.get());
        return SendMessage.builder()
                .chatId(message.getChatId().toString())
                .text(
                        String.format(
                                "%4.2f %s   \uD83D\uDD00️ %4.2f %s",
                                value.get(), originalCurrency, (value.get() * ratio), targetCurrency))
                .build();

    }

    @Override
    public SendMessage handleMessage(Message message) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        Constant originalCurrency =
                CurrencyModServise.getOriginalCurrency(message.getChatId());
        Constant targetCurrency = CurrencyModServise.getTargetCurrency(message.getChatId());
        for (Constant currency : Constant.values()) {
            buttons.add(
                    Arrays.asList(
                            InlineKeyboardButton.builder()
                                    .text(getCurrencyButton(originalCurrency, currency))
                                    .callbackData("ORIGINAL:" + currency)
                                    .build(),
                            InlineKeyboardButton.builder()
                                    .text(getCurrencyButton(targetCurrency, currency))
                                    .callbackData("TARGET:" + currency)
                                    .build()));
        }


        return SendMessage.builder()
                .text("Valyutalarni tanlab summani kiriting")
                .chatId(message.getChatId().toString())
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                .build();
    }


    @Override
    public EditMessageReplyMarkup handleCallback(CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        String[] param = callbackQuery.getData().split(":");
        String action = param[0];
        Constant newCurrency = Constant.valueOf(param[1]);
        switch (action) {
            case "ORIGINAL" -> CurrencyModServise.setOriginalCurrency(message.getChatId(), newCurrency);
            case "TARGET" -> CurrencyModServise.setTargetCurrency(message.getChatId(), newCurrency);
        }

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        Constant originalCurrency = CurrencyModServise.getOriginalCurrency(message.getChatId());
        Constant targetCurrency = CurrencyModServise.getTargetCurrency(message.getChatId());
        for (Constant currency : Constant.values()) {
            buttons.add(
                    Arrays.asList(
                            InlineKeyboardButton.builder()
                                    .text(getCurrencyButton(originalCurrency, currency))
                                    .callbackData("ORIGINAL:" + currency)
                                    .build(),
                            InlineKeyboardButton.builder()
                                    .text(getCurrencyButton(targetCurrency, currency))
                                    .callbackData("TARGET:" + currency)
                                    .build()));
        }
        return EditMessageReplyMarkup.builder()
                .chatId(message.getChatId().toString())
                .messageId(message.getMessageId())
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                .build();
    }

    @Override
    public void registerUser(Update update, String chatId) {
        boolean hasUser = false;
        if (!Database.users.isEmpty()) {
            for (model.User user : Database.users) {
                if (user.getChatId().equals(chatId)) {
                    hasUser = true;
                }
            }
            if (!hasUser) {
                model.User user = new model.User();
                user.setChatId(chatId);
                user.setFirstName(!update.getMessage().getFrom().getFirstName().isEmpty()
                        ? update.getMessage().getFrom().getFirstName() :
                        Constant2.NO_INFO);
                user.setUsername(!update.getMessage().getFrom().getUserName().isEmpty()
                        ? update.getMessage().getFrom().getUserName() : Constant2.NO_INFO);
                Database.users.add(user);
                Database.map.putIfAbsent(Constant2.USER, Collections.singletonList(Database.users));
                Database.writeJson(Constant2.USER);
            }
        } else {
            model.User user = new model.User();
            user.setChatId(chatId);
            user.setChatId(chatId);
            user.setFirstName(!update.getMessage().getFrom().getFirstName().isEmpty()
                    ? update.getMessage().getFrom().getFirstName() :
                    Constant2.NO_INFO);
            user.setUsername(!update.getMessage().getFrom().getUserName().isEmpty()
                    ? update.getMessage().getFrom().getUserName() : Constant2.NO_INFO);
            Database.users.add(user);
            Database.map.putIfAbsent(Constant2.USER, Collections.singletonList(Database.users));
            Database.writeJson(Constant2.USER);
        }


    }


    @Override
    public model.User getUser(String chatId) {
        for (model.User user : Database.users) {
            if (user.getChatId().equals(chatId)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public SendMessage shereContact(Update update, String chatId) throws IOException, DocumentException {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        List<KeyboardRow> row = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardButton button = new KeyboardButton("CONTACT");
        button.setRequestContact(true);
        row1.add(button);
        row.add(row1);
        replyKeyboardMarkup.setKeyboard(row);
        registerUser(update, chatId);
        return SendMessage.builder()
                .text("Assalomu alaykum")
                .replyMarkup(replyKeyboardMarkup)
                .chatId(chatId)
                .build();
    }

    @Override
    public SendMessage Contact(Update update, String chatId) throws IOException {
        Contact contact = update.getMessage().getContact();
        User user = getUser(chatId);
        user.setPhone(contact.getPhoneNumber());
        Database.writeJson(Constant2.USER);
        return SendMessage.builder()
                .text("Hizmatlar haqida⬇️")
                .chatId(chatId)
                .build();
    }


    @Override
    public SendMessage ConverterButton(Update update, String chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> listList = new ArrayList<>();
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton("Converter");
        button.setCallbackData("converter");
        buttons.add(button);
        listList.add(buttons);
        inlineKeyboardMarkup.setKeyboard(listList);
        return SendMessage.builder()
                .text("Hizmatlar haqida⬇️")
                .chatId(chatId)
                .replyMarkup(inlineKeyboardMarkup)
                .build();
    }

    public SendMessage daleteKeyboard(Update update, String chatId) {
        ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove();
        replyKeyboardRemove.setRemoveKeyboard(true);
        return SendMessage.builder()
                .chatId(chatId)
                .text("/help")
                .replyMarkup(replyKeyboardRemove)
                .build();
    }

    public SendMessage message(Update update, String chatId) {
        return SendMessage.builder()
                .text(update.getMessage().getText())
                .chatId(chatId)
                .build();
    }
}
