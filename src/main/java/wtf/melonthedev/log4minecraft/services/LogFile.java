package wtf.melonthedev.log4minecraft.services;

import org.json.simple.JSONObject;
import wtf.melonthedev.log4minecraft.utils.LoggerUtils;

import java.time.LocalDate;

public class LogFile extends JsonFile {

    private final LocalDate dateOfCreation;

    public LogFile(LocalDate date, boolean cache) {
        super("log4minecraft-" + date.getYear() +
                "-" + LoggerUtils.getWithZeros(date.getMonthValue()) +
                "-" + LoggerUtils.getWithZeros(date.getDayOfMonth()) + ".json",
                "logs", cache);
        dateOfCreation = date;
    }

    @Override
    public JSONObject get() {
        if (dateOfCreation.getDayOfMonth() != LocalDate.now().getDayOfMonth())
            refreshLogFile();
        return super.get();
    }

    public void refreshLogFile() {
        LocalDate date = LocalDate.now();
        this.setFile("log4minecraft-" + date.getYear() +
                "-" + LoggerUtils.getWithZeros(date.getMonthValue()) +
                "-" + LoggerUtils.getWithZeros(date.getDayOfMonth()) + ".json");
    }

    public LocalDate getDateOfCreation() {
        return dateOfCreation;
    }
}
