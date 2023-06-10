package wtf.melonthedev.log4minecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import wtf.melonthedev.log4minecraft.services.PlayerActivityService;

import java.util.List;

public class PlayerActivityCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "You are not allowed to use this command.");
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage("Usage: /playeractivity <player>");
            return false;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("Player not found.");
            return true;
        }
        if (!PlayerActivityService.playerActivities.containsKey(target.getUniqueId())) {
            sender.sendMessage("No data for this player.");
            return true;
        }
        sender.sendMessage(PlayerActivityService.playerActivities.get(target.getUniqueId()).getOreBreaksString());
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}
