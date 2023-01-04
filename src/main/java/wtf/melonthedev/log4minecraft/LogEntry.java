package wtf.melonthedev.log4minecraft;

import org.bukkit.Location;

public class LogEntry {

    public final String playerName;
    public final Action action;
    public final String type;
    public final Location location;
    public final String owner;
    public final String executor;

    public LogEntry(String playerName, Action action, String type, Location location, String owner, String executor) {
        this.playerName = playerName;
        this.action = action;
        this.type = type;
        this.location = location;
        this.owner = owner;
        this.executor = executor;
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
}
