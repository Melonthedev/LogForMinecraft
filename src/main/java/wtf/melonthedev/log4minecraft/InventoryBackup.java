package wtf.melonthedev.log4minecraft;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class InventoryBackup {

    private final int backupId;
    private final ItemStack[] contents;
    private final UUID owner;
    private final long created;


    public InventoryBackup(int backupId, ItemStack[] contents, UUID owner, long created) {
        this.backupId = backupId;
        this.contents = contents;
        this.owner = owner;
        this.created = created;
    }

    public int getBackupId() {
        return backupId;
    }

    public ItemStack[] getContents() {
        return contents;
    }

    public UUID getOwner() {
        return owner;
    }

    public long getCreated() {
        return created;
    }

    public Inventory getPlayerInventory() {
        Inventory inv = Bukkit.createInventory(null, InventoryType.PLAYER);
        inv.setContents(getContents());
        return inv;
    }
}
