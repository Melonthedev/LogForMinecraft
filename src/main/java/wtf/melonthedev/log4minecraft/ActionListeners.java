package wtf.melonthedev.log4minecraft;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import wtf.melonthedev.log4minecraft.enums.Action;

public class ActionListeners implements Listener {

    @EventHandler
    public void onContainerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;
        MinecraftLogger.log(new LogEntry(event.getPlayer(),
                Action.INTERACT,
                new LogTarget(event.getClickedBlock()),
                event.getClickedBlock().getLocation(),
                null,
                null));
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        MinecraftLogger.log(new LogEntry(event.getPlayer(),
                Action.PLACE,
                new LogTarget(event.getBlockPlaced()),
                event.getBlockPlaced().getLocation(),
                null,
                null));
    }


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        MinecraftLogger.log(new LogEntry(event.getPlayer(),
                Action.BREAK,
                new LogTarget(event.getBlock()),
                event.getBlock().getLocation(),
                null,
                null));
    }

    @EventHandler
    public void onInventoryMoveItem(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;
        MinecraftLogger.log(new LogEntry(event.getWhoClicked(),
                Action.GRAB,
                new LogTarget(event.getCurrentItem()),
                event.getWhoClicked().getLocation(),
                null,
                event.getAction().name()));
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {
        String owner = event.getItem().getOwner() == null ? (event.getItem().getThrower() == null ? "" : event.getItem().getThrower().toString()) : event.getItem().getOwner().toString();
        MinecraftLogger.log(new LogEntry(event.getEntity(),
                Action.PICKUP,
                new LogTarget(event.getItem()),
                event.getItem().getLocation(),
                owner,
                null));
    }

    @EventHandler
    public void onItemDrop(EntityDropItemEvent event) {
        MinecraftLogger.log(new LogEntry(event.getEntity(), Action.DROP, new LogTarget(event.getItemDrop()), event.getItemDrop().getLocation(), null, null));
    }

    @EventHandler
    public void onItemDespawn(ItemDespawnEvent event) {
        String owner = event.getEntity().getOwner() == null ? (event.getEntity().getThrower() == null ? "" : event.getEntity().getThrower().toString()) : event.getEntity().getOwner().toString();
        MinecraftLogger.log(new LogEntry(event.getEntity(), Action.DESPAWN, new LogTarget(event.getEntity()), event.getLocation(), owner, null));
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        MinecraftLogger.log(new LogEntry(event.getPlayer(), Action.INTERACT, new LogTarget(event.getRightClicked()), event.getRightClicked().getLocation(), null, null));
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        MinecraftLogger.log(new LogEntry(event.getEntity(), Action.DIE, new LogTarget(event.getEntity()), event.getEntity().getLocation(), null, null));
    }
}
