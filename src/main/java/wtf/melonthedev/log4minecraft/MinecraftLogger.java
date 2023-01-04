package wtf.melonthedev.log4minecraft;

import org.bukkit.ChatColor;
import org.json.simple.JSONObject;
import wtf.melonthedev.log4minecraft.enums.LogLevel;
import wtf.melonthedev.log4minecraft.enums.LogOutput;

import java.io.*;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class MinecraftLogger {

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
        if (!Main.getPlugin().getConfig().contains("logactions." + action.name())) {
            setLogAction(action, true);
        }
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


    public static void log(LogEntry entry) {
        if (!getLogAction(entry.getAction())) return; //Return if action shouldnt be logged

        if (getLogLevel(LogOutput.CONSOLE) != LogLevel.DISABLED) logToConsole(LoggerUtils.getLogText(entry)); //TODO: implement loglevel system thing
        if (getLogLevel(LogOutput.TEXTFILE) != LogLevel.DISABLED) logToTextFile(LoggerUtils.getLogText(entry));
        if (getLogLevel(LogOutput.FILE) != LogLevel.DISABLED) logToFile(entry);
    }

    private static void logToTextFile(String text) {
        Writer output;
        String fileName = "log4minecraft-" + Calendar.getInstance().get(Calendar.YEAR) +
                "-" + (Calendar.getInstance().get(Calendar.MONTH) + 1 < 10 ? "0" + Calendar.getInstance().get(Calendar.MONTH) + 1 : Calendar.getInstance().get(Calendar.MONTH) + 1) +
                "-" + (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) < 10 ? "0" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) : Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) + ".txt";
        File logFile = new File(Main.getPlugin().getDataFolder(), fileName);
        if (!logFile.exists()) {
            try {
                if (!logFile.createNewFile()) Main.getPlugin().getLogger().log(new LogRecord(Level.SEVERE,"Could not create log-file '" + fileName + "'"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            output = new BufferedWriter(new FileWriter(logFile, true));
            output.append(text).append("\n");
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void logToFile(LogEntry entry) {
        JSONObject object = LoggerUtils.getJsonObjFromFile();
        JSONObject log = new JSONObject();
        log.put("player", "melonthedev");
        log.put("action", "BROKE");
        //object.("logs", log);
        System.out.println(object);
        System.out.println(log);
        try (FileWriter file = new FileWriter("fileName.json")) {
            file.write(object.toString()); // data is a JSON string here
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //JsonObject obj = new JsonObject();
        //obj.addProperty("ddd", "lolPENIS");
        //String json = obj.getAsString();
        //System.out.println(obj);
        // pretty-print
        //String jsonString2 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new Staff());
    }

    private static void logToConsole(String text) {
        Main.getPlugin().getLogger().log(Level.INFO, text);
    }

}
