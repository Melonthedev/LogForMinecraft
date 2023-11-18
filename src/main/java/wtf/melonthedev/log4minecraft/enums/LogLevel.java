package wtf.melonthedev.log4minecraft.enums;

import java.util.ArrayList;
import java.util.List;

public enum LogLevel {
    DISABLED, VALUABLE, NORMAL, DETAILED, EVERYTHING;

    public static List<String> names() {
        List<String> names = new ArrayList<>();
        for (LogLevel level : LogLevel.values())
            names.add(level.name());
        return names;
    }
}
