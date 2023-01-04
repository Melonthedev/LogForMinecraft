package wtf.melonthedev.log4minecraft;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import wtf.melonthedev.log4minecraft.enums.LogLevel;
import wtf.melonthedev.log4minecraft.enums.LogOutput;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

public class LoggerUtils {

    public static void setLogLevel(LogOutput output, LogLevel level) {
        Main.getPlugin().getConfig().set("loglevels." + output.name(), level.name());
        Main.getPlugin().saveConfig();
    }

    public static LogLevel getLogLevel(LogOutput output) {
        if (!Main.getPlugin().getConfig().contains("loglevels." + output.name())) {
            setLogLevel(output, LogLevel.NORMAL);
        }
        return LogLevel.valueOf(Main.getPlugin().getConfig().getString("loglevels." + output.name()));
    }

    public static void setLogAction(Action action, boolean log) {
        Main.getPlugin().getConfig().set("logactions." + action.name(), log);
        Main.getPlugin().saveConfig();
    }

    public static boolean getLogAction(Action action) {
        if (!Main.getPlugin().getConfig().contains("logactions." + action.name()))
            setLogAction(action, true);
        return Main.getPlugin().getConfig().getBoolean("logactions." + action.name());
    }

    public static String getFormattedLogLevelString(LogLevel level) {
        return switch(level) {
            case DISABLED -> ChatColor.RED + "Disabled";
            case VALUABLES -> ChatColor.LIGHT_PURPLE + "Valuables";
            case NORMAL -> ChatColor.AQUA + "Normal";
            case DETAILED -> ChatColor.DARK_GREEN + "Detailed";
            case EVERYTHING -> ChatColor.GREEN + "Everything";
        };
    }

    public static LogLevel getLevel(String type) {
        try {
            return LogLevel.valueOf(Main.getPlugin().getConfig().getString("logLevels." + type, "normal"));
        } catch (IllegalArgumentException e) {
            return LogLevel.NORMAL;
        }
    }

    public static String getDateTimeString() {
        Date currentDate = new Date();
        return "[" + DateFormat.getDateInstance().format(currentDate) + " " + (currentDate.getHours() < 10 ? "0" + currentDate.getHours() : currentDate.getHours()) + ":" + (currentDate.getMinutes() < 10 ? "0" + currentDate.getMinutes() : currentDate.getMinutes()) + "] ";
    }

    public static JSONObject getJsonObjFromFile() {
        JSONParser jsonParser = new JSONParser();
        if (!new File(Main.getPlugin().getDataFolder(), "log4minecraft.json").exists()) {
            try {
                new File(Main.getPlugin().getDataFolder(), "log4minecraft.json").createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (FileReader reader = new FileReader(new File(Main.getPlugin().getDataFolder(), "log4minecraft.json")))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONObject jo = (JSONObject) obj;
            System.out.println(jo);
            return jo;
            //Iterate over employee array
            //employeeList.forEach( emp -> parseEmployeeObject( (JSONObject) emp ) );

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isValidForLogLevel(LogEntry entry, LogLevel level) {
        if (!getLogAction(entry.getAction())) return false;
        switch (level) {
            case DISABLED -> {
                return false;
            }
            case VALUABLES -> {
                return getLevel(entry.type) == LogLevel.VALUABLES;
            }
            case NORMAL -> {
                return getLevel(entry.type) == LogLevel.VALUABLES
                        || getLevel(entry.type) == LogLevel.NORMAL;
            }
            case DETAILED -> {
                return getLevel(entry.type) == LogLevel.VALUABLES
                        || getLevel(entry.type) == LogLevel.NORMAL
                        || getLevel(entry.type) == LogLevel.DETAILED;
            }
            case EVERYTHING -> {
                return true;
            }
        }
        return false;
    }

}
