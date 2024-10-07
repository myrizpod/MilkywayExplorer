package fr.myriapod.milkywayexplorer;

import fr.myriapod.milkywayexplorer.spaceexplorer.spaceship.CreateShipCommand;
import fr.myriapod.milkywayexplorer.spaceexplorer.spaceship.DevMoveShipCommand;
import fr.myriapod.milkywayexplorer.surface.BlockInteractionListener;
import fr.myriapod.milkywayexplorer.techtree.TechtreeListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import fr.myriapod.milkywayexplorer.spaceexplorer.spaceobjects.GenPlanetCommand;
import fr.myriapod.milkywayexplorer.spaceexplorer.spaceobjects.SpacePlanet;

import java.util.ArrayList;

//https://mm.tt/app/map/3454339276?t=W7hAsr21jL tech tree

public class Main extends JavaPlugin {

    public ArrayList<SpacePlanet> allPlanets = new ArrayList<>();

    public static Main plugin;

    @Override
    public void onEnable() {
        plugin = this;

        Bukkit.getLogger().info("Hello Bozo");

        new Game();

        getCommand("gen").setExecutor(new GenPlanetCommand());
        getCommand("ship").setExecutor(new CreateShipCommand());
        getCommand("goto").setExecutor(new DevMoveShipCommand());

        Bukkit.getPluginManager().registerEvents(new TechtreeListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockInteractionListener(), this);

    }


}