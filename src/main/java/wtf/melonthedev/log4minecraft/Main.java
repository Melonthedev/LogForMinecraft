package wtf.melonthedev.log4minecraft;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;
import wtf.melonthedev.log4minecraft.commands.LogActionCommand;
import wtf.melonthedev.log4minecraft.commands.LogLevelCommand;

import java.util.logging.Level;

public final class Main extends JavaPlugin {

    public static String prefix = ChatColor.BOLD + ChatColor.AQUA.toString() + "[Log4Minecraft] " + ChatColor.YELLOW;
    private static JavaPlugin plugin;

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
    }

    @Override
    public void onDisable() {

    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }
}
