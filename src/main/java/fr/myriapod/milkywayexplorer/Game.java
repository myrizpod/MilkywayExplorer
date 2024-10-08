package fr.myriapod.milkywayexplorer;

import fr.myriapod.milkywayexplorer.spaceexplorer.spaceship.Ship;
import fr.myriapod.milkywayexplorer.techtree.Tech;
import fr.myriapod.milkywayexplorer.techtree.Techtree;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;

public class Game {
    private static List<Ship> shipList;
    private static Techtree techtree;


    public Game() {
        shipList = new ArrayList<>();
        techtree = new Techtree();
    }

    public static void addShip(Ship ship) {
        shipList.add(ship);
    }


    public static Ship getShipByPlayer(Player player) {
        for(Ship s : shipList) {
            if(s.getPlayer().getUniqueId().equals(player.getUniqueId())) {
                return s;
            }
        }
        return null;
    }

    public static void unlockTech(Tech tech) {
        if(techtree.hasTech(tech)) {
            return;
        }
        techtree.unlockTech(tech);
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(ChatColor.GREEN + "Vous avez debeloqué la tech " + ChatColor.GOLD + tech.getName()));
    }

}
