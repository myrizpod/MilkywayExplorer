package fr.myriapod.milkywayexplorer;

import fr.myriapod.milkywayexplorer.spaceexplorer.spaceship.CreateShipCommand;
import fr.myriapod.milkywayexplorer.spaceexplorer.spaceship.DevMoveShipCommand;
import fr.myriapod.milkywayexplorer.spaceexplorer.spaceship.SpaceShipListener;
import fr.myriapod.milkywayexplorer.surface.SurfaceListener;
import fr.myriapod.milkywayexplorer.techtree.TechtreeListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import fr.myriapod.milkywayexplorer.spaceexplorer.spaceobjects.GenPlanetCommand;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

//https://mm.tt/app/map/3454339276?t=W7hAsr21jL tech tree

public class Main extends JavaPlugin {

    public static Main plugin;

    @Override
    public void onEnable() {
        plugin = this;

        Bukkit.getLogger().info("-------------");
        Bukkit.getLogger().info("MilkyWayExplorer enabled");
        Bukkit.getLogger().info("Searching for a save...");
        Bukkit.getLogger().info("-------------");

        createSchematics();

        new Game(); //TODO load game by .json or .yml or just config.yml

        getCommand("gen").setExecutor(new GenPlanetCommand());
        getCommand("ship").setExecutor(new CreateShipCommand());
        getCommand("goto").setExecutor(new DevMoveShipCommand());
        getCommand("debug").setExecutor(new DebugCommand());

        Bukkit.getPluginManager().registerEvents(new TechtreeListener(), this);
        Bukkit.getPluginManager().registerEvents(new SurfaceListener(), this);
        Bukkit.getPluginManager().registerEvents(new SpaceShipListener(), this);

    }

    private void createSchematics() {
        Set<String> schem = new HashSet<>();
        schem.add("ship");
        schem.add("basic_crafter");
        schem.add("basic_drill");
        schem.add("drill");
        schem.add("iron_ore");

        for(String s : schem) {

            String fileName = s + ".schem";
            String dirName = "schematics";

            File schematicsFolder = new File(getDataFolder(), dirName); //creates folder in the server's files
            if (!schematicsFolder.exists()) {
                schematicsFolder.mkdir();
            }

            saveResource(dirName + "/" + fileName, false);

            new File(getDataFolder(), fileName)
                    .renameTo(new File(getDataFolder() + File.separator + dirName + File.separator + fileName));

        }
    }


}