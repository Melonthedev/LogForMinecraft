package wtf.melonthedev.log4minecraft.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.ChatColor;
import wtf.melonthedev.log4minecraft.LogEntry;
import wtf.melonthedev.log4minecraft.LogTarget;
import wtf.melonthedev.log4minecraft.Main;
import wtf.melonthedev.log4minecraft.enums.Action;
import wtf.melonthedev.log4minecraft.enums.LogLevel;
import wtf.melonthedev.log4minecraft.enums.LogOutput;

import java.io.File;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class LoggerUtils {

    //LogLevel & LogAction
    public static void setLogLevel(LogOutput output, LogLevel level) {
        Main.getPlugin().getConfig().set("logOutputLevels." + output.name(), level.name());
        Main.getPlugin().saveConfig();
    }
    public static LogLevel getLogLevel(LogOutput output) {
        if (!Main.getPlugin().getConfig().contains("logOutputLevels." + output.name()))
            setLogLevel(output, LogLevel.NORMAL);
        return LogLevel.valueOf(Main.getPlugin().getConfig().getString("logOutputLevels." + output.name()));
    }
    public static void setLogAction(Action action, boolean log) {
        Main.getPlugin().getConfig().set("logactions." + action.name(), log);
        Main.getPlugin().saveConfig();
    }
    public static boolean shouldLogAction(Action action) {
        if (!Main.getPlugin().getConfig().contains("logactions." + action.name()))
            setLogAction(action, true);
        return Main.getPlugin().getConfig().getBoolean("logactions." + action.name());
    }

    //Validation Methods
    public static LogLevel getLevel(String type) {
        try {
            return LogLevel.valueOf(Main.getPlugin().getConfig().getString("logLevels." + type.toLowerCase(), "normal").toUpperCase());
        } catch (IllegalArgumentException e) {
            return LogLevel.NORMAL;
        }
    }
    public static boolean isValidForLogLevel(LogEntry entry, LogLevel level) {
        if (!shouldLogAction(entry.getAction())) return false;
        return isValidForLogLevel(entry.getSubject(), level) && isValidForLogLevel(entry.getTarget(), level);
    }
    public static boolean isValidForLogLevel(LogTarget target, LogLevel level) {
        if (target == null) return true;
        return isValidForLogLevel(target.getKey(), level);
    }
    public static boolean isValidForLogLevel(String key, LogLevel level) {
        switch (level) {
            case DISABLED -> {
                return false;
            }
            case VALUABLE -> {
                return getLevel(key) == LogLevel.VALUABLE;
            }
            case NORMAL -> {
                return getLevel(key) == LogLevel.VALUABLE
                        || getLevel(key) == LogLevel.NORMAL;
            }
            case DETAILED -> {
                return getLevel(key) == LogLevel.VALUABLE
                        || getLevel(key) == LogLevel.NORMAL
                        || getLevel(key) == LogLevel.DETAILED;
            }
            case EVERYTHING -> {
                return true;
            }
        }
        return false;
    }

    public static File getExistingJsonFile(String name) {
        return new File(Main.getPlugin().getDataFolder(), name);
    }

    //UTILS
    public static String getWithZeros(int i) {
        return i < 10 ? "0" + i : String.valueOf(i);
    }
    public static boolean isComponentEmpty(Component component) {
        return component == null || PlainTextComponentSerializer.plainText().serialize(component).isBlank();
    }
    public static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static boolean isValidLogLevel(String s) {
        try {
            LogLevel.valueOf(s);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    public static String getFormattedLogLevelString(LogLevel level) {
        return switch(level) {
            case DISABLED -> ChatColor.RED + "Disabled";
            case VALUABLE -> ChatColor.LIGHT_PURPLE + "Valuable";
            case NORMAL -> ChatColor.AQUA + "Normal";
            case DETAILED -> ChatColor.DARK_GREEN + "Detailed";
            case EVERYTHING -> ChatColor.GREEN + "Everything";
        };
    }
    public static String getDateTimeString() {
        Date currentDate = new Date();
        return "[" + DateFormat.getDateInstance().format(currentDate) + " "
                + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 10
                ? "0" + Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                : Calendar.getInstance().get(Calendar.HOUR_OF_DAY))
                + ":" + (Calendar.getInstance().get(Calendar.MINUTE) < 10
                ? "0" + Calendar.getInstance().get(Calendar.MINUTE)
                : Calendar.getInstance().get(Calendar.MINUTE))
                + "] ";
    }
}
