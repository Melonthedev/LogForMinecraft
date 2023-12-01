package wtf.melonthedev.log4minecraft.services;

import org.bukkit.Bukkit;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import wtf.melonthedev.log4minecraft.Main;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JsonFile {

    private JSONObject cachedJsonFile;
    public File jsonFile;
    public final boolean cache;
    private String fileName;
    public String jsonRoot;

    public JsonFile(String fileName, String jsonRoot, boolean cache) {
        this.cache = cache;
        this.jsonRoot = jsonRoot;
        setFile(fileName);
        if (cache) Bukkit.getScheduler().runTaskTimer(Main.getPlugin(), this::saveToDisk, 0, 20L * 60 * Main.logFileCacheDuration);
    }

    public void setFile(String fileName) {
        this.fileName = fileName;
        this.jsonFile = new File(Main.getPlugin().getDataFolder(), fileName);
        loadFromFile(jsonRoot);
    }

    private void loadFromFile(String jsonRoot) {
        if (!jsonFile.exists()) {
            try {
                jsonFile.createNewFile();
                try (FileWriter fw = new FileWriter(jsonFile)) {
                    fw.write("{\"" + jsonRoot + "\" : []}");
                    fw.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(jsonFile)) {
            Object obj = jsonParser.parse(reader);
            cachedJsonFile = (JSONObject) obj;
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    public void write(JSONObject newJson) {
        cachedJsonFile = newJson;
        if (!cache) saveToDisk();
    }

    public void saveToDisk() {
        try (FileWriter file = new FileWriter(jsonFile)) {
            file.write(cachedJsonFile.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JSONObject get() {
        return cachedJsonFile;
    }

    public String getFileName() {
        return fileName;
    }

}
