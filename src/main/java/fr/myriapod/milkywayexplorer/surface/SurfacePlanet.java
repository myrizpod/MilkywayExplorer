package fr.myriapod.milkywayexplorer.surface;

import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Interaction;
import org.joml.Vector3d;

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
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        world.setClearWeatherDuration(10);

    }


    public void shipLands(Vector3d vector3d) {
        Location loc = new Location(world, vector3d.x, vector3d.y, vector3d.z);
        Interaction interaction = (Interaction) world.spawnEntity(loc, EntityType.INTERACTION);

        interaction.addScoreboardTag("ship");
        interaction.setInteractionWidth(2.5f);
        interaction.setInteractionHeight(2.5f);

        Bukkit.getLogger().info("inter: " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ());

        //TODO Summon ship schem

    }
}
