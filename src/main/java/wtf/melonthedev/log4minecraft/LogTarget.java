package wtf.melonthedev.log4minecraft;

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import wtf.melonthedev.log4minecraft.utils.LoggerUtils;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;

public class LogTarget {
    private final TargetType activeType;
    private Entity entity;
    private ItemStack item;
    private Item itemEntity;
    private Block block;
    private String text;
    private Location location;
    private OfflinePlayer owner;

    public LogTarget(Entity entity) {
        this.entity = entity;
        this.activeType = TargetType.ENTITY;
        this.owner = getOwner(entity);
    }

    public LogTarget(ItemStack item) {
        this.item = item;
        this.activeType = TargetType.ITEM;
        this.owner = getOwner(item);
    }

    public LogTarget(Block block) {
        this.block = block;
        this.activeType = TargetType.BLOCK;
        this.owner = getOwner(block);
    }

    public LogTarget(Item itemEntity) {
        this.itemEntity = itemEntity;
        this.activeType = TargetType.ITEMENTITY;
        this.owner = getItemOwner(itemEntity);
    }

    public LogTarget(String text) {
        this.activeType = TargetType.TEXT;
        this.text = text;
    }

    public LogTarget(Location location) {
        this.activeType = TargetType.TEXT;
        this.location = location;
    }

    public static boolean hasPersistentDataContainer(Object obj) {
        return obj instanceof PersistentDataHolder;
    }


    public static OfflinePlayer getItemOwner(Item itemEntity) {
        return itemEntity.getOwner() == null
                ? (itemEntity.getThrower() == null ? null
                : Bukkit.getOfflinePlayer(itemEntity.getThrower()))
                : Bukkit.getOfflinePlayer(itemEntity.getOwner());
    }
    public static OfflinePlayer getOwner(Object obj) {
        if (!hasPersistentDataContainer(obj)) return null;
        PersistentDataContainer container = ((PersistentDataHolder) obj).getPersistentDataContainer();
        return getOwner(container);
    }

    public static OfflinePlayer getOwner(ItemStack stack) {
        if (stack == null || !stack.hasItemMeta()) return null;
        return getOwner(stack.getItemMeta().getPersistentDataContainer());
    }

    public static OfflinePlayer getOwner(PersistentDataContainer container) {
        if (!container.has(new NamespacedKey(Main.getPlugin(), "owner"))) return null;
        String owner = container.getOrDefault(new NamespacedKey(Main.getPlugin(), "owner"), PersistentDataType.STRING, "");
        try {
            UUID uuid = UUID.fromString(owner);
            return Bukkit.getOfflinePlayer(uuid);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }


    public TargetType getActiveType() {
        return activeType;
    }

    public Entity getEntity() {
        return entity;
    }

    public ItemStack getItem() {
        return item;
    }

    public Item getItemEntity() {
        return itemEntity;
    }

    public Block getBlock() {
        return block;
    }

    public OfflinePlayer getOwner() {
        return owner;
    }

    public String getKey() {
        switch (activeType) {
            case ITEM -> {
                return item.getType().name();
            }
            case BLOCK -> {
                return block.getType().name();
            }
            case ENTITY -> {
                return entity.getType().name();
            }
            case ITEMENTITY -> {
                return itemEntity.getItemStack().getType().name();
            }
            default -> {
                return "";
            }
        }
    }

    public String getLabel() {
        switch (activeType) {
            case ITEM -> {
                return item.getType().name() + (item != null && item.hasItemMeta() && !LoggerUtils.isComponentEmpty(item.getItemMeta().displayName()) ? " [" + PlainTextComponentSerializer.plainText().serialize(Objects.requireNonNull(item.getItemMeta().displayName())) + "]" : "");
            }
            case BLOCK -> {
                return block.getType().name();
            }
            case ENTITY -> {
                return entity instanceof HumanEntity player ? player.getName() : (entity.getType().name() + (!LoggerUtils.isComponentEmpty(entity.customName()) ? " [" + PlainTextComponentSerializer.plainText().serialize(Objects.requireNonNull(entity.customName())) + "]" : ""));
            }
            case ITEMENTITY -> {
                return itemEntity.getItemStack().getType().name() + (item != null && item.hasItemMeta() && !LoggerUtils.isComponentEmpty(itemEntity.getItemStack().getItemMeta().displayName()) ? " [" + PlainTextComponentSerializer.plainText().serialize(Objects.requireNonNull(itemEntity.getItemStack().getItemMeta().displayName())) + "]" : "");
            }
            case TEXT ->  {
                return text;
            }
            case LOCATION -> {
                return location.toString();
            }
            default -> {
                return "";
            }
        }
    }

    public @Nullable Location tryGetLocation() {
        switch (activeType) {
            case BLOCK -> {
                return block.getLocation();
            }
            case ENTITY -> {
                return entity.getLocation();
            }
            case ITEMENTITY -> {
                return itemEntity.getLocation();
            }
            case LOCATION -> {
                return location;
            }
            default -> {
                return null;
            }
        }
    }

    public enum TargetType {
        ENTITY, ITEM, BLOCK, ITEMENTITY, TEXT, LOCATION
    }
}
