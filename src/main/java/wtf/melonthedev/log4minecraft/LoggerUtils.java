package wtf.melonthedev.log4minecraft;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import wtf.melonthedev.log4minecraft.enums.LogLevel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

public class LoggerUtils {


    public static LogLevel getLogLevelByType() {


        return LogLevel.NORMAL; //TODO: Impl
    }

    public static String getLogText(LogEntry entry) {
        return getDateTimeString()
                + entry.getPlayerName() + " "
                + entry.getAction().name().toLowerCase() + " "
                + entry.getType()
                + (entry.getLocation() == null ? "" : " at X: " + entry.getLocation().getX() + " Y: " + entry.getLocation().getY() + " Z: " + entry.getLocation().getZ() + " W: " + (entry.getLocation().getWorld() == null ? "unknown" : entry.getLocation().getWorld().getName()))
                + (entry.getOwner() == null ? "" :  " from " + entry.getOwner())
                + (entry.getExecutor() == null ? "" :  " by " + entry.getExecutor());
    }

    private static String getDateTimeString() {
        Date currentDate = new Date();
        return "[" + DateFormat.getDateInstance().format(currentDate) + " " + (currentDate.getHours() < 10 ? "0" + currentDate.getHours() : currentDate.getHours()) + ":" + (currentDate.getMinutes() < 10 ? "0" + currentDate.getMinutes() : currentDate.getMinutes()) + "] ";
    }

    public static JSONObject getJsonObjFromFile() {
        JSONParser jsonParser = new JSONParser();
        if (!new File(Main.getPlugin().getDataFolder(), "log4minecraft.json").exists()) {
            try {
                new File(Main.getPlugin().getDataFolder(), "log4minecraft.json").createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (FileReader reader = new FileReader(new File(Main.getPlugin().getDataFolder(), "log4minecraft.json")))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONObject jo = (JSONObject) obj;
            System.out.println(jo);
            return jo;
            //Iterate over employee array
            //employeeList.forEach( emp -> parseEmployeeObject( (JSONObject) emp ) );

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
