package wtf.melonthedev.log4minecraft.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import wtf.melonthedev.log4minecraft.*;
import wtf.melonthedev.log4minecraft.enums.LogLevel;
import wtf.melonthedev.log4minecraft.enums.LogOutput;

import java.util.ArrayList;
import java.util.List;

public class LogLevelCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) return true;
        switch (args.length) {
            case 0 -> {
                sender.sendMessage(Main.prefix + "Usage: /loglevel <output: console/textfile/file> <level: disabled/valuables/normal/detailed/everything>");
                sender.sendMessage(Main.prefix + "Status (Console): " + MinecraftLogger.getFormattedLogLevelString(MinecraftLogger.getLogLevel(LogOutput.CONSOLE)) + ChatColor.YELLOW + " - Textfile: " + MinecraftLogger.getFormattedLogLevelString(MinecraftLogger.getLogLevel(LogOutput.TEXTFILE)) + ChatColor.YELLOW + " - File: " + MinecraftLogger.getFormattedLogLevelString(MinecraftLogger.getLogLevel(LogOutput.FILE)));
            }
            case 1 -> {
                if (!LogOutput.names().contains(args[0])) {
                    sender.sendMessage(Main.prefix + "Usage: /loglevel <output: console/textfile/file> <level: disabled/valuables/normal/detailed/everything>");
                    return true;
                }
                sender.sendMessage(Main.prefix + "Status (" + args[0].toLowerCase() + "): " + MinecraftLogger.getFormattedLogLevelString(MinecraftLogger.getLogLevel(LogOutput.valueOf(args[0].toUpperCase()))));
            }
            case 2 -> {
                if (!LogOutput.names().contains(args[0]) || !LogLevel.names().contains(args[0])) {
                    sender.sendMessage(Main.prefix + "Usage: /loglevel <output: console/textfile/file> <level: disabled/valuables/normal/detailed/everything>");
                    return true;
                }
                MinecraftLogger.setLogLevel(LogOutput.valueOf(args[0].toUpperCase()), LogLevel.valueOf(args[1].toUpperCase()));
                sender.sendMessage(Main.prefix + "Successfully updated LogLevel of " + args[0].toUpperCase() + " to " + MinecraftLogger.getFormattedLogLevelString(LogLevel.valueOf(args[1].toUpperCase())));
            }
            default -> sender.sendMessage(Main.prefix + "Usage: /loglevel <output: console/textfile/file> <level: disabled/valuables/normal/detailed/everything>");
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> tab = new ArrayList<>();
        if (args.length == 1)
            tab.addAll(LogOutput.names());
        if (args.length == 2)
            tab.addAll(LogLevel.names());
        return tab;
    }
}
