package wtf.melonthedev.log4minecraft.enums;

import org.bukkit.Material;

public enum OreActivity {
    COAL("Coal Ore", Material.COAL_ORE, 100, 200),
    COPPER("Copper Ore", Material.COPPER_ORE, 100, 200),
    LAPIS("Lapis Ore", Material.LAPIS_ORE, 20, 40),
    IRON("Iron Ore", Material.IRON_ORE, 40, 50),
    GOLD("Gold Ore", Material.GOLD_ORE, 15, 30),
    REDSTONE("Redstone Ore", Material.REDSTONE_ORE, 50, 80),
    DIAMOND("Diamond Ore", Material.DIAMOND_ORE, 15, 20),
    EMERALD("Emerald Ore", Material.EMERALD_ORE, 50, 80),
    DEEPSLATE_COAL("Deepslate Coal Ore", Material.DEEPSLATE_COAL_ORE, 100, 200),
    DEEPSLATE_COPPER("Deepslate Copper Ore", Material.DEEPSLATE_COPPER_ORE, 100, 200),
    DEEPSLATE_LAPIS("Deepslate Lapis Ore", Material.DEEPSLATE_LAPIS_ORE, 20, 40),
    DEEPSLATE_IRON("Deepslate Iron Ore", Material.DEEPSLATE_IRON_ORE, 40, 50),
    DEEPSLATE_GOLD("Deepslate Gold Ore", Material.DEEPSLATE_GOLD_ORE, 15, 30),
    DEEPSLATE_REDSTONE("Deepslate Redstone Ore", Material.DEEPSLATE_REDSTONE_ORE, 50, 80),
    DEEPSLATE_DIAMOND("Deepslate Diamond Ore", Material.DEEPSLATE_DIAMOND_ORE, 15, 20),
    DEEPSLATE_EMERALD("Deepslate Emerald Ore", Material.DEEPSLATE_EMERALD_ORE, 50, 8),
    QUARZ("Quarz Ore", Material.NETHER_QUARTZ_ORE, 150, 200),
    NETHER_GOLD("Nether Gold Ore", Material.NETHER_GOLD_ORE, 40, 60),
    ANCIENT_DEBRIS("Ancient Debris", Material.ANCIENT_DEBRIS, 20, 30);


    public final String displayName;
    public final Material material;
    public final int lightSusBlockBreakCount;
    public final int mediumSusBlockBreakCount;


    OreActivity(String displayName, Material material, int lightSusBlockBreakCount, int mediumSusBlockBreakCount) {
        this.displayName = displayName;
        this.material = material;
        this.lightSusBlockBreakCount = lightSusBlockBreakCount;
        this.mediumSusBlockBreakCount = mediumSusBlockBreakCount;
    }
}
