package wtf.melonthedev.log4minecraft.services;

import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import wtf.melonthedev.log4minecraft.InventoryBackup;
import wtf.melonthedev.log4minecraft.Main;
import wtf.melonthedev.log4minecraft.utils.ItemSerializer;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class InventoryBackupService {
    public static void backupInventory(HumanEntity player) {
        PlayerInventory inv = player.getInventory();
        JsonFile invBackupFile = Main.invBackupFile;
        JSONObject object = invBackupFile.get();
        if (!object.containsKey("invbackups"))
            object.put("invbackups", new JSONArray());
        JSONArray array = (JSONArray) object.get("invbackups");
        JSONObject inventory = new JSONObject();
        inventory.put("contents", ItemSerializer.itemsToString(inv.getContents()));
        inventory.put("created", Instant.now().getEpochSecond());
        inventory.put("owner", player.getUniqueId().toString());
        inventory.put("id", getInventoryBackups(player.getUniqueId()).size() + 1);
        array.add(inventory);
        object.put("invbackups", array);
        invBackupFile.write(object);
    }

    public static InventoryBackup getInventoryBackup(UUID uuid, int backupId) {
        return getInventoryBackups(uuid).stream().filter(inventoryBackup -> inventoryBackup.getBackupId() == backupId).findFirst().orElse(null);
    }

    public static List<InventoryBackup> getInventoryBackups(UUID uuid) {
        return getInventoryBackups().stream().filter(inventoryBackup -> Objects.equals(inventoryBackup.getOwner().toString(), uuid.toString())).toList();
    }

    public static List<InventoryBackup> getInventoryBackups() {
        List<InventoryBackup> backups = new ArrayList<>();
        JsonFile invBackupFile = Main.invBackupFile;
        JSONObject object = invBackupFile.get();
        if (!object.containsKey("invbackups"))
            object.put("invbackups", new JSONArray());
        JSONArray array = (JSONArray) object.get("invbackups");
        for (Object o : array) {
            if (o == null) continue;
            JSONObject rawBackup = (JSONObject) o;
            UUID uuid = UUID.fromString((String) rawBackup.get("owner"));
            long created = (long) rawBackup.get("created");
            int id = rawBackup.get("id") instanceof Long ? (int)(long) rawBackup.get("id") : (int) rawBackup.get("id");
            ItemStack[] items = ItemSerializer.stringToItems((String) rawBackup.get("contents"));
            InventoryBackup backup = new InventoryBackup(id, items, uuid, created);
            backups.add(backup);
        }
        return backups;
    }

}
