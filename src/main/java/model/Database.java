package model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


import java.io.*;
import java.util.*;

public interface Database {
    Set<User> users = new LinkedHashSet<>();

    Map<String, List> map = new HashMap<>();


    static void writeJson(String name) {
        File file = new File("src/main/resources/users.json");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(gson.toJson(map.get(name)));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void readJson() {
        Gson gson = new Gson();
        File file = new File("src/main/resources/users.json");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            Set<User> userSet = gson.fromJson(reader, new TypeToken<Set<User>>() {
            }.getType());
            users.addAll(userSet);
            map.put(Constant2.USER, Collections.singletonList(userSet));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


}
