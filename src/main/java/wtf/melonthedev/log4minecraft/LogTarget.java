package wtf.melonthedev.log4minecraft;

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import wtf.melonthedev.log4minecraft.utils.LoggerUtils;

import javax.annotation.Nullable;
import java.util.Objects;

public class LogTarget {
    private final TargetType activeType;
    private Entity entity;
    private ItemStack item;
    private Item itemEntity;
    private Block block;
    private final OfflinePlayer owner;

    public LogTarget(Entity entity) {
        this.entity = entity;
        this.activeType = TargetType.ENTITY;
        this.owner = LoggerUtils.getOwner(entity);
    }

    public LogTarget(ItemStack item) {
        this.item = item;
        this.activeType = TargetType.ITEM;
        this.owner = LoggerUtils.getOwner(item);
    }

    public LogTarget(Block block) {
        this.block = block;
        this.activeType = TargetType.BLOCK;
        this.owner = LoggerUtils.getOwner(block);
    }

    public LogTarget(Item itemEntity) {
        this.itemEntity = itemEntity;
        this.activeType = TargetType.ITEMENTITY;
        this.owner = LoggerUtils.getOwner(itemEntity);
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
            default -> {
                return null;
            }
        }
    }

    public enum TargetType {
        ENTITY, ITEM, BLOCK, ITEMENTITY
    }
}
