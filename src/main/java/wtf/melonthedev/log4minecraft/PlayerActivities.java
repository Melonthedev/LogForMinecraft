package wtf.melonthedev.log4minecraft;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import wtf.melonthedev.log4minecraft.enums.OreActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerActivities {

    private final Player player;
    private final List<HashMap<OreActivity, Integer>> oreBreaks = new ArrayList<>();


    public PlayerActivities(Player player) {
        this.player = player;
        oreBreaks.add(new HashMap<>());
    }

    public void handleOreBreak(OreActivity ore) {
        if (!getCurrentOreBreaks().containsKey(ore))
            getCurrentOreBreaks().put(ore, 0);
        getCurrentOreBreaks().put(ore, getCurrentOreBreaks().get(ore) + 1);
    }

    public void updateToNewHalfHour() {
        oreBreaks.add(new HashMap<>());
    }

    public HashMap<OreActivity, Integer> getCurrentOreBreaks() {
        return oreBreaks.get(oreBreaks.size() - 1);
    }

    public String getOreBreaksString() {
        StringBuilder builder = new StringBuilder();
        builder.append(ChatColor.AQUA).append("SusPlayerActivity for Player ").append(player.getName()).append(": \n");
        for (HashMap<OreActivity, Integer> oreBreak : oreBreaks) {
            builder.append(ChatColor.WHITE).append(ChatColor.BOLD.toString()).append("--------------------------------------------\n");
            for (OreActivity ore : oreBreak.keySet()) {
                ChatColor color;
                if (oreBreak.get(ore) < ore.lightSusBlockBreakCount) color = ChatColor.GREEN;
                else if (oreBreak.get(ore) < ore.mediumSusBlockBreakCount) color = ChatColor.YELLOW;
                else color = ChatColor.RED;
                builder.append(ChatColor.BOLD).append("- ").append(ChatColor.LIGHT_PURPLE).append(ore.displayName).append(": ").append(color).append(oreBreak.get(ore)).append("\n");
            }
        }
        return builder.toString();
    }

}
