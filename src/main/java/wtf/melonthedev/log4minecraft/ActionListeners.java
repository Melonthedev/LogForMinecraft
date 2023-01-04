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
    */}


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        /*String owner = getOwner(event.getBlock());
        logAction(event.getPlayer().getName(), "broke", event.getBlock().getLocation(), owner, event.getBlock().getType().toString(), isValuable(event.getBlock().getType()));*/
        MinecraftLogger.log(new LogEntry(event.getPlayer().getName(), Action.BROKE, event.getBlock().getType().name(), event.getBlock().getLocation(), null, null));
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
    */}

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {

    }

    @EventHandler
    public void onItemDrop(EntityDropItemEvent event) {

    }

    @EventHandler
    public void onItemDespawn(ItemDespawnEvent event) {

    }

    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        //if (isValuable(event.getRightClicked().getType())) logAction(event.getPlayer().getName(), "interacted with", event.getRightClicked().getLocation(), "unknown", event.getRightClicked().getType().toString(), isValuable(event.getRightClicked().getType()));
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        /*if (event.getEntity().getType() == EntityType.PLAYER || event.getEntity().getType() == EntityType.VILLAGER)
            return;
        logAction(event.getEntity().getName(), "died", event.getEntity().getLocation(), event.getEntity().getKiller() == null ? "unknown" : event.getEntity().getKiller().toString(), "", isValuable(event.getEntity().getType()), null, isValuable(event.getEntity().getType()));*/
    }
}
