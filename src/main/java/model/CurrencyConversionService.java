package model;


import service.ApiService;

import java.io.IOException;

public class CurrencyConversionService {
    public static double getConversionRatio(Constant original, Constant target) throws IOException {
        double result = 0;
        if (original.equals(Constant.UZS)) {
            double targetRate = Double.parseDouble(ApiService.getCurrency(String.valueOf(target)).getRate());
            result = 1 / targetRate;
        } else if (target.equals(Constant.UZS)) {
            double originalRate = Double.parseDouble(ApiService.getCurrency(String.valueOf(original)).getRate());
            result = originalRate;

        } else {
            double originalRate = Double.parseDouble(ApiService.getCurrency(String.valueOf(original)).getRate());
            double targetRate = Double.parseDouble(ApiService.getCurrency(String.valueOf(target)).getRate());
            result = originalRate / targetRate;
        }
        return result;


    }
}
