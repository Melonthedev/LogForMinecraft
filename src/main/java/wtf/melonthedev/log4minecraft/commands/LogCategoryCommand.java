package wtf.melonthedev.log4minecraft.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wtf.melonthedev.log4minecraft.utils.LoggerUtils;
import wtf.melonthedev.log4minecraft.Main;
import wtf.melonthedev.log4minecraft.enums.LogLevel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LogCategoryCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp()) return true;
        if (args.length < 1) {
            sender.sendMessage(Main.prefix + "Syntaxerror: /logCategory <Type> <DISABLED/VALUABLE/NORMAL/DETAILED>");
            return true;
        }
        try {
            Material.valueOf(args[0]);
        } catch (IllegalArgumentException e) {
            try {
                EntityType.valueOf(args[0]);
            } catch (IllegalArgumentException ex) {
                sender.sendMessage(Main.prefix + ChatColor.RED + "Make sure you used a valid material (e.g. GRASS_BLOCK) or entity type (e.g ZOMBIE or WITHER_SKELETON)");
                return true;
            }
        }
        if (args.length == 1) {
            LogLevel category = LogLevel.valueOf(Main.getPlugin().getConfig().getString("logLevels." + args[0].toLowerCase(), "normal").toUpperCase());
            sender.sendMessage(Main.prefix + "Current category of " + ChatColor.GRAY + args[0].toLowerCase() + ChatColor.YELLOW + ": " + LoggerUtils.getFormattedLogLevelString(category));
        } else if (args.length == 2) {
            try {
                LogLevel category = LogLevel.valueOf(args[1].toUpperCase());
                Main.getPlugin().getConfig().set("logLevels." + args[0].toLowerCase(), category.name());
                Main.getPlugin().saveConfig();
                sender.sendMessage(Main.prefix + "Successfully updated LogCategory of " + args[0].toUpperCase() + " to " + LoggerUtils.getFormattedLogLevelString(category));
            } catch (IllegalArgumentException e) {
                sender.sendMessage(Main.prefix + ChatColor.RED + "Please use a valid category: DISABLED/VALUABLE/NORMAL/DETAILED");
                return true;
            }
        } else sender.sendMessage(Main.prefix + "Syntaxerror: /logCategory <Type> <DISABLED/VALUABLE/NORMAL/DETAILED>");
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> tab = new ArrayList<>();
        if (!sender.isOp()) return tab;
        if (args.length == 1) {
            tab.addAll(Arrays.stream(Material.values()).map(Enum::name).filter(s -> s.startsWith(args[0].toUpperCase())).toList());
            tab.addAll(Arrays.stream(EntityType.values()).map(Enum::name).filter(s -> s.startsWith(args[0].toUpperCase())).toList());
        } else if (args.length == 2) {
            tab.add("NORMAL");
            tab.add("VALUABLE");
            tab.add("DETAILED");
            tab.add("DISABLED");
        }
        return tab;
    }
}
