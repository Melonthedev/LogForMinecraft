package wtf.melonthedev.log4minecraft;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import wtf.melonthedev.log4minecraft.enums.Action;
import wtf.melonthedev.log4minecraft.enums.LogLevel;
import wtf.melonthedev.log4minecraft.enums.LogOutput;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

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
        return "[" + DateFormat.getDateInstance().format(currentDate) + " "
                + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 10
                ? "0" + Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                : Calendar.getInstance().get(Calendar.HOUR_OF_DAY))
                + ":" + (Calendar.getInstance().get(Calendar.MINUTE) < 10
                ? "0" + Calendar.getInstance().get(Calendar.MINUTE)
                : Calendar.getInstance().get(Calendar.MINUTE))
                + "] ";
    }

    public static boolean hasPersistentDataContainer(Object obj) {
        return obj instanceof PersistentDataHolder;
    }

    public static OfflinePlayer getOwner(Object obj) {
        if (!hasPersistentDataContainer(obj)) return null;
        PersistentDataContainer container = ((PersistentDataHolder) obj).getPersistentDataContainer();
        return getOwner(container);
    }

    public static OfflinePlayer getOwner(ItemStack stack) {
        return getOwner(stack.getItemMeta().getPersistentDataContainer());
    }

    public static OfflinePlayer getOwner(PersistentDataContainer container) {
        if (!container.has(new NamespacedKey(Main.getPlugin(), "owner"))) return null;
        String owner = container.getOrDefault(new NamespacedKey(Main.getPlugin(), "owner"), PersistentDataType.STRING, "");
        try {
            UUID uuid = UUID.fromString(owner);
            return Bukkit.getOfflinePlayer(uuid);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static boolean isValidForLogLevel(LogEntry entry, LogLevel level) {
        if (!getLogAction(entry.action())) return false;
        switch (level) {
            case DISABLED -> {
                return false;
            }
            case VALUABLES -> {
                return getLevel(entry.target().getKey()) == LogLevel.VALUABLES;
            }
            case NORMAL -> {
                return getLevel(entry.target().getKey()) == LogLevel.VALUABLES
                        || getLevel(entry.target().getKey()) == LogLevel.NORMAL;
            }
            case DETAILED -> {
                return getLevel(entry.target().getKey()) == LogLevel.VALUABLES
                        || getLevel(entry.target().getKey()) == LogLevel.NORMAL
                        || getLevel(entry.target().getKey()) == LogLevel.DETAILED;
            }
            case EVERYTHING -> {
                return true;
            }
        }
        return false;
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
}
