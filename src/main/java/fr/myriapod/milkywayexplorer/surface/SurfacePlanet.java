package fr.myriapod.milkywayexplorer.surface;

import fr.myriapod.milkywayexplorer.Ressource;
import fr.myriapod.milkywayexplorer.mytools.PasteSchem;
import fr.myriapod.milkywayexplorer.mytools.Tuple;
import fr.myriapod.milkywayexplorer.spaceexplorer.spaceship.Ship;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.joml.Random;
import org.joml.Vector2i;
import org.joml.Vector3d;
import org.joml.Vector3i;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SurfacePlanet {


    private int side;
    private int seed;
    private final Ressource[] ores;
    private Map<Ressource, Set<Tuple<Vector2i, Vector3i>>> oresPose; // Where Vector2i is chunk pos and Vector3i exact pos
    private World world;
    private Set<Player> players = new HashSet<>();

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
        wc.generator(new CustomPlanetGeneration(seed, side, ores));
        wc.generateStructures(false);

        world = wc.createWorld();

        world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        world.setClearWeatherDuration(10);


        oresPose = ((CustomPlanetGeneration) (wc.generator())).getOrePose();

        generateVeins();

    }

    private void generateVeins() {
        for(Ressource r : oresPose.keySet()) {
            if(r.getModelName() != null) {

                for(Tuple<Vector2i, Vector3i> t : oresPose.get(r)) {
                    Vector3i pos = t.getB();

                    new PasteSchem().generate(new Location(world, pos.x, world.getHighestBlockYAt(pos.x, pos.z), pos.z), r.getModelName(), true);
                }

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
}
