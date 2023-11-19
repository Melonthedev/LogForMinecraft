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
import java.util.logging.Level;

public class JsonFile {

    private JSONObject cachedJsonFile;
    private final File jsonFile;
    private final boolean cache;
    public String fileName;

    public JsonFile(String fileName, String jsonRoot, boolean cache) {
        this.cache = cache;
        this.fileName = fileName;
        jsonFile = new File(Main.getPlugin().getDataFolder(), fileName);
        loadFromFile(jsonRoot);
        if (cache) Bukkit.getScheduler().runTaskTimer(Main.getPlugin(), this::saveToDisk, 0, 20L * 60 * Main.logFileCacheDuration);
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
        try (FileReader reader = new FileReader(jsonFile))
        {
            Object obj = jsonParser.parse(reader);
            JSONObject jo = (JSONObject) obj;
            cachedJsonFile = jo;
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }


    public void write(JSONObject newJson) {
        cachedJsonFile = newJson;
        if (!cache) saveToDisk();
    }

    public JSONObject get() {
        return cachedJsonFile;
    }

    public void saveToDisk() {
        //Main.getPlugin().getLogger().log(Level.INFO, "SAVING TO DISK <------------------------------------------------------------");
        try (FileWriter file = new FileWriter(jsonFile)) {
            file.write(cachedJsonFile.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
