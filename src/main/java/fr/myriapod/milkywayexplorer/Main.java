package fr.myriapod.milkywayexplorer;

import fr.myriapod.milkywayexplorer.spaceexplorer.spaceship.CreateShip;
import fr.myriapod.milkywayexplorer.spaceexplorer.spaceship.DevMoveShip;
import fr.myriapod.milkywayexplorer.spaceexplorer.spaceship.Ship;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import fr.myriapod.milkywayexplorer.spaceexplorer.spaceobjects.GenPlanet;
import fr.myriapod.milkywayexplorer.spaceexplorer.spaceobjects.SpacePlanet;

import java.util.ArrayList;

//https://mm.tt/app/map/3454339276?t=W7hAsr21jL tech tree

public class Main extends JavaPlugin {

    public ArrayList<SpacePlanet> allPlanets = new ArrayList<>();

    public static Main plugin;
    public Ship ship;

    @Override
    public void onEnable() {
        plugin = this;

        Bukkit.getLogger().info("Hello Bozo");

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage("._.");
        }

        getCommand("gen").setExecutor(new GenPlanet());
        getCommand("ship").setExecutor(new CreateShip());
        getCommand("goto").setExecutor(new DevMoveShip());

    }


}