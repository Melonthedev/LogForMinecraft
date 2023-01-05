package wtf.melonthedev.log4minecraft;

import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

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

    private enum TargetType {
        ENTITY, ITEM, BLOCK, ITEMENTITY
    }
}
