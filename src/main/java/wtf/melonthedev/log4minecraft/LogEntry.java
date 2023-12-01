package wtf.melonthedev.log4minecraft;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Container;
import org.bukkit.block.TileState;
import org.bukkit.entity.HumanEntity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import wtf.melonthedev.log4minecraft.enums.Action;
import wtf.melonthedev.log4minecraft.services.InventoryBackupService;
import wtf.melonthedev.log4minecraft.utils.LoggerUtils;

import javax.annotation.Nullable;
import java.util.UUID;

public class LogEntry {
    private final LogTarget subject;
    private final Action action;
    private final LogTarget target;
    private final Location location;
    private OfflinePlayer owner;

    public LogEntry(@NotNull LogTarget subject, Action action, @Nullable LogTarget target) {
        this.subject = subject;
        this.action = action;
        this.target = target;
        this.location = (target != null && target.tryGetLocation() != null) ? target.tryGetLocation() : subject.tryGetLocation();
        this.owner = target == null ? null : target.getOwner();
        this.handleOwner();
        this.handleInvBackup();
    }

    public LogEntry(@NotNull LogTarget subject, Action action) {
        this.subject = subject;
        this.action = action;
        this.target = null;
        this.location = subject.tryGetLocation();
        this.owner = null;
        this.handleOwner();
        this.handleInvBackup();
    }

    public void handleOwner() {
        if (getTarget() != null
                && getTarget().getActiveType() == LogTarget.TargetType.BLOCK
                && getTarget().getBlock().getState() instanceof Container) {
            TileState state = (TileState) getTarget().getBlock().getState();
            NamespacedKey key = new NamespacedKey(Main.getPlugin(), "owner");
            PersistentDataContainer container = state.getPersistentDataContainer();
            switch (action) {
                case PLACE -> container.set(key, PersistentDataType.STRING, getSubject().getEntity().getUniqueId().toString());
                case INTERACT, BREAK -> {
                    if (!container.has(key, PersistentDataType.STRING)) return;
                    try {
                        UUID uuid = UUID.fromString(container.get(key, PersistentDataType.STRING));
                        owner = Bukkit.getOfflinePlayer(uuid);
                    } catch (IllegalArgumentException e) {
                        System.out.println(container.get(key, PersistentDataType.STRING) + " not uuid");
                    }
                }
            }
            state.update();
        }
    }

    public void handleInvBackup() {
        if ((action != Action.DIE || subject.getActiveType() != LogTarget.TargetType.ENTITY || !(subject.getEntity() instanceof HumanEntity))
            && (action != Action.KILL || target.getActiveType() != LogTarget.TargetType.ENTITY || !(target.getEntity() instanceof HumanEntity))) return;
        HumanEntity player = (HumanEntity) (action == Action.DIE ? getSubject().getEntity() : getTarget().getEntity());
        InventoryBackupService.backupInventory(player);
    }

    public String getLogText() {
        return LoggerUtils.getDateTimeString()
                + subject.getLabel() + " "
                + action.getDisplayedString()
                + (target != null ? " " + target.getLabel() : "")
                + (location != null ? " at X: " + (Main.useExactLocations ? location.getX() : location.getBlockX())
                + " Y: " + (Main.useExactLocations ? location.getY() : location.getBlockY())
                + " Z: " + (Main.useExactLocations ? location.getZ() : location.getBlockZ())
                + " W: " + (location.getWorld() == null ? "unknown" : location.getWorld().getName()) : "")
                + (owner != null ? " from " + owner.getName() : "");
    }

    public LogTarget getSubject() {
        return subject;
    }

    public Action getAction() {
        return action;
    }

    public LogTarget getTarget() {
        return target;
    }

    public Location getLocation() {
        return location;
    }

    public OfflinePlayer getOwner() {
        return owner;
    }
}