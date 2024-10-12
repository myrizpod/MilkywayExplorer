package fr.myriapod.milkywayexplorer.spaceexplorer;

import fr.myriapod.milkywayexplorer.spaceexplorer.spaceobjects.StarSystem;
import fr.myriapod.milkywayexplorer.spaceexplorer.spaceship.Ship;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Univers {


    private List<StarSystem> allLoadedSystems = new ArrayList<>();
    private final World world;


    public Univers() {
        WorldCreator wc = new WorldCreator("world_space");
        wc.generateStructures(false);
        wc.keepSpawnInMemory(true);
        wc.type(WorldType.FLAT);

        world = wc.createWorld();

        StarSystem s = new StarSystem(new Vector3d(0 , 0, 0), new Random());

        allLoadedSystems.add(s);
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

}
