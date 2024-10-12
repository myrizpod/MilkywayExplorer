package fr.myriapod.milkywayexplorer.surface;

import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

public class SurfacePlanet {


    private int radius;
    private int seed;
    private World world;

    public SurfacePlanet(int radius, int seed) {
        this.radius = radius;
        this.seed = seed;
    }

    public World getWorld() {
        return world;
    }


    public void generate() {
        WorldCreator wc = new WorldCreator("world_" + seed);

        wc.type(WorldType.FLAT);
        wc.generator(new CustomPlanetGeneration(seed));
        wc.generateStructures(false);

        world = wc.createWorld();

        world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
    }



}
