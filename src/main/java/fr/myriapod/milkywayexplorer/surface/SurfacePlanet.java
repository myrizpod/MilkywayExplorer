package fr.myriapod.milkywayexplorer.surface;

import fr.myriapod.milkywayexplorer.Ressource;
import fr.myriapod.milkywayexplorer.mytools.PasteSchem;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Interaction;
import org.joml.Random;
import org.joml.Vector3d;

public class SurfacePlanet {


    private int area;
    private int seed;
    private final Ressource[] ores;
    private World world;

    public SurfacePlanet(int radius, int seed) {
        this.area = (int) (radius * radius * Math.PI * 4);
        this.seed = seed;

        Ressource[] ores = new Ressource[3];
        Ressource[] allOres = Ressource.values();
        for (int i = 0; i < ores.length ; i++) {
            ores[i] = allOres[new Random(seed).nextInt(allOres.length)];
        }
        this.ores = ores;
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

//        new PasteSchem().generate(loc, "ship");

    }
}
