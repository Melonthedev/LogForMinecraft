package wtf.melonthedev.log4minecraft.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import wtf.melonthedev.log4minecraft.InventoryBackup;
import wtf.melonthedev.log4minecraft.utils.ConfigurationSerializableAdapter;
import wtf.melonthedev.log4minecraft.utils.ItemSerializer;
import wtf.melonthedev.log4minecraft.utils.LoggerUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class InventoryBackupService {
    public static void backupInventory(HumanEntity player) {
        PlayerInventory inv = player.getInventory();
        JSONObject object = LoggerUtils.getJsonObjFromInvBackupFile();
        if (!object.containsKey("invbackups"))
            object.put("invbackups", new JSONArray());
        JSONArray array = (JSONArray) object.get("invbackups");
        JSONObject inventory = new JSONObject();
        inventory.put("contents", ItemSerializer.itemsToString(inv.getContents()));
        inventory.put("created", Instant.now().getEpochSecond());
        inventory.put("owner", player.getUniqueId().toString());
        inventory.put("id", (int)((int) getInventoryBackups(player.getUniqueId()).size() + 1));
        array.add(inventory);
        object.put("invbackups", array);
        try (FileWriter file = new FileWriter(LoggerUtils.getInvBackupJsonFile())) {
            file.write(object.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static InventoryBackup getInventoryBackup(UUID uuid, int backupId) {
        return getInventoryBackups(uuid).stream().filter(inventoryBackup -> inventoryBackup.getBackupId() == backupId).findFirst().orElse(null);
    }

    public static List<InventoryBackup> getInventoryBackups(UUID uuid) {
        return getInventoryBackups().stream().filter(inventoryBackup -> Objects.equals(inventoryBackup.getOwner().toString(), uuid.toString())).toList();
    }

    public static List<InventoryBackup> getInventoryBackups() {
        List<InventoryBackup> backups = new ArrayList<>();
        JSONObject object = LoggerUtils.getJsonObjFromInvBackupFile();
        if (!object.containsKey("invbackups"))
            object.put("invbackups", new JSONArray());
        JSONArray array = (JSONArray) object.get("invbackups");
        for (Object o : array) {
            JSONObject inventory = (JSONObject) o;
            UUID uuid = UUID.fromString((String) inventory.get("owner"));
            long created = (long) inventory.get("created");
            int id = (int)((long) inventory.get("id"));
            ItemStack[] items = ItemSerializer.stringToItems((String) inventory.get("contents"));
            InventoryBackup backup = new InventoryBackup(id, items, uuid, created);
            backups.add(backup);
        }
        return backups;
    }

}
