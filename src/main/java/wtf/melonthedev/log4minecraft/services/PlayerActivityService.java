package wtf.melonthedev.log4minecraft.services;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import wtf.melonthedev.log4minecraft.Main;
import wtf.melonthedev.log4minecraft.PlayerActivities;
import wtf.melonthedev.log4minecraft.enums.OreActivity;

import java.util.HashMap;
import java.util.UUID;

public class PlayerActivityService {

    public static HashMap<UUID, PlayerActivities> playerActivities = new HashMap<>();


    public static void handlePlayerActivitySystem() {
        new BukkitRunnable() {
            @Override
            public void run() {
                playerActivities.forEach((uuid, playerActivities1) -> playerActivities1.updateToNewHalfHour());
            }
        }.runTaskTimer(Main.getPlugin(), 0, 36000); // 30min
    }


    public static void handleBlockBreak(Player player, Block block) {
        OreActivity ore;
        switch (block.getType()) {
            case COAL_ORE -> ore = OreActivity.COAL;
            case COPPER_ORE -> ore = OreActivity.COPPER;
            case LAPIS_ORE -> ore = OreActivity.LAPIS;
            case IRON_ORE -> ore = OreActivity.IRON;
            case GOLD_ORE -> ore = OreActivity.GOLD;
            case REDSTONE_ORE -> ore = OreActivity.REDSTONE;
            case DIAMOND_ORE -> ore = OreActivity.DIAMOND;
            case EMERALD_ORE -> ore = OreActivity.EMERALD;
            case NETHER_QUARTZ_ORE -> ore = OreActivity.QUARZ;
            case NETHER_GOLD_ORE -> ore = OreActivity.NETHER_GOLD;
            case ANCIENT_DEBRIS -> ore = OreActivity.ANCIENT_DEBRIS;
            case DEEPSLATE_COAL_ORE -> ore = OreActivity.DEEPSLATE_COAL;
            case DEEPSLATE_COPPER_ORE -> ore = OreActivity.DEEPSLATE_COPPER;
            case DEEPSLATE_LAPIS_ORE -> ore = OreActivity.DEEPSLATE_LAPIS;
            case DEEPSLATE_IRON_ORE -> ore = OreActivity.DEEPSLATE_IRON;
            case DEEPSLATE_GOLD_ORE -> ore = OreActivity.DEEPSLATE_GOLD;
            case DEEPSLATE_REDSTONE_ORE -> ore = OreActivity.DEEPSLATE_REDSTONE;
            case DEEPSLATE_DIAMOND_ORE -> ore = OreActivity.DEEPSLATE_DIAMOND;
            case DEEPSLATE_EMERALD_ORE -> ore = OreActivity.DEEPSLATE_EMERALD;
            default -> ore = null;
        }
        if (ore == null) return;
        handleOreBreak(player, ore);
    }

    public static void handleOreBreak(Player player, OreActivity ore) {
        if (!playerActivities.containsKey(player.getUniqueId())) {
            playerActivities.put(player.getUniqueId(), new PlayerActivities(player));
        }
        PlayerActivities activities = playerActivities.get(player.getUniqueId());
        activities.handleOreBreak(ore);
    }


}
