package model;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InlineUtil {

    public static InlineKeyboardButton button(String text,String callbackdata ){
        InlineKeyboardButton button=new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackdata);
        return button;
    }
    public static List<InlineKeyboardButton> rowlist(InlineKeyboardButton... inlineKeyboardButtons){
        List<InlineKeyboardButton> rowlist=new ArrayList<>();
        rowlist.addAll(Arrays.asList(inlineKeyboardButtons));
        return rowlist;
    }
    public static List<List<InlineKeyboardButton>> listList(List<InlineKeyboardButton>... rowlist){
        List<List<InlineKeyboardButton>> listList=new ArrayList<>();
        listList.addAll(Arrays.asList(rowlist));
        return listList;
    }
    public static InlineKeyboardMarkup inlineKeyboardMarkup(List<List<InlineKeyboardButton>> listList){
        InlineKeyboardMarkup inlineKeyboardMarkup=new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(listList);
        return inlineKeyboardMarkup;
    }

}
