package wtf.melonthedev.log4minecraft;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

        //Register Commands
        getCommand("loglevel").setExecutor(new LogLevelCommand());
        getCommand("logaction").setExecutor(new LogActionCommand());

        //Register Listeners
        Bukkit.getPluginManager().registerEvents(new ActionListeners(), this);
    }

    @Override
    public void onDisable() {

    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }
}
