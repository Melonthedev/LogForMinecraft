package wtf.melonthedev.log4minecraft;

import java.util.ArrayList;
import java.util.List;

public enum Action {

    INTERACTED, OPENED, BROKE, PLACED, PICKEDUP, DROPPED, GRABBED, DIED, WASBANNED;


    public static List<String> names() {
        List<String> names = new ArrayList<>();
        for (Action action : Action.values()) {
            names.add(action.name());
        }
        return names;
    }

}
