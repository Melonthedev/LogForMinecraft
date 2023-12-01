package wtf.melonthedev.log4minecraft;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import wtf.melonthedev.log4minecraft.commands.*;
import wtf.melonthedev.log4minecraft.services.JsonFile;
import wtf.melonthedev.log4minecraft.services.LogFile;
import wtf.melonthedev.log4minecraft.services.MinecraftLogger;
import wtf.melonthedev.log4minecraft.services.PlayerActivityService;

import java.time.LocalDate;
import java.util.logging.Level;

public final class Main extends JavaPlugin {

    public static String prefix = ChatColor.BOLD + ChatColor.AQUA.toString() + "[Log4MC] " + ChatColor.YELLOW;
    private static JavaPlugin plugin;
    public static LogFile logFile;
    public static JsonFile invBackupFile;

    public static boolean useExactLocations = false;
    public static boolean createInvBackupOnDeath = true;
    public static boolean createInvBackupOnLeave = false;
    public static boolean logActionsInPlayersInventory = false;
    public static boolean logNonContainerBlockRightClicks = true;
    public static boolean checkSusPlayerActivity = true;
    public static int logFileCacheDuration = 1;

    @Override
    public void onEnable() {
        plugin = this;
        getLogger().log(Level.INFO, "*************");
        getLogger().log(Level.INFO, "Log4Minecraft");
        getLogger().log(Level.INFO, "*************");
        handleConfig();
        initJsonFiles();
        MinecraftLogger.init();

        //Register Commands
        getCommand("logoutput").setExecutor(new LogOutputCommand());
        getCommand("logaction").setExecutor(new LogActionCommand());
        getCommand("playeractivity").setExecutor(new PlayerActivityCommand());
        getCommand("findevent").setExecutor(new FindEventCommand());
        getCommand("logcategory").setExecutor(new LogCategoryCommand());
        getCommand("manageinventory").setExecutor(new ManageInventoryCommand());

        //Register Listeners
        Bukkit.getPluginManager().registerEvents(new ActionListeners(), this);

        PlayerActivityService.handlePlayerActivitySystem();
    }


    public void handleConfig() {
        saveDefaultConfig();
        useExactLocations = getConfig().getBoolean("useExactLocations", false);
        createInvBackupOnDeath = getConfig().getBoolean("createInventoryBackups.onDeath", true);
        createInvBackupOnLeave = getConfig().getBoolean("createInventoryBackups.onLeave", false);
        logActionsInPlayersInventory = getConfig().getBoolean("logActionsInPlayersInventory", false);
        logNonContainerBlockRightClicks = getConfig().getBoolean("logNonContainerBlockRightClicks", true);
        checkSusPlayerActivity = getConfig().getBoolean("checkSusPlayerActivity", true);
        logFileCacheDuration = getConfig().getInt("logFileCacheDuration", 1);
    }


    public void initJsonFiles() {
        logFile = new LogFile(LocalDate.now(), true);
        invBackupFile = new JsonFile("log4minecraft-inventorybackups.json", "invbackups", true);
    }

    @Override
    public void onDisable() {
        logFile.saveToDisk();
        invBackupFile.saveToDisk();
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }
}
