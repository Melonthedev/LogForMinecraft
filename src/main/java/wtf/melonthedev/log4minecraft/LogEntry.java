package wtf.melonthedev.log4minecraft;

import org.bukkit.Location;

public class LogEntry {

    public final String playerName;
    public final Action action;
    public final String type;
    public final Location location;
    public final String owner;
    public final String executor;
    public final int amount;

    public LogEntry(String playerName, Action action, String type, int amount, Location location, String owner, String executor) {
        this.playerName = playerName;
        this.action = action;
        this.type = type;
        this.location = location;
        this.owner = owner;
        this.executor = executor;
        this.amount = amount;
    }

    public LogEntry(String playerName, Action action, String type, Location location, String owner, String executor) {
        this.playerName = playerName;
        this.action = action;
        this.type = type;
        this.location = location;
        this.owner = owner;
        this.executor = executor;
        this.amount = 1;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Action getAction() {
        return action;
    }

    public String getType() {
        return type;
    }

    public Location getLocation() {
        return location;
    }

    public String getOwner() {
        return owner;
    }

    public String getExecutor() {
        return executor;
    }

    public String getLogText() {
        return LoggerUtils.getDateTimeString()
                + getPlayerName() + " "
                + getAction().getString() + " "
                + getType()
                + (getLocation() == null ? "" : " at X: " + getLocation().getX() + " Y: " + getLocation().getY() + " Z: " + getLocation().getZ() + " W: " + (getLocation().getWorld() == null ? "unknown" : getLocation().getWorld().getName()))
                + (getOwner() == null ? "" :  " from " + getOwner())
                + (getExecutor() == null ? "" :  " by " + getExecutor());
    }
}
