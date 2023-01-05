package wtf.melonthedev.log4minecraft.enums;

import java.util.ArrayList;
import java.util.List;

public enum Action {
    INTERACT("interacted"),
    OPEN("opened"),
    BREAK("broke"),
    PLACE("placed"),
    PICKUP("picked up"),
    DROP("dropped"),
    GRAB("grabbed"),
    DIE("died"),
    DESPAWN("despawned"),
    BAN("got banned");

    private final String string;

    Action(String displayedString) {
        string = displayedString;
    }

    public String getString() {
        return string;
    }

    public static List<String> names() {
        List<String> names = new ArrayList<>();
        for (Action action : Action.values()) {
            names.add(action.name());
        }
        return names;
    }
}
