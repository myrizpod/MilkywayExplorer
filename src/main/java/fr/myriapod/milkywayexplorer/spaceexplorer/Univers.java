package fr.myriapod.milkywayexplorer.spaceexplorer;

import fr.myriapod.milkywayexplorer.Planet;
import fr.myriapod.milkywayexplorer.surface.SurfaceListener;
import fr.myriapod.milkywayexplorer.tools.SaveFile;
import fr.myriapod.milkywayexplorer.spaceexplorer.spaceobjects.StarSystem;
import fr.myriapod.milkywayexplorer.spaceexplorer.spaceship.Ship;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Univers {

    private final String WORLD_SPACE_NAME = "world_space";

    private int seed = new Random().nextInt();
    private final List<StarSystem> allLoadedSystems = new ArrayList<>();
    private World world;


    public Univers() {
        createSpaceWorld();
    }

    public Univers(int seed) {
        this.seed = seed;
        createSpaceWorld();

        world.getEntities().forEach(Entity::remove);

        SaveFile f = new SaveFile();

        List<StarSystem> ss = f.getAllStarSystems(seed);
        Bukkit.getLogger().info("nb solar system: " + ss.size());

        allLoadedSystems.addAll(ss);
    }

    public void loadAllStarSystems() {
        StarSystem s = new StarSystem(new Vector3d(0 , 0, 0), seed);

        allLoadedSystems.add(s);

        for(StarSystem ss : allLoadedSystems) {
            ss.preloadSystem();
            ss.loadSystem();
        }
    }

    private void createSpaceWorld() {
        WorldCreator wc = new WorldCreator(WORLD_SPACE_NAME);
        wc.generateStructures(false);
        wc.keepSpawnInMemory(true);
        wc.type(WorldType.FLAT);
        wc.generator(new ChunkGenerator() { //Empty world generator to create an empty world
            @Override
            public void generateSurface(WorldInfo worldInfo, java.util.Random random, int chunkX, int chunkZ, ChunkData chunkData) {}}
        );

        world = wc.createWorld();
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        world.setClearWeatherDuration(Integer.MAX_VALUE);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
    }


    public void restoreSave() {
        for(StarSystem ss : allLoadedSystems) {
            ss.loadSystem();
        }
    }
    public void preloadSave() {
        for(StarSystem ss : allLoadedSystems) {
            ss.preloadSystem();
        }
    }


    public void shipEnters(int id, Ship ship) {
        allLoadedSystems.get(id).shipEnters(ship);
    }

    public World getWorld() {
        return world;
    }

    public int getSeed() {
        return seed;
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
