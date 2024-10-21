package wtf.melonthedev.log4minecraft.services;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import wtf.melonthedev.log4minecraft.Main;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;

public class JsonFile {

    private JSONObject cachedJsonFile;
    public File jsonFile;
    public final boolean cache;
    private String fileName;
    public String jsonRoot;

    public BukkitTask cacheTask;

    public JsonFile(String fileName, String jsonRoot, boolean cache) {
        this.cache = cache;
        this.jsonRoot = jsonRoot;
        setFile(fileName);
        if (cache) cacheTask = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(), this::saveToDisk, 0, 20L * Main.getPlugin().getConfig().getInt("cacheInterval", 60) * Main.logFileCacheDuration);
    }

    public void setFile(String fileName) {
        this.fileName = fileName;
        this.jsonFile = new File(Main.getPlugin().getDataFolder(), fileName);
        loadFromFile();
    }

    private void loadFromFile() {
        if (!jsonFile.exists()) {
            createNewFile();
        }
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(jsonFile)) {
            Object obj = jsonParser.parse(reader);
            cachedJsonFile = (JSONObject) obj;
        } catch (ParseException | IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Error loading json file " + fileName + " from disk (Corrupted file?) - Creating new file.");
            e.printStackTrace();
            jsonFile.renameTo(new File(jsonFile.getAbsolutePath() + "--" + UUID.randomUUID() + ".corrupted"));
            Bukkit.getLogger().log(Level.INFO, jsonFile.getAbsolutePath());
            this.jsonFile = new File(Main.getPlugin().getDataFolder(), fileName);
            createNewFile();
        }
    }

    private void createNewFile() {
        try {
            jsonFile.createNewFile();
            //Bukkit.getLogger().log(Level.SEVERE, "DFEBGG: " + jsonFile.length());
            try (FileWriter fw = new FileWriter(jsonFile)) {
                String json = "{\"" + jsonRoot + "\" : []}";
                cachedJsonFile = (JSONObject) new JSONParser().parse(json);
                fw.write(json);
                fw.flush();
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
            //Bukkit.getLogger().log(Level.SEVERE, "DFEBGG: " + jsonFile.length());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(JSONObject newJson) {
        cachedJsonFile = newJson;
        if (!cache) saveToDisk();
    }

    public void saveToDisk() {
        try (FileWriter file = new FileWriter(jsonFile)) {
            if (cachedJsonFile == null) {
                Bukkit.getLogger().log(Level.WARNING, "Cached log file not found. Unable to save.");
                return;
            }
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
