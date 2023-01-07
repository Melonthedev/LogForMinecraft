package wtf.melonthedev.log4minecraft;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;
import wtf.melonthedev.log4minecraft.commands.*;

import java.util.logging.Level;

public final class Main extends JavaPlugin {

    public static String prefix = ChatColor.BOLD + ChatColor.AQUA.toString() + "[Log4MC] " + ChatColor.YELLOW;
    private static JavaPlugin plugin;
    public static boolean useExactLocations = false;

    @Override
    public void onEnable() {
        plugin = this;
        getLogger().log(Level.INFO, "*************");
        getLogger().log(Level.INFO, "Log4Minecraft");
        getLogger().log(Level.INFO, "*************");
        handleConfig();

        //Register Commands
        getCommand("loglevel").setExecutor(new LogLevelCommand());
        getCommand("logaction").setExecutor(new LogActionCommand());
        getCommand("playeractivity").setExecutor(new PlayerActivityCommand());
        getCommand("findevent").setExecutor(new FindEventCommand());
        getCommand("logcategory").setExecutor(new LogCategoryCommand());
        getCommand("manageinventory").setExecutor(new ManageInventoryCommand());

        //Register Listeners
        Bukkit.getPluginManager().registerEvents(new ActionListeners(), this);


        /*for (Material material : Material.values()) {
            try {
                getLogger().log(Level.INFO, material.getKey().toString().replaceAll("minecraft:", "") + ": normal");
            } catch (Exception e) {
                continue;
            }
        }
        for (EntityType material : EntityType.values()) {
            try {
                getLogger().log(Level.INFO, material.getKey().toString().replaceAll("minecraft:", "") + ": normal");
            } catch (Exception e) {
                continue;
            }
        }*/
    }


    public void handleConfig() {
        saveDefaultConfig();
        useExactLocations = getConfig().getBoolean("useExactLocations", false);
    }

    @Override
    public void onDisable() {

    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }
}
