package fr.myriapod.milkywayexplorer;

import fr.myriapod.milkywayexplorer.spaceexplorer.spaceship.Ship;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Game {
    private static ArrayList<Ship> shipList = new ArrayList<>();

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
}
