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
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ActionListeners implements Listener {

    @EventHandler
    public void onContainerInteract(PlayerInteractEvent event) {
        /*if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.hasBlock() && event.getClickedBlock() instanceof Container && event.getClickedBlock() instanceof PersistentDataHolder) {
            String owner = getOwner(event.getClickedBlock());
            logAction(event.getPlayer().getName(), "opened", event.getClickedBlock().getLocation(), owner, event.getClickedBlock().getType().toString(), true);
        }*/
        if (event.getClickedBlock() == null) return;
        MinecraftLogger.log(new LogEntry(event.getPlayer().getName(), Action.INTERACT, event.getClickedBlock().getType().name(), event.getClickedBlock().getLocation(), null, null));
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        /*if (event.getBlockPlaced().getState() instanceof Container && event.getBlockPlaced().getState() instanceof PersistentDataHolder) {
            PersistentDataContainer container = ((Container) event.getBlockPlaced().getState()).getPersistentDataContainer();
            container.set(new NamespacedKey(Main.getPlugin(), "owner"), PersistentDataType.STRING, event.getPlayer().getName());
            event.getBlockPlaced().getState().update();
        }
        blockLog.put(event.getBlockPlaced().getLocation(), event.getPlayer().getName());
        logAction(event.getPlayer().getName(), "placed", event.getBlockPlaced().getLocation(), event.getPlayer().getName(), event.getBlockPlaced().getType().toString(), isValuable(event.getBlockPlaced().getType()));
    */
        MinecraftLogger.log(new LogEntry(event.getPlayer().getName(), Action.PLACE, event.getBlockPlaced().getType().name(), event.getBlockPlaced().getLocation(), null, null));
    }


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        /*String owner = getOwner(event.getBlock());
        logAction(event.getPlayer().getName(), "broke", event.getBlock().getLocation(), owner, event.getBlock().getType().toString(), isValuable(event.getBlock().getType()));*/
        MinecraftLogger.log(new LogEntry(event.getPlayer().getName(), Action.BREAK, event.getBlock().getType().name(), event.getBlock().getLocation(), null, null));
    }

    @EventHandler
    public void onInventoryMoveItem(InventoryClickEvent event) {
        /*if (!(event.getWhoClicked() instanceof Player) || event.getClickedInventory() == null || event.getClickedInventory() instanceof PlayerInventory || event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR)
            return;
        String owner = "unknown";
        if (event.getClickedInventory().getLocation() != null && event.getClickedInventory().getLocation().getBlock() != null  && event.getClickedInventory().getLocation().getBlock().getType() != null) {
            owner = getOwner(event.getClickedInventory().getLocation().getBlock());
        }
        logAction(event.getWhoClicked().getName(), "grabbed " + event.getCurrentItem().getAmount(), Objects.requireNonNull(event.getClickedInventory().getLocation()), owner, event.getCurrentItem().getType().toString(), isValuable(event.getCurrentItem().getType()));
    */
        if (event.getCurrentItem() == null) return;
        MinecraftLogger.log(new LogEntry(event.getWhoClicked().getName(), Action.GRAB, event.getCurrentItem().getType().name(), event.getWhoClicked().getLocation(), null, event.getAction().name()));
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {
        String owner = event.getItem().getOwner() == null ? (event.getItem().getThrower() == null ? "" : event.getItem().getThrower().toString()) : event.getItem().getOwner().toString();
        MinecraftLogger.log(new LogEntry(event.getEntity().getName(), Action.PICKUP, event.getItem().getType().name(), event.getItem().getItemStack().getAmount(), event.getItem().getLocation(), owner, null));
    }

    @EventHandler
    public void onItemDrop(EntityDropItemEvent event) {
        MinecraftLogger.log(new LogEntry(event.getEntity().getName(), Action.DROP, event.getItemDrop().getType().name(), event.getItemDrop().getItemStack().getAmount(), event.getItemDrop().getLocation(), null, null));
    }

    @EventHandler
    public void onItemDespawn(ItemDespawnEvent event) {
        String owner = event.getEntity().getOwner() == null ? (event.getEntity().getThrower() == null ? "" : event.getEntity().getThrower().toString()) : event.getEntity().getOwner().toString();
        MinecraftLogger.log(new LogEntry(event.getEntity().getItemStack().getType().name(), Action.DESPAWN, event.getEntity().getItemStack().getType().name(), event.getLocation(), owner, null));
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        MinecraftLogger.log(new LogEntry(event.getPlayer().getName(), Action.INTERACT, event.getRightClicked().getType().name(), event.getRightClicked().getLocation(), null, null));
        //if (isValuable(event.getRightClicked().getType())) logAction(event.getPlayer().getName(), "interacted with", event.getRightClicked().getLocation(), "unknown", event.getRightClicked().getType().toString(), isValuable(event.getRightClicked().getType()));
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        MinecraftLogger.log(new LogEntry(event.getEntity().getUniqueId().toString(), Action.DIE, event.getEntity().getType().name(), event.getEntity().getLocation(), null, null));
        /*if (event.getEntity().getType() == EntityType.PLAYER || event.getEntity().getType() == EntityType.VILLAGER)
            return;
        logAction(event.getEntity().getName(), "died", event.getEntity().getLocation(), event.getEntity().getKiller() == null ? "unknown" : event.getEntity().getKiller().toString(), "", isValuable(event.getEntity().getType()), null, isValuable(event.getEntity().getType()));*/
    }
}
