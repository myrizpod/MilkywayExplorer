package fr.myriapod.milkywayexplorer.tools;

import fr.myriapod.milkywayexplorer.Main;
import fr.myriapod.milkywayexplorer.Planet;
import fr.myriapod.milkywayexplorer.spaceexplorer.spaceobjects.StarSystem;
import fr.myriapod.milkywayexplorer.surface.machinery.Machinery;
import fr.myriapod.milkywayexplorer.surface.machinery.machinerytype.MachineryType;
import fr.myriapod.milkywayexplorer.techtree.Tech;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.joml.Vector3d;
import org.joml.Vector3i;

import java.util.*;

public class SaveFile {

    FileConfiguration config = Main.plugin.getConfig();


    public void saveGame(int seed, List<Tech> unlocked) {
        config.set("seed", seed);

        List<String> s = new ArrayList<>();
        for(Tech t : unlocked) {
            s.add(t.name());
        }
        config.set("techs", s);
    }


    public void saveStarSystem(StarSystem starSystem) {

        for(Planet p : starSystem.getAllPlanets()) {
            for(UUID uuid : p.getAllMachines().keySet()) {
                Machinery m = p.getAllMachines().get(uuid);
                String path = "starsystems." + formatVectorAsString(starSystem.getCenter()) + "." + starSystem.getAllPlanets().indexOf(p) + ".machineries." + m.getID();
                List<String> chunckCoords = config.isSet(path) ? config.getStringList(path) : new ArrayList<>();
                Location loc = Bukkit.getEntity(uuid).getLocation();
                Vector3i v = new Vector3i(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
                String vector = formatVectorAsString(v);
                if(! chunckCoords.contains(vector)) {
                    chunckCoords.add(vector);
                }
                config.set(path, chunckCoords);
            }
        }

    }


    public List<StarSystem> getAllStarSystems(int seed) {
        List<StarSystem> starSystems = new ArrayList<>();

        if(! config.contains("starsystems")) {
            return starSystems;
        }

        for(String s : config.getConfigurationSection("starsystems.deconnection").getKeys(false)) {
            StarSystem ss = new StarSystem(formatStringAsVectord(s), seed);
            starSystems.add(ss);
        }

        return starSystems;
    }


    public void registerPlayerPos(StarSystem ss) {
        String path = "starsystems.deconnection." + formatVectorAsString(ss.getCenter());

        config.set(path, true);
    }


    public void savePlayer(Player p) {
        config.set("players." + p.getUniqueId().toString(), true);
    }


    public boolean isFirstTimePlayer(Player player) {
        return ! config.contains("players." + player.getUniqueId().toString());
    }


    public boolean doPlanetHasMachinery(Vector3d starSystemPos, int index) {
        return config.contains("starsystems." + formatVectorAsString(starSystemPos) + "." + index + ".machineries");
    }

    public Map<Vector3i, Machinery> getAllMachineries(Vector3d starSystemPos, int index) {
        String path = "starsystems." + formatVectorAsString(starSystemPos) + "." + index + ".machineries";
        if(! config.contains(path)) {
            return new HashMap<>();
        }
        Map<Vector3i, Machinery> machineryMap = new HashMap<>();

        for(String s : config.getConfigurationSection(path).getKeys(false)) {
            for(String vectorS : config.getStringList(path + "." + s)) {
                Vector3i v = formatStringAsVectori(vectorS);
                Machinery machinery = MachineryType.createMachineryByID(s, v);

                machineryMap.put(v, machinery);

            }
        }

        return machineryMap;
    }


    private String formatVectorAsString(Vector3d v) {
        return (int) v.x + ";" + (int) v.y + ";" + (int) v.z;
    }

    public static String formatVectorAsString(Vector3i v) {
        return v.x + ";" + v.y + ";" + v.z;
    }

    private Vector3d formatStringAsVectord(String s) {
        String[] coord = s.split(";");
        return new Vector3d(Double.valueOf(coord[0]), Double.valueOf(coord[1]), Double.valueOf(coord[2]));
    }

    private Vector3i formatStringAsVectori(String s) {
        String[] coord = s.split(";");
        return new Vector3i(Integer.valueOf(coord[0]), Integer.valueOf(coord[1]), Integer.valueOf(coord[2]));
    }

}
