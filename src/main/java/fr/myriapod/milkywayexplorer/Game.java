package fr.myriapod.milkywayexplorer;

import fr.myriapod.milkywayexplorer.spaceexplorer.Univers;
import fr.myriapod.milkywayexplorer.spaceexplorer.spaceobjects.StarSystem;
import fr.myriapod.milkywayexplorer.spaceexplorer.spaceship.Ship;
import fr.myriapod.milkywayexplorer.techtree.Tech;
import fr.myriapod.milkywayexplorer.techtree.Techtree;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private static List<Ship> shipList;
    private static Techtree techtree;
    private static Univers univers;



    public Game() {
        shipList = new ArrayList<>();
        techtree = new Techtree();
        univers = new Univers();
        univers.loadAllStarSystems();

        Bukkit.getOnlinePlayers().forEach(player -> {
            player.sendTitle(ChatColor.GOLD + "MilkyWayExplorer", ChatColor.DARK_AQUA + "Bienvenue dans", 10, 50, 10);
            Planet planet = univers.getFirstPlanet();
            planet.teleportPlayerToSurface(player);
            Location shipPos = player.getLocation().add(10, 0, 0);
            planet.getSurfacePlanet().shipLands(new Vector3d(shipPos.getX(), shipPos.getY(), shipPos.getZ()));
        });
        Bukkit.getLogger().info(String.valueOf(univers));
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
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(ChatColor.GREEN + "Vous avez débloqué la tech " + ChatColor.GOLD + tech.getName()));
    }

    public static boolean hasTech(Tech tech) {
        return techtree.hasTech(tech);
    }

    public static void shipEnters(int id, Ship ship) {
        univers.shipEnters(id, ship);

    }

    public static World getUniversWorld() {
        return univers.getWorld();
    }

    //TODO ONLY FOR DEGUB
    public static List<StarSystem> getAllLoadedSystems() {
        return univers.getAllLoadedSystems();
    }

    public static Planet getPlayerPlanet(Player player) {
        return univers.getPlayerPlanet(player);
    }
}
