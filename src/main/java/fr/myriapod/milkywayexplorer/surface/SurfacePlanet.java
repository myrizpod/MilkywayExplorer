package fr.myriapod.milkywayexplorer.surface;

import fr.myriapod.milkywayexplorer.Game;
import fr.myriapod.milkywayexplorer.surface.ressource.*;
import fr.myriapod.milkywayexplorer.tools.PasteSchem;
import fr.myriapod.milkywayexplorer.spaceexplorer.spaceship.Ship;
import fr.myriapod.milkywayexplorer.surface.machinery.Drill;
import fr.myriapod.milkywayexplorer.surface.machinery.Machinery;
import fr.myriapod.milkywayexplorer.tools.SaveFile;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.joml.Random;
import org.joml.Vector2i;
import org.joml.Vector3d;
import org.joml.Vector3i;

import java.util.*;

public class SurfacePlanet {


    private final int side;
    private final int seed;
    private final Generable[] ores;
    private final Map<Generable, Set<Vector3i>> oresPoseFinal = new HashMap<>();
    private final Map<Generable, Set<Vector3i>> oresPose = new HashMap<>();
    private World world;
    private final Set<Player> players = new HashSet<>();
    private final Map<UUID, Machinery> allMachineries = new HashMap<>();


    public SurfacePlanet(int radius, int seed) {
        this.side = (int) Math.sqrt(radius * radius * Math.PI * 4);
        this.seed = seed;

        Generable[] ores = new Generable[3];
        Generable[] allOres = Generable.values();
        for (int i = 0; i < ores.length ; i++) {
            ores[i] = allOres[new Random(seed).nextInt(allOres.length)];
        }
        ores[2] = Generable.IRON; //TODO DEBUG TO HAVE RESSOURCE
        this.ores = ores;
    }

    public World getWorld() {
        return world;
    }

    public int getSide() {
        return side;
    }


    public void generate() {
        String worldName = "world_" + seed;

        WorldCreator wc = new WorldCreator(worldName);

        wc.type(WorldType.FLAT);
//        wc.generator(new CustomPlanetGeneration(seed, side, SurfaceTypes.WHITE_STONES, ores));
        wc.generator(new CustomPlanetGeneration(seed, side, SurfaceTypes.RED_DUNES, ores));
        wc.generateStructures(false);

        world = wc.createWorld();

        assert world != null;
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        world.setClearWeatherDuration(10);


        Map<Generable, Set<Vector2i>> oresPosChunk = ((CustomPlanetGeneration) (wc.generator())).getOrePose();

        generateVeins(oresPosChunk);
    }


    private void generateVeins(Map<Generable, Set<Vector2i>> oresPosChunk) {
        for(Generable r : oresPosChunk.keySet()) {
            if(r.getModelName() != null) {

                Set<Vector3i> set = new HashSet<>();

                for(Vector2i v : oresPosChunk.get(r)) {
                    boolean done = false;

                    Map<Vector3i, Machinery> allMachineries = new SaveFile().getAllMachineries(Game.getSystemByWorld(world).getCenter(), 0);

                    for (Machinery m : allMachineries.values()) {
                        if(m instanceof Drill) {
                            Vector2i chunckPos = new Vector2i(m.getLocation().x/16, m.getLocation().z/16);

                            if(chunckPos.equals(v)) {
                                done = true;
                            }
                        }
                    }
                    if(! done) {
                        Vector3i pos = new Vector3i(v.x * 16, world.getHighestBlockYAt(v.x * 16, v.y * 16), v.y * 16);

                        new PasteSchem().generate(new Location(world, pos.x, pos.y, pos.z), r.getModelName(), true);
                        set.add(pos);
                    }
                }

                oresPose.put(r, set);
                oresPoseFinal.put(r, set);
            }
        }
    }

    public Map<Generable, Set<Vector3i>> getOresPose() {
        return oresPose;
    }

    public void addMachinery(UUID uuid, Machinery machinery) {
        allMachineries.put(uuid, machinery);

        if(machinery instanceof Drill d) {
            for(Generable r : d.getRessources()) {
                Set<Vector3i> set = oresPose.get(r);
                set.remove(d.getLocation());
                oresPose.put(r, set);
            }

        }

    }

    public Map<UUID, Machinery> getAllMachineries() {
        return allMachineries;
    }



    public void shipLands(Vector3d vector3d, Player player) {
        players.add(player);
        Location loc = new Location(world, vector3d.x, vector3d.y, vector3d.z);
        Interaction interaction = (Interaction) world.spawnEntity(loc, EntityType.INTERACTION);

        interaction.addScoreboardTag("ship");
        interaction.setInteractionWidth(6f);
        interaction.setInteractionHeight(4f);

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

    public Machinery getMachinery(UUID uuid) {
        return allMachineries.get(uuid);
    }

    public void setAllMachineries(Map<Vector3i, Machinery> allMachineries) {
        //HUGE CHANCE IT WONT WORK, MAYBE DO INTERACTION PROPERTIES IN EACH MACHINERY
        for(Entity e : world.getEntities()) {
            for(Vector3i v : allMachineries.keySet()) {
                Location loc = e.getLocation();
                Set<String> tags = e.getScoreboardTags();
                if(tags.contains("vein")) {
                    continue;
                }
                if (loc.getBlockX() == v.x && loc.getBlockZ() == v.z) {

                    Machinery machinery = allMachineries.get(v);
                    this.allMachineries.put(e.getUniqueId(), machinery);


                    //TODO CHANGE FOR ALL TYPES
                    if(machinery instanceof Drill d) {
                        Set<String> t = new HashSet<>(tags);
                        t.remove("drill");

                        Generable ressource = Generable.nameToRessource((String) t.toArray()[0]);

                        Bukkit.getLogger().info("r " + ressource);

                        if(ressource == null) continue;

                        d.setProduction(ressource);
                        d.startProduction();
                    }

                }
            }

        }

    }
}
