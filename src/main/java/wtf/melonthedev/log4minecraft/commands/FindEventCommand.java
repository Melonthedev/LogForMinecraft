package wtf.melonthedev.log4minecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import wtf.melonthedev.log4minecraft.LoggerUtils;
import wtf.melonthedev.log4minecraft.Main;
import wtf.melonthedev.log4minecraft.enums.Action;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class FindEventCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp()) return true;
        if (args.length < 3) {
            sender.sendMessage(Main.prefix + "Syntaxerror: /findevent <Date: 2023-01-05/latest> <Subject/*> <Action/*> (<Target>)");
            return true;
        }
        try {
            String startingDate = args[0];
            String subject = args[1];
            Action action = args[2].equals("*") ? Action.ALL : Action.valueOf(args[2].toUpperCase());
            String target = args.length >= 4 ? args[3] : "*";
            String owner = args.length >= 5 ? args[4] : "*";

            if (startingDate.equalsIgnoreCase("latest")) {
                startingDate = Calendar.getInstance().get(Calendar.YEAR) +
                        "-" + LoggerUtils.getWithZeros(Calendar.getInstance().get(Calendar.MONTH) + 1) +
                        "-" + LoggerUtils.getWithZeros(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            }

            if (startingDate.split("-").length != 3
                    || startingDate.split("-")[0].length() != 4
                    || startingDate.split("-")[1].length() != 2
                    || startingDate.split("-")[2].length() != 2
                    || !isInt(startingDate.split("-")[0])
                    || !isInt(startingDate.split("-")[1])
                    || !isInt(startingDate.split("-")[2])) {
                sender.sendMessage(Main.prefix + ChatColor.RED + "Invalid date. Please use YYYY-MM-DD e.g. 2023-01-05");
                return true;
            }
            int year = Integer.parseInt(startingDate.split("-")[0]);
            int month = Integer.parseInt(startingDate.split("-")[1]);
            int day = Integer.parseInt(startingDate.split("-")[2]);

            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
            int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

            LocalDate startDate = LocalDate.of(year, month, day);
            LocalDate currentDate = LocalDate.of(currentYear, currentMonth, currentDay);

            sender.sendMessage(ChatColor.AQUA + "Matching Log Entries:");
            for (LocalDate date = startDate; !date.isAfter(currentDate); date = date.plusDays(1)) {
                String fileName = "log4minecraft-" + date.getYear() + "-" + LoggerUtils.getWithZeros(date.getMonthValue()) + "-" + LoggerUtils.getWithZeros(date.getDayOfMonth()) + ".json";
                if (!LoggerUtils.getExistingJsonFile(fileName).exists()) {
                    sender.sendMessage(Main.prefix + ChatColor.RED + "File not found: " + fileName + ", Skipping!");
                    continue;
                }
                JSONObject obj = LoggerUtils.getJsonObjFromFile(fileName);
                JSONArray logs = (JSONArray) obj.get("logs");
                for (Object o : logs){
                    JSONObject log = (JSONObject) o;
                    if (!subject.equalsIgnoreCase("*") && !((String) log.get("subject")).equalsIgnoreCase(subject)) continue;
                    if (action != Action.ALL && !((String) log.get("action")).equalsIgnoreCase(action.name())) continue;
                    if (!target.equalsIgnoreCase("*") && (log.get("target") == null || !((String) log.get("target")).equalsIgnoreCase(target))) continue;
                    if (!owner.equalsIgnoreCase("*") && (log.get("owner") == null || !((String) log.get("owner")).equalsIgnoreCase(owner))) continue;

                    JSONObject loc = (JSONObject) log.get("location");
                    sender.sendMessage(ChatColor.GRAY + Instant.ofEpochSecond((long) log.get("created")).toString() + ChatColor.GOLD
                            + " - " + log.get("subject") + " " + ChatColor.AQUA
                            + log.get("action") + " " + ChatColor.RED
                            + (log.get("target") != null ? log.get("target") : "") + " " + ChatColor.YELLOW
                            + (loc != null ? "X: " + loc.get("x") + " Y: " + loc.get("y") + " Z: " + loc.get("z") + " W: " + loc.get("w") : "")
                            + (log.get("owner") != null ? ChatColor.LIGHT_PURPLE + " Owner: " + log.get("owner") : ""));
                }
            }
        } catch (IllegalArgumentException e) {
            sender.sendMessage(Main.prefix + ChatColor.RED + "Invalid action: " + args[2]);
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
            tab.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).toList());
        } else if (args.length == 3) {
            tab.add("*");
            tab.addAll(Action.names().stream().filter(s -> !s.equalsIgnoreCase("all")).toList());
        } else if (args.length == 4) {
            tab.add("*");
            tab.addAll(Arrays.stream(Material.values()).map(Enum::name).filter(s -> s.startsWith(args[3].toUpperCase())).toList());
            tab.addAll(Arrays.stream(EntityType.values()).map(Enum::name).filter(s -> s.startsWith(args[3].toUpperCase())).toList());
        }
        return tab;
    }

    public boolean isInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
