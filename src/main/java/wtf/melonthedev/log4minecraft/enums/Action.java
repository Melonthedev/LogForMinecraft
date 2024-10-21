package wtf.melonthedev.log4minecraft.enums;

import java.util.ArrayList;
import java.util.List;

public enum Action {
    INTERACT("interacts with"),
    BREAK("breaks"),
    PLACE("places"),
    PICKUP("picks up"),
    DROP("drops"),
    GRAB("grabs"),
    DIE("dies"),
    KILL("kills"),
    DESPAWN("despawnes"),
    BAN("is banned"),
    UNBAN("is unanned"),
    KICK("is kicked because of"),
    JOIN("joins"),
    LEAVE("leaves"),
    EXECUTE_COMMAND("executes command"),
    COMPLETES_ADVANCEMENT("completes advancement"),
    CHANGES_WORLD("changes world to"),


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
