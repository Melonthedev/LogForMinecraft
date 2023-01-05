package wtf.melonthedev.log4minecraft.enums;

import java.util.ArrayList;
import java.util.List;

public enum LogOutput {

    CONSOLE, TEXTFILE, FILE;

    public static List<String> names() {
        List<String> names = new ArrayList<>();
        for (LogOutput output : LogOutput.values()) {
            names.add(output.name());
        }
        return names;
    }

}
