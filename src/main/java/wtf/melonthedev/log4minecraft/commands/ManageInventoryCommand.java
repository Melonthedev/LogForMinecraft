package wtf.melonthedev.log4minecraft.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wtf.melonthedev.log4minecraft.InventoryBackup;
import wtf.melonthedev.log4minecraft.services.InventoryBackupService;
import wtf.melonthedev.log4minecraft.utils.LoggerUtils;
import wtf.melonthedev.log4minecraft.Main;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class ManageInventoryCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp()) return true;
        // /manageinventory Melonthedev view/backup/listbackups/restorebackup
        if (args.length < 2) {
            sender.sendMessage(Main.prefix + "Syntaxerror: /manageinventory <player> <view/backup/listbackups/restorebackup> (<backupId>)");
            return true;
        }
        OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[0]);
        if (target == null) {
            sender.sendMessage(Main.prefix + "Player not found!");
            return true;
        }
        if (args.length == 2) {
            switch (args[1].toLowerCase()) {
                case "view" -> {
                    if (!target.isOnline()) {
                        sender.sendMessage(Main.prefix + "Player not online!");
                        return true;
                    }
                    if (sender instanceof Player player) player.openInventory(((Player) target).getInventory());
                    else sendTextInventory(sender, ((Player) target).getInventory());
                }
                case "backup" -> {
                    if (!target.isOnline()) {
                        sender.sendMessage(Main.prefix + "Player not online!");
                        return true;
                    }
                    InventoryBackupService.backupInventory((Player) target);
                    sender.sendMessage(Main.prefix + "Backed up inventory of " + target.getName());
                }
                case "listbackups" -> {
                    sender.sendMessage(Main.prefix + "Inventory Backups of " + target.getName() + ":");
                    InventoryBackupService.getInventoryBackups(target.getUniqueId()).forEach(backup -> sender.sendMessage(Component.text(ChatColor.GRAY +
                            "- " + ChatColor.AQUA + "ID: " + backup.getBackupId() + ChatColor.YELLOW +
                            ", Created at " + ChatColor.GREEN + Instant.ofEpochSecond(backup.getCreated()).toString() + ChatColor.LIGHT_PURPLE +
                            " from " + (Bukkit.getOfflinePlayer(backup.getOwner()).getName() == null ? backup.getOwner() : Bukkit.getOfflinePlayer(backup.getOwner()).getName()))
                            .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/manageinventory " + Bukkit.getOfflinePlayer(backup.getOwner()).getName() + " view " + backup.getBackupId()))));
                }
            }
        } else if (args.length == 3) {
            if (!LoggerUtils.isInt(args[2])) {
                sender.sendMessage(Main.prefix + "Backup id must be an integer!");
                return true;
            }
            int backupId = Integer.parseInt(args[2]);
            InventoryBackup backup = InventoryBackupService.getInventoryBackup(target.getUniqueId(), backupId);
            if (backup == null) {
                sender.sendMessage(Main.prefix + "Backup not found!");
                return true;
            }
            switch (args[1].toLowerCase()) {
                case "view" -> {
                    if (sender instanceof Player player) player.openInventory(backup.getPlayerInventory());
                    else sendTextInventory(sender, backup.getPlayerInventory());
                }
                case "restorebackup" -> {
                    if (!target.isOnline()) {
                        sender.sendMessage(Main.prefix + "Player not online!");
                        return true;
                    }
                    ((Player) target).getInventory().setContents(backup.getContents());
                    sender.sendMessage(Main.prefix + "Successfully restored inventory backup.");
                }
            }
        }
        InventoryBackupService.getInventoryBackups();
        return false;
    }

    public String getEnchantmentsString(Map<Enchantment, Integer> list) {
        if (list.isEmpty()) return "";
        StringBuilder sb = new StringBuilder("(");
        list.forEach((enchantment, integer) -> sb.append(StringUtils.capitalize(enchantment.getKey().asString().toLowerCase().replaceAll("minecraft:", ""))).append(" ").append(integer).append(", "));
        return sb.substring(0, sb.length() - 2) + ")";
    }

    public void sendTextInventory(CommandSender sender, Inventory inventory) {
        sender.sendMessage(ChatColor.GOLD + "---- Inventory Backup Viewer -----");
        /*sender.sendMessage(ChatColor.AQUA + "Armor:");
            if (stack == null) continue;
            sender.sendMessage(ChatColor.AQUA + "- " + ChatColor.YELLOW + stack.getAmount() + "x " + ChatColor.AQUA + StringUtils.capitalize(stack.getType().getKey().asString().replaceAll("minecraft:", "").toLowerCase().replaceAll("_", " ")) + " " + getEnchantmentsString(stack.getEnchantments()));
        }
        sender.sendMessage(ChatColor.BLUE + "Extra:");
        for (ItemStack stack : inventory.getExtraContents()) {
            if (stack == null) continue;
            sender.sendMessage(ChatColor.BLUE + "- " + ChatColor.YELLOW + stack.getAmount() + "x " + ChatColor.BLUE + StringUtils.capitalize(stack.getType().getKey().asString().replaceAll("minecraft:", "").toLowerCase().replaceAll("_", " ")) + " " + getEnchantmentsString(stack.getEnchantments()));
        }*/
        sender.sendMessage(ChatColor.GREEN + "Storage:");
        for (ItemStack stack : inventory.getContents()) {
            if (stack == null) continue;
            sender.sendMessage(ChatColor.GREEN + "- " + ChatColor.YELLOW + stack.getAmount() + "x " + ChatColor.GREEN + StringUtils.capitalize(stack.getType().getKey().asString().replaceAll("minecraft:", "").toLowerCase().replaceAll("_", " ")) + " " + getEnchantmentsString(stack.getEnchantments()));
        }
        /*sender.sendMessage(ChatColor.LIGHT_PURPLE + "EnderChest:");
        for (ItemStack stack : inventory.getStorageContents()) {
            if (stack == null) continue;
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "- " + ChatColor.YELLOW + stack.getAmount() + "x " + ChatColor.LIGHT_PURPLE + StringUtils.capitalize(stack.getType().getKey().asString().replaceAll("minecraft:", "").toLowerCase().replaceAll("_", " ")) + " " + getEnchantmentsString(stack.getEnchantments()));
        }*/
        sender.sendMessage(ChatColor.GOLD + "----------------------------------");
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
