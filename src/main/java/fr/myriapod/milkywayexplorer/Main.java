package fr.myriapod.milkywayexplorer;

import fr.myriapod.milkywayexplorer.spaceexplorer.spaceship.CreateShipCommand;
import fr.myriapod.milkywayexplorer.spaceexplorer.spaceship.DevMoveShipCommand;
import fr.myriapod.milkywayexplorer.spaceexplorer.spaceship.SpaceShipListener;
import fr.myriapod.milkywayexplorer.surface.SurfaceListener;
import fr.myriapod.milkywayexplorer.surface.listeners.PreloadEvent;
import fr.myriapod.milkywayexplorer.surface.machinery.machinerytype.MachineryType;
import fr.myriapod.milkywayexplorer.surface.ressource.Generable;
import fr.myriapod.milkywayexplorer.techtree.Tech;
import fr.myriapod.milkywayexplorer.techtree.TechtreeListener;
import fr.myriapod.milkywayexplorer.tools.DelayedTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import fr.myriapod.milkywayexplorer.spaceexplorer.spaceobjects.GenPlanetCommand;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//https://mm.tt/app/map/3454339276?t=W7hAsr21jL tech tree

public class Main extends JavaPlugin {

    public static Main plugin;
    public static int id = -1;

    @Override
    public void onEnable() {
        plugin = this;

        //getting listeners
        Bukkit.getPluginManager().registerEvents(new PreloadEvent(), this);

        //Debug info
        Bukkit.getLogger().info("-------------");
        Bukkit.getLogger().info("MilkyWayExplorer enabled");
        Bukkit.getLogger().info("Searching for a save...");

        /****** Load or create Game ******/
        if(getConfig().contains("seed")) {
            int seed = getConfig().getInt("seed");
            List<String> techsAsString = getConfig().getStringList("techs");
            List<Tech> techs = new ArrayList<>();

            for(String s : techsAsString) {
                for (Tech t : Tech.values()) {
                    if (t.name().equals(s)) {
                        techs.add(t);
                    }
                }
            }

            Bukkit.getLogger().info("Game with seed: " + seed + " found !");
            new Game(seed, techs);


        } else {
            new Game();
            for(Player p : Bukkit.getOnlinePlayers()) {
                Game.startGame(p);
            }
        }

        Bukkit.getPluginManager().registerEvents(new TechtreeListener(), this);
        Bukkit.getPluginManager().registerEvents(new SurfaceListener(), this);
        Bukkit.getPluginManager().registerEvents(new SpaceShipListener(), this);
        new DelayedTask(this);

        checkForLoading();


        //important functions to makes the plugin work
        createSchematics();

        Bukkit.unloadWorld("world_nether", false);
        Bukkit.unloadWorld("world_the_end", false);

        //getting debug commands
        getCommand("gen").setExecutor(new GenPlanetCommand());
        getCommand("ship").setExecutor(new CreateShipCommand());
        getCommand("goto").setExecutor(new DevMoveShipCommand());
        getCommand("debug").setExecutor(new DebugCommand());

    }

    private void checkForLoading() {
        id = Bukkit.getScheduler().runTaskTimer(this, () -> {
            if(Game.isUniversPreloaded()) {
                Bukkit.getLogger().info("ssssssssssssssssssssssssssssssssssssssssssssssssss");
                Game.loadPlanets();

                Bukkit.getLogger().info("-------------");
                Bukkit.getServer().getScheduler().cancelTask(id);
                id = -1;
            }
        },2,0).getTaskId();
    }


    @Override
    public void onDisable() {
        //ConveyorManager.resetAllPlacings();
        Game.saveGame();
        saveConfig();
    }




    private void createSchematics() {
        Set<String> schem = new HashSet<>();
        schem.add("ship");

        for(MachineryType m : MachineryType.getAllTypes()) {
            if(m.getModel() != null) {
                schem.add(m.getModel());
            }
        }
        for(Generable g : Generable.values()) {
            if(g.getModelName() != null) {
                schem.add(g.getModelName());
            }
        }


        for(String s : schem) {

            String fileName = s + ".schem";
            String dirName = "schematics";

            File schematicsFolder = new File(getDataFolder(), dirName); //creates folder in the server's files
            if (!schematicsFolder.exists()) {
                schematicsFolder.mkdir();
            }

            if(! new File(getDataFolder() + File.separator + dirName + File.separator + fileName).exists()) {

                saveResource(dirName + "/" + fileName, false);

                new File(getDataFolder(), fileName)
                        .renameTo(new File(getDataFolder() + File.separator + dirName + File.separator + fileName));
            }

        }
    }


}