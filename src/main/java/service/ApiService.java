package service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Currency;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

public class ApiService {
    public static Currency getCurrency(String currency) throws IOException {
        HttpGet httpGet = new HttpGet("https://cbu.uz/en/arkhiv-kursov-valyut/json/" + currency + "/");
        HttpClient client = HttpClients.createDefault();
        HttpResponse response = client.execute(httpGet);
        Gson gson = new Gson();
        Reader reader = new InputStreamReader(response.getEntity().getContent());
        List<Currency> currencyList = gson.fromJson(reader, new TypeToken<List<Currency>>() {
        }.getType());
        return currencyList.get(0);
    }

    public static List<Currency> getCurrencyAll() throws IOException {
        HttpGet httpGet = new HttpGet("https://cbu.uz/en/arkhiv-kursov-valyut/json/");
        HttpClient client = HttpClients.createDefault();
        HttpResponse response = client.execute(httpGet);
        Gson gson = new Gson();
        Reader reader = new InputStreamReader(response.getEntity().getContent());
        List<Currency> currencyList = gson.fromJson(reader, new TypeToken<List<Currency>>() {
        }.getType());
        return currencyList;
    }


}
