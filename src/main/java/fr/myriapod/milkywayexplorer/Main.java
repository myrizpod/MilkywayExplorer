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

        new SpacePlanet(this);

        getCommand("gen").setExecutor(new GenPlanet(this));
        getCommand("ship").setExecutor(new CreateShip(this));
        getCommand("goto").setExecutor(new DevMoveShip(this));

    }
}