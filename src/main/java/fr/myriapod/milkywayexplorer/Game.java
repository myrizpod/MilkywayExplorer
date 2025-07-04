package fr.myriapod.milkywayexplorer;

import fr.myriapod.milkywayexplorer.spaceexplorer.Univers;
import fr.myriapod.milkywayexplorer.spaceexplorer.spaceobjects.StarSystem;
import fr.myriapod.milkywayexplorer.spaceexplorer.spaceship.Ship;
import fr.myriapod.milkywayexplorer.surface.Tools;
import fr.myriapod.milkywayexplorer.surface.listeners.PreloadEvent;
import fr.myriapod.milkywayexplorer.surface.machinery.machinerytype.CrafterType;
import fr.myriapod.milkywayexplorer.surface.machinery.machinerytype.DrillType;
import fr.myriapod.milkywayexplorer.surface.machinery.machinerytype.TechtreeType;
import fr.myriapod.milkywayexplorer.techtree.Tech;
import fr.myriapod.milkywayexplorer.techtree.Techtree;
import fr.myriapod.milkywayexplorer.tools.SaveFile;
import org.bukkit.*;
import org.bukkit.entity.Player;
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
    }

    public Game(int seed, List<Tech> techs) {
        shipList = new ArrayList<>();
        techtree = new Techtree(techs);
        univers = new Univers(seed);
        univers.preloadSave();
    }


    public static void saveGame() {
        SaveFile f = new SaveFile();
        f.saveGame(univers.getSeed(), techtree.getUnlocked());
        for(StarSystem s : univers.getAllLoadedSystems()) {
            f.saveStarSystem(s);
        }
        for(Player player : Bukkit.getOnlinePlayers()) {
            StarSystem ss = Game.getSystemByWorld(player.getWorld());
            assert ss != null;
            f.registerPlayerPos(ss);

        }
    }


    public static void startGame(Player player) {
        player.sendTitle(ChatColor.GOLD + "MilkyWayExplorer", ChatColor.DARK_AQUA + "Bienvenue dans", 10, 50, 10);
        Planet planet = univers.getFirstPlanet();
        Tech.getMajorBranches().forEach(Game::unlockTech);

        player.getInventory().clear();
        player.getInventory().addItem(DrillType.MINI.getAsItem());
        player.getInventory().addItem(CrafterType.BASIC.getAsItem());
        player.getInventory().addItem(TechtreeType.TECHTREE_BLOCK.getAsItem());
        player.getInventory().addItem(Tools.MANUAL_DRILL.getAsItem());
        player.getInventory().addItem(Tools.MACHINERY_DESTRUCTOR.getAsItem());
        player.setGameMode(GameMode.SURVIVAL);
        player.setFoodLevel(20);
        player.setHealth(20);

        planet.teleportPlayerToSurface(player);
        Location shipPos = player.getLocation().add(10, 0, 0);
        planet.getSurfacePlanet().shipLands(new Vector3d(shipPos.getX(), player.getWorld().getHighestBlockYAt((int) shipPos.getX(), (int) shipPos.getZ()), shipPos.getZ()), player);

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

    public static Planet getPlayerPlanet(Player player) {
        return univers.getPlayerPlanet(player);
    }

    public static StarSystem getSystemByWorld(World world) {
        for(StarSystem ss : univers.getAllLoadedSystems()) {
            for(Planet p : ss.getAllPlanets()) {
                if(p.getSurfacePlanet().getWorld().equals(world)) {
                    return ss;
                }
            }
        }
        return null;
    }

    public static Planet getPlanetByWorld(World world) {
        for(StarSystem ss : univers.getAllLoadedSystems()) {
            for(Planet p : ss.getAllPlanets()) {
                if(p.getSurfacePlanet().getWorld().equals(world)) {
                    return p;
                }
            }
        }
        return null;
    }

    public static void loadPlanets() {
        univers.restoreSave();
    }

    private static boolean stopLoadEvent = false;

    public static boolean isUniversPreloaded() {
        boolean check = (!PreloadEvent.loadedEntitiesChunk.isEmpty()) && PreloadEvent.loadedEntitiesChunk.containsAll(PreloadEvent.loadedChunk) && univers != null;
        if(check) {
            stopLoadEvent = true;
        }
        return check;
    }

    public static boolean stopLoadEvent() {
        return stopLoadEvent;
    }
}
