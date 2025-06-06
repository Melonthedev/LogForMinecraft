package wtf.melonthedev.log4minecraft.services;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import wtf.melonthedev.log4minecraft.LogEntry;
import wtf.melonthedev.log4minecraft.Main;
import wtf.melonthedev.log4minecraft.enums.LogOutput;
import wtf.melonthedev.log4minecraft.utils.LoggerUtils;

import java.io.*;
import java.time.Instant;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class MinecraftLogger {


    private static File logFile;
    private static int currentFileNameDayOfMonth = 0;

    public static void init() {
        String fileName = "log4minecraft-" + Calendar.getInstance().get(Calendar.YEAR) +
                "-" + LoggerUtils.getWithZeros(Calendar.getInstance().get(Calendar.MONTH) + 1) +
                "-" + LoggerUtils.getWithZeros(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) + ".txt";
        currentFileNameDayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        logFile = new File(Main.getPlugin().getDataFolder(), fileName);
        if (!logFile.exists()) {
            try {
                if (!logFile.createNewFile()) Main.getPlugin().getLogger().log(new LogRecord(Level.SEVERE,"Could not create log-file '" + fileName + "'"));
            } catch (IOException e) {
                Main.getPlugin().getLogger().log(new LogRecord(Level.SEVERE,"Could not create log-file '" + fileName + "'"));
                e.printStackTrace();
            }
        }
    }


    public static void log(LogEntry entry) {
        if (!LoggerUtils.shouldLogAction(entry.getAction()))
            return;
        logToConsole(entry);
        logToTextFile(entry);
        logToFile(entry);
    }

    private static void validateTextFile() {
        if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) != currentFileNameDayOfMonth) init();
    }

    private static void logToConsole(LogEntry entry) {
        if (LoggerUtils.isValidForLogLevel(entry, LoggerUtils.getLogLevel(LogOutput.CONSOLE)))
            Main.getPlugin().getLogger().log(Level.INFO, entry.getLogText());
    }

    private static void logToTextFile(LogEntry entry) {
        if (!LoggerUtils.isValidForLogLevel(entry, LoggerUtils.getLogLevel(LogOutput.TEXTFILE))) return;
        validateTextFile();
        Writer output;
        try {
            output = new BufferedWriter(new FileWriter(logFile, true));
            output.append(entry.getLogText()).append("\n");
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void logToFile(LogEntry entry) {
        if (!LoggerUtils.isValidForLogLevel(entry, LoggerUtils.getLogLevel(LogOutput.FILE))) return;
        JSONObject object = Main.logFile.get();
        if (object == null) return;
        JSONArray array = (JSONArray) object.get("logs");
        JSONObject log = new JSONObject();
        JSONObject location = new JSONObject();
        location.put("x", entry.getLocation().getBlockX());
        location.put("y", entry.getLocation().getBlockY());
        location.put("z", entry.getLocation().getBlockZ());
        location.put("w", entry.getLocation().getWorld().getName());

        log.put("subject", entry.getSubject().getLabel());
        if (entry.getSubject().getEntity() != null)
            log.put("subjectUUID", entry.getSubject().getEntity().getUniqueId().toString());

        log.put("action", entry.getAction().name());
        if (entry.getTarget() != null) log.put("target", entry.getTarget().getLabel());
        log.put("location", location);
        log.put("created", Instant.now().getEpochSecond());
        if (entry.getOwner() != null) log.put("owner", entry.getOwner().getName());
        if (entry.getOwner() != null) log.put("ownerUUID", entry.getOwner().getUniqueId().toString());

        array.add(log);
        Main.logFile.write(object);
    }
}
