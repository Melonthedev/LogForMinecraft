package wtf.melonthedev.log4minecraft.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import wtf.melonthedev.log4minecraft.LogTarget;
import wtf.melonthedev.log4minecraft.enums.LogLevel;
import wtf.melonthedev.log4minecraft.services.JsonFile;
import wtf.melonthedev.log4minecraft.utils.LoggerUtils;
import wtf.melonthedev.log4minecraft.Main;
import wtf.melonthedev.log4minecraft.enums.Action;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class FindEventCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp()) return true;
        if (args.length < 1) {
            sender.sendMessage(Main.prefix + "Syntaxerror: /findevent <Date: 2023-01-05/latest> <Subject/*> <Action/*> <Target/*> <Owner/*> <X/* Y/* Z/*> <World/*>");
            return true;
        }
        try {
            String startingDate = args[0];
            String subject = args.length >= 2 ? args[1] : "*";
            Action action = args.length >= 3 ? args[2].equals("*") ? Action.ALL : Action.valueOf(args[2].toUpperCase()) : Action.ALL;
            String target = args.length >= 4 ? args[3] : "*";
            String owner = args.length >= 5 ? args[4] : "*";
            String x = args.length >= 8 ? args[5] : "*";
            String y = args.length >= 8 ? args[6] : "*";
            String z = args.length >= 8 ? args[7] : "*";
            String world = args.length >= 9 ? args[8] : "*";

            if (startingDate.equalsIgnoreCase("latest")) {
                startingDate = Calendar.getInstance().get(Calendar.YEAR) +
                        "-" + LoggerUtils.getWithZeros(Calendar.getInstance().get(Calendar.MONTH) + 1) +
                        "-" + LoggerUtils.getWithZeros(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            }

            if (startingDate.split("-").length != 3
                    || startingDate.split("-")[0].length() != 4
                    || startingDate.split("-")[1].length() != 2
                    || startingDate.split("-")[2].length() != 2
                    || !LoggerUtils.isInt(startingDate.split("-")[0])
                    || !LoggerUtils.isInt(startingDate.split("-")[1])
                    || !LoggerUtils.isInt(startingDate.split("-")[2])) {
                sender.sendMessage(Main.prefix + ChatColor.RED + "Invalid date. Please use YYYY-MM-DD e.g. 2023-01-05");
                return true;
            }

            if ((!LoggerUtils.isInt(x) && !x.equalsIgnoreCase("*"))
                    || (!LoggerUtils.isInt(y) && !y.equalsIgnoreCase("*"))
                    || (!LoggerUtils.isInt(z) && !z.equalsIgnoreCase("*"))) {
                sender.sendMessage(Main.prefix + ChatColor.RED + "Invalid location. Please use integers or '*' as wildcard. E.g: 115 32 -27");
                return true;
            }

            int year = Integer.parseInt(startingDate.split("-")[0]);
            int month = Integer.parseInt(startingDate.split("-")[1]);
            int day = Integer.parseInt(startingDate.split("-")[2]);

            /*int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
            int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);*/

            LocalDate startDate = LocalDate.of(year, month, day);
            LocalDate currentDate = LocalDate.now();//LocalDate.of(currentYear, currentMonth, currentDay);

            sender.sendMessage(ChatColor.AQUA + "Matching Log Entries:");
            for (LocalDate date = startDate; !date.isAfter(currentDate); date = date.plusDays(1)) {
                String fileName = "log4minecraft-" + date.getYear() + "-" + LoggerUtils.getWithZeros(date.getMonthValue()) + "-" + LoggerUtils.getWithZeros(date.getDayOfMonth()) + ".json";
                if (!LoggerUtils.getExistingJsonFile(fileName).exists()) {
                    sender.sendMessage(Main.prefix + ChatColor.RED + "File not found: " + fileName + ", Skipping!");
                    continue;
                }
                JSONObject obj = (Main.logFile.getDateOfCreation().equals(date)) ? Main.logFile.get() : new JsonFile(fileName, "logs", false).get();
                if (obj == null) {
                    sender.sendMessage(Main.prefix + ChatColor.RED + "Error reading currently cached logfile: " + fileName + ", Skipping!");
                    continue;
                }
                JSONArray logs = (JSONArray) obj.get("logs");
                for (Object o : logs){
                    JSONObject log = (JSONObject) o;
                    if (!subject.equalsIgnoreCase("*") && !((String) log.get("subject")).equalsIgnoreCase(subject)) continue;
                    if (action != Action.ALL && !((String) log.get("action")).equalsIgnoreCase(action.name())) continue;
                    if (!target.equalsIgnoreCase("*") && (log.get("target") == null || !((String) log.get("target")).equalsIgnoreCase(target))) continue;
                    if (!owner.equalsIgnoreCase("*") && (log.get("owner") == null || !((String) log.get("owner")).equalsIgnoreCase(owner))) continue;
                    JSONObject loc = (JSONObject) log.get("location");
                    if (!x.equalsIgnoreCase("*") && (loc.get("x") == null || !(String.valueOf(loc.get("x")).equalsIgnoreCase(x)))) continue;
                    if (!y.equalsIgnoreCase("*") && (loc.get("y") == null || !(String.valueOf(loc.get("y")).equalsIgnoreCase(y)))) continue;
                    if (!z.equalsIgnoreCase("*") && (loc.get("z") == null || !(String.valueOf(loc.get("z")).equalsIgnoreCase(z)))) continue;
                    if (!world.equalsIgnoreCase("*") && (loc.get("w") == null || !((String) loc.get("w")).equalsIgnoreCase(world))) continue;

                    String logTarget = String.valueOf(log.get("target"));
                    if (log.get("action") == Action.EXECUTE_COMMAND.name() && log.get("target") != null
                            && (logTarget.contains("findevent") || logTarget.contains("searchlog") || logTarget.contains("findlog"))
                            && !Main.includeFindLogCommandsInFindLogCommandOutput) {
                        continue; // Skip if its a findlog command
                    }

                    /*if (!loglevel.equalsIgnoreCase("*") && LoggerUtils.isValidLogLevel(loglevel)
                            && (log.get("target") == null || LoggerUtils.isValidForLogLevel((String) log.get("target"), LogLevel.valueOf(loglevel)))
                            && LoggerUtils.isValidForLogLevel((String) log.get("subject"), LogLevel.valueOf(loglevel))) continue;*/

                    /*sender.sendMessage(ChatColor.GRAY + Instant.ofEpochSecond((long) log.get("created")).toString() + ChatColor.GOLD
                            + " - " + log.get("subject") + " " + ChatColor.AQUA
                            + Action.valueOf(log.get("action").toString()).getDisplayedString()  + " " + ChatColor.RED
                            + (log.get("target") != null ? log.get("target") + " " : "") + ChatColor.YELLOW
                            + (loc != null ? "at X: " + loc.get("x") + " Y: " + loc.get("y") + " Z: " + loc.get("z") + " W: " + loc.get("w") : "")
                            + (log.get("owner") != null ? ChatColor.LIGHT_PURPLE + " Owner: " + log.get("owner") : ""));*/
                    TextComponent message = Component.text()
                            .append(Component.text(Instant.ofEpochSecond((long) log.get("created")).toString()).color(NamedTextColor.GRAY))
                            .append(Component.text(" - " + log.get("subject") + " ").color(NamedTextColor.GOLD).hoverEvent(Component.text(log.get("subjectUUID") == null ? "No UUID found" : log.get("subjectUUID").toString())).clickEvent(ClickEvent.runCommand("/log4minecraft:manageinventory " + log.get("subject") + " listbackups")))
                            .append(Component.text(Action.valueOf(log.get("action").toString()).getDisplayedString()  + " ").color(NamedTextColor.AQUA))
                            .append(Component.text((log.get("target") != null ? log.get("target") + " " : "")).color(NamedTextColor.RED))
                            .append(Component.text((loc != null ? "at X: " + loc.get("x") + " Y: " + loc.get("y") + " Z: " + loc.get("z") + " W: " + loc.get("w") : "")).color(NamedTextColor.YELLOW))
                            .append(Component.text((log.get("owner") != null ? " Owner: " + log.get("owner") : ""))
                                    .color(NamedTextColor.LIGHT_PURPLE)
                                    .hoverEvent(Component.text(log.get("ownerUUID") == null ? "No UUID found" : log.get("ownerUUID").toString())))
                            .build();
                    sender.sendMessage(message);
                }
            }
        } catch (IllegalArgumentException e) {
            sender.sendMessage(Main.prefix + ChatColor.RED + "Invalid input! Check if the action is valid!");
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> tab = new ArrayList<>();
        if (args.length == 1) {
            tab.add("latest");
            tab.add(Calendar.getInstance().get(Calendar.YEAR) +
                    "-" + LoggerUtils.getWithZeros(Calendar.getInstance().get(Calendar.MONTH) + 1) +
                    "-" + LoggerUtils.getWithZeros(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)));
        } else if (args.length == 2) {
            tab.add("*");
            tab.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(s -> s.toLowerCase().startsWith(args[1].toLowerCase())).toList());
            tab.addAll(Arrays.stream(EntityType.values()).map(Enum::name).filter(s -> s.toLowerCase().startsWith(args[1].toLowerCase())).toList());
        } else if (args.length == 3) {
            tab.add("*");
            tab.addAll(Action.names().stream().filter(s -> !s.equalsIgnoreCase("all")).filter(s -> s.startsWith(args[2].toUpperCase())).toList());
        } else if (args.length == 4) {
            tab.add("*");
            tab.addAll(Arrays.stream(Material.values()).map(Enum::name).filter(s -> s.startsWith(args[3].toUpperCase())).toList());
            tab.addAll(Arrays.stream(EntityType.values()).map(Enum::name).filter(s -> s.startsWith(args[3].toUpperCase())).toList());
        } else if (args.length == 5) {
            tab.add("*");
            tab.addAll(Arrays.stream(Bukkit.getOfflinePlayers()).map(OfflinePlayer::getName).toList());
        } else if (args.length == 9) {
            tab.add("*");
            tab.addAll(Bukkit.getWorlds().stream().map(World::getName).toList());
        } else if (sender instanceof Player player) {
            Block targetBlock = player.getTargetBlockExact(5);
            if (targetBlock == null) return tab;
            if (args.length == 6) {
                tab.add(String.valueOf(targetBlock.getX()));
                tab.add(targetBlock.getX() + " " + targetBlock.getY());
                tab.add(targetBlock.getX() + " " + targetBlock.getY() + " " + targetBlock.getZ());
            }
            if (args.length == 7) {
                tab.add(String.valueOf(targetBlock.getY()));
                tab.add(targetBlock.getY() + " " + targetBlock.getZ());
            }
            if (args.length == 8) {
                tab.add(String.valueOf(targetBlock.getZ()));
            }
        }
        return tab;
    }
}
