package wtf.melonthedev.log4minecraft.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import wtf.melonthedev.log4minecraft.enums.Action;
import wtf.melonthedev.log4minecraft.LoggerUtils;
import wtf.melonthedev.log4minecraft.Main;

import java.util.ArrayList;
import java.util.List;

public class LogActionCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) return true;
        switch (args.length) {
            case 1 -> {
                if (!Action.names().contains(args[0].toUpperCase())) {
                    sender.sendMessage(Main.prefix + "Usage: /logAction <action> <true/false>");
                    return true;
                }
                sender.sendMessage(Main.prefix + "Status (" + args[0].toLowerCase() + "): " + LoggerUtils.getLogAction(Action.valueOf(args[0].toUpperCase())));
            }
            case 2 -> {
                if (!Action.names().contains(args[0]) || (!args[1].equalsIgnoreCase("true") && !args[1].equalsIgnoreCase("false"))) {
                    sender.sendMessage(Main.prefix + "Usage: /logAction <action> <true/false>");
                    return true;
                }
                LoggerUtils.setLogAction(Action.valueOf(args[0]), Boolean.parseBoolean(args[1]));
                sender.sendMessage(Main.prefix + "Success! Actions from type '" + args[0].toUpperCase() + "' " + (Boolean.parseBoolean(args[1]) ? "will" : "won't") + " be logged in the future.");
            }
            default -> sender.sendMessage(Main.prefix + "Usage: /logAction <action> <true/false>");
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> tab = new ArrayList<>();
        if (args.length == 1)
            tab.addAll(Action.names());
        if (args.length == 2) {
            tab.add("true");
            tab.add("false");
        }
        return tab;
    }
}
