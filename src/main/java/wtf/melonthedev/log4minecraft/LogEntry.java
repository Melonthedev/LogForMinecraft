package wtf.melonthedev.log4minecraft;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import wtf.melonthedev.log4minecraft.enums.Action;

public record LogEntry(Entity subject, Action action, LogTarget target, Location location, String owner, String executor) {

    public String getLogText() {
        return LoggerUtils.getDateTimeString()
                + (subject().getName() != null ? subject().getName() : subject().getType().name()) + " "
                + action().getString() + " "
                + target().getKey()
                + (location() == null ? "" : " at X: " + location().getX() + " Y: " + location().getY() + " Z: " + location().getZ() + " W: " + (location().getWorld() == null ? "unknown" : location().getWorld().getName()))
                + (owner() == null ? "" : " from " + owner())
                + (executor() == null ? "" : " by " + executor());
    }
}