package wtf.melonthedev.log4minecraft.enums;

import org.bukkit.event.inventory.InventoryAction;

import java.util.ArrayList;
import java.util.List;

public enum Action {
    INTERACT("interacted with"),
    BREAK("broke"),
    PLACE("placed"),
    PICKUP("picked up"),
    DROP("dropped"),
    GRAB("grabbed"),
    DIE("died"),
    KILL("killed"),
    DESPAWN("despawned"),
    BAN("got banned"),

    ALL("*");

    private String string;

    Action(String displayedString) {
        string = displayedString;
    }

    public String getDisplayedString() {
        return string;
    }

    public Action setDisplayedString(String string) {
        this.string = string;
        return this;
    }

    public static List<String> names() {
        List<String> names = new ArrayList<>();
        for (Action action : Action.values())
            names.add(action.name());
        return names;
    }

}
