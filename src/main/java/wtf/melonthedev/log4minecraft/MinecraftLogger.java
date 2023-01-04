package wtf.melonthedev.log4minecraft;

import org.json.simple.JSONObject;
import wtf.melonthedev.log4minecraft.enums.LogLevel;
import wtf.melonthedev.log4minecraft.enums.LogOutput;

import java.io.*;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class MinecraftLogger {

    public static void log(LogEntry entry) {
        if (!LoggerUtils.getLogAction(entry.getAction())) return; //Return if action shouldn't be logged
        if (LoggerUtils.getLogLevel(LogOutput.CONSOLE) != LogLevel.DISABLED) logToConsole(entry);
        if (LoggerUtils.getLogLevel(LogOutput.TEXTFILE) != LogLevel.DISABLED) logToTextFile(entry);
        if (LoggerUtils.getLogLevel(LogOutput.FILE) != LogLevel.DISABLED) logToFile(entry);
    }

    private static void logToConsole(LogEntry entry) {
        if (LoggerUtils.isValidForLogLevel(entry, LoggerUtils.getLogLevel(LogOutput.CONSOLE)))
            Main.getPlugin().getLogger().log(Level.INFO, entry.getLogText());
    }

    private static void logToTextFile(LogEntry entry) {
        if (!LoggerUtils.isValidForLogLevel(entry, LoggerUtils.getLogLevel(LogOutput.TEXTFILE))) return;
        Writer output;
        String fileName = "log4minecraft-" + Calendar.getInstance().get(Calendar.YEAR) +
                "-" + (Calendar.getInstance().get(Calendar.MONTH) + 1 < 10 ? "0" + Calendar.getInstance().get(Calendar.MONTH) + 1 : Calendar.getInstance().get(Calendar.MONTH) + 1) +
                "-" + (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) < 10 ? "0" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) : Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) + ".txt";
        File logFile = new File(Main.getPlugin().getDataFolder(), fileName);
        if (!logFile.exists()) {
            try {
                if (!logFile.createNewFile()) Main.getPlugin().getLogger().log(new LogRecord(Level.SEVERE,"Could not create log-file '" + fileName + "'"));
            } catch (IOException e) {
                Main.getPlugin().getLogger().log(new LogRecord(Level.SEVERE,"Could not create log-file '" + fileName + "'"));
                e.printStackTrace();
            }
        }
        try {
            output = new BufferedWriter(new FileWriter(logFile, true));
            output.append(entry.getLogText()).append("\n");
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void logToFile(LogEntry entry) {
        /*JSONObject object = LoggerUtils.getJsonObjFromFile();
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
        }*/
        //JsonObject obj = new JsonObject();
        //obj.addProperty("ddd", "lolPENIS");
        //String json = obj.getAsString();
        //System.out.println(obj);
        // pretty-print
        //String jsonString2 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new Staff());
        System.out.println("logtofile");
    }


}
