package fr.myriapod.milkywayexplorer.surface;

import fr.myriapod.milkywayexplorer.Ressource;
import fr.myriapod.milkywayexplorer.mytools.PasteSchem;
import fr.myriapod.milkywayexplorer.spaceexplorer.spaceship.Ship;
import fr.myriapod.milkywayexplorer.surface.machinery.Drill;
import fr.myriapod.milkywayexplorer.surface.machinery.Machinery;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.joml.Random;
import org.joml.Vector2i;
import org.joml.Vector3d;
import org.joml.Vector3i;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SurfacePlanet {


    private int side;
    private int seed;
    private final Ressource[] ores;
    private final Map<Ressource, Set<Vector3i>> oresPoseFinal = new HashMap<>();
    private final Map<Ressource, Set<Vector3i>> oresPose = new HashMap<>();
    private World world;
    private Set<Player> players = new HashSet<>();
    private Set<Machinery> allMachineries = new HashSet<>();


    public SurfacePlanet(int radius, int seed) {
        this.side = (int) Math.sqrt(radius * radius * Math.PI * 4);
        this.seed = seed;

        Ressource[] ores = new Ressource[3];
        Ressource[] allOres = Ressource.values();
        for (int i = 0; i < ores.length ; i++) {
            ores[i] = allOres[new Random(seed).nextInt(allOres.length)];
        }
        ores[2] = Ressource.IRON;
        this.ores = ores;
    }

    public World getWorld() {
        return world;
    }

    public int getSide() {
        return side;
    }


    public void generate() {
        WorldCreator wc = new WorldCreator("world_" + seed);

        wc.type(WorldType.FLAT);
        wc.generator(new CustomPlanetGeneration(seed, side, SurfaceTypes.WHITE_STONES, ores));
        wc.generateStructures(false);

        world = wc.createWorld();

        world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        world.setClearWeatherDuration(10);


        Map<Ressource, Set<Vector2i>> oresPosChunk = ((CustomPlanetGeneration) (wc.generator())).getOrePose();

        generateVeins(oresPosChunk);

    }

    private void generateVeins(Map<Ressource, Set<Vector2i>> oresPosChunk) {
        for(Ressource r : oresPosChunk.keySet()) {
            if(r.getModelName() != null) {

                Set<Vector3i> set = new HashSet<>();

                for(Vector2i v : oresPosChunk.get(r)) {
                    Vector3i pos = new Vector3i(v.x * 16, world.getHighestBlockYAt(v.x * 16, v.y * 16), v.y * 16);

                    new PasteSchem().generate(new Location(world, pos.x, pos.y, pos.z), r.getModelName(), true);
                    set.add(pos);
                }

                oresPose.put(r, set);
                oresPoseFinal.put(r, set);
            }
        }
    }

    public Map<Ressource, Set<Vector3i>> getOresPose() {
        return oresPose;
    }

    public void addMachinery(Machinery machinery) {
        allMachineries.add(machinery);

        if(machinery instanceof Drill d) {
            for(Ressource r : d.getRessources()) {
                Set<Vector3i> set = oresPose.get(r);
                set.remove(d.getLocation());
                oresPose.put(r, set);
            }

        }

    }



    public void shipLands(Vector3d vector3d, Player player) {
        players.add(player);
        Location loc = new Location(world, vector3d.x, vector3d.y, vector3d.z);
        Interaction interaction = (Interaction) world.spawnEntity(loc, EntityType.INTERACTION);

        interaction.addScoreboardTag("ship");
        interaction.setInteractionWidth(2.5f);
        interaction.setInteractionHeight(2.5f);

        new PasteSchem().generate(loc, "ship");

    }

    public void shipLands(Ship ship) {
        players.add(ship.getPlayer());
        Vector3i shipPos = spacePosToPlanet(ship);

        Location shipLoc = new Location(world, shipPos.x, world.getHighestBlockYAt(shipPos.x, shipPos.z), shipPos.z);
        Interaction interaction = (Interaction) world.spawnEntity(shipLoc, EntityType.INTERACTION);

        interaction.addScoreboardTag("ship");
        interaction.setInteractionWidth(2.5f);
        interaction.setInteractionHeight(2.5f);

    }

    public void shipGoes(Ship ship) {
        players.remove(ship.getPlayer());
        Vector3d shipPos = planetToSpacePos(ship);



    }


    private static Vector3d planetToSpacePos(Ship ship) {
        //TODO MATHS THAT DETERMINES SHIP POS ON SURFACE DEPENDING OF AREA
        return new Vector3d();
    }


    private static Vector3i spacePosToPlanet(Ship ship) {
        //TODO MATHS THAT DETERMINES SHIP POS ON SURFACE DEPENDING OF AREA
        return new Vector3i();
    }

    public Machinery getMachinery(Location entityLoc) {
        Vector3i v = new Vector3i((int) entityLoc.getX(), (int) entityLoc.getY(), (int) entityLoc.getZ());
        for(Machinery m : allMachineries) {
            if(m.getLocation().equals(v)) {
                return m;
            }
        }
        return null;
    }
}
