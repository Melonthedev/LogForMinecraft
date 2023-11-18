package wtf.melonthedev.log4minecraft.services;

import org.json.simple.JSONObject;
import wtf.melonthedev.log4minecraft.Main;
import wtf.melonthedev.log4minecraft.utils.LoggerUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JsonFile {

    private JSONObject cachedJsonFile;
    private final File jsonFile;


    public JsonFile(String fileName) {
        jsonFile = new File(Main.getPlugin().getDataFolder(), fileName);
        loadFromFile();
        //TODO: RUN MODIFICATION CHECK AND SAVE TO DISK INTERVALL
    }

    private void loadFromFile() {

        if (!file.exists()) {
            try {
                file.createNewFile();
                try (FileWriter fw = new FileWriter(file)) {//TODO IMPLEM ENTAAAHHH GOOFY
                    fw.write("{\"" + jsonRoot + "\" : []}");
                    fw.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
        File file = getJsonFile(fileName, "logs");
        return getJsonObject(file);
    }


    public void write(JSONObject newJson) {
        cachedJsonFile = newJson;
    }

    public JSONObject get() {
        return cachedJsonFile;
    }

    public void saveToDisk() {
        try (FileWriter file = new FileWriter(jsonFile)) {
            file.write(cachedJsonFile.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
