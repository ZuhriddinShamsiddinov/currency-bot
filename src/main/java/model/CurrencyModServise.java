package model;


import java.util.HashMap;
import java.util.Map;

public class CurrencyModServise {
    private static final Map<Long, Constant> originalCurrency = new HashMap<>();
    private static final Map<Long, Constant> targetCurrency = new HashMap<>();

    public static Constant getOriginalCurrency(long chatId) {
        return originalCurrency.getOrDefault(chatId, Constant.USD);
    }

    public static Constant getTargetCurrency(long chatId) {
        return targetCurrency.getOrDefault(chatId, Constant.USD);
    }

    public static void setOriginalCurrency(long chatId, Constant constant) {
        originalCurrency.put(chatId, constant);
    }

    public static void setTargetCurrency(long chatId, Constant constant) {
        targetCurrency.put(chatId, constant);
    }

}
