package fr.myriapod.milkywayexplorer.spaceexplorer;

import fr.myriapod.milkywayexplorer.Planet;
import fr.myriapod.milkywayexplorer.spaceexplorer.spaceobjects.StarSystem;
import fr.myriapod.milkywayexplorer.spaceexplorer.spaceship.Ship;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Univers {


    private final List<StarSystem> allLoadedSystems = new ArrayList<>();
    private final World world;


    public Univers() {
        WorldCreator wc = new WorldCreator("world_space");
        wc.generateStructures(false);
        wc.keepSpawnInMemory(true);
        wc.type(WorldType.FLAT);

        world = wc.createWorld();
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        world.setClearWeatherDuration(Integer.MAX_VALUE);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);


    }

    public void loadAllStarSystems(){
        StarSystem s = new StarSystem(new Vector3d(0 , 0, 0), new Random());

        allLoadedSystems.add(s);

        for(StarSystem ss : allLoadedSystems) {
            ss.loadSystem();
        }

    }


    public void shipEnters(int id, Ship ship) {
        allLoadedSystems.get(id).shipEnters(ship);
    }

    public World getWorld() {
        return world;
    }

    public List<StarSystem> getAllLoadedSystems() {
        return allLoadedSystems;
    }

    public Planet getFirstPlanet() {
        return allLoadedSystems.getFirst().getPlanet(0);
    }

    public Planet getPlayerPlanet(Player player) {
        for(StarSystem ss : allLoadedSystems) {
            for(Planet p : ss.getAllPlanets()) {
                if(p.getSurfacePlanet().getWorld().equals(player.getWorld())) {
                    return p;
                }
            }
        }
        return null;
    }
}
