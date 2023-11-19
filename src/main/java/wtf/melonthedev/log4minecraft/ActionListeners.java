package wtf.melonthedev.log4minecraft;

import org.bukkit.block.Container;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import wtf.melonthedev.log4minecraft.enums.Action;
import wtf.melonthedev.log4minecraft.services.InventoryBackupService;
import wtf.melonthedev.log4minecraft.services.MinecraftLogger;
import wtf.melonthedev.log4minecraft.services.PlayerActivityService;

public class ActionListeners implements Listener {

    @EventHandler
    public void onContainerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null
                || event.getAction() == org.bukkit.event.block.Action.LEFT_CLICK_BLOCK
                || event.getHand() == EquipmentSlot.OFF_HAND
                || (!(event.getClickedBlock().getState() instanceof Container) && !Main.logNonContainerBlockRightClicks)) return;
        MinecraftLogger.log(new LogEntry(new LogTarget(event.getPlayer()), Action.INTERACT, new LogTarget(event.getClickedBlock())));
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        MinecraftLogger.log(new LogEntry(new LogTarget(event.getPlayer()), Action.PLACE, new LogTarget(event.getBlockPlaced())));
    }


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        MinecraftLogger.log(new LogEntry(new LogTarget(event.getPlayer()), Action.BREAK, new LogTarget(event.getBlock())));
        PlayerActivityService.handleBlockBreak(event.getPlayer(), event.getBlock());
    }

    @EventHandler
    public void onInventoryMoveItem(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;
        MinecraftLogger.log(new LogEntry(
                new LogTarget(event.getWhoClicked()),
                Action.GRAB.setDisplayedString(event.getAction().name().toLowerCase().replaceAll("_", "")),
                new LogTarget(event.getCurrentItem())));
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {
        MinecraftLogger.log(new LogEntry(new LogTarget(event.getEntity()), Action.PICKUP, new LogTarget(event.getItem())));
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        MinecraftLogger.log(new LogEntry(new LogTarget(event.getPlayer()), Action.DROP, new LogTarget(event.getItemDrop())));
    }

    @EventHandler
    public void onItemDespawn(ItemDespawnEvent event) {
        MinecraftLogger.log(new LogEntry(new LogTarget(event.getEntity()), Action.DESPAWN));
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        MinecraftLogger.log(new LogEntry(new LogTarget(event.getPlayer()), Action.INTERACT, new LogTarget(event.getRightClicked())));
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LogTarget subject = new LogTarget(event.getEntity().getKiller() == null ? event.getEntity() : event.getEntity().getKiller());
        LogTarget target = event.getEntity().getKiller() == null ? null : new LogTarget(event.getEntity());
        Action action = event.getEntity().getKiller() == null ? Action.DIE : Action.KILL;
        MinecraftLogger.log(new LogEntry(subject, action, target));

        if (!(event.getEntity() instanceof HumanEntity) || !Main.createInvBackupOnDeath) return;
        InventoryBackupService.backupInventory(((HumanEntity) event.getEntity()));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (!Main.createInvBackupOnLeave) return;
        InventoryBackupService.backupInventory(event.getPlayer());
    }
}
