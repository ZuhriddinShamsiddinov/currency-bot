package model;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


import java.io.*;
import java.util.*;

public interface DatabaseStory {
    Set<Story> story = new LinkedHashSet<>();

    Map<String, List> map = new HashMap<>();


    public static void writeJson(String name) {
        File file = new File("src/main/resources/story.json");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(gson.toJson(map.get(name)));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readJson() {
        Gson gson = new Gson();
        File file = new File("src/main/resources/story.json");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            List<Set<Story>> userSet = gson.fromJson(reader, new TypeToken<List<Set<Story>>>() {
            }.getType());
            story.addAll(userSet.get(0));
            map.put(Constant2.USER, Collections.singletonList(userSet.get(0)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
