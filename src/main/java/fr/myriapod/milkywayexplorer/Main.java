package fr.myriapod.milkywayexplorer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import fr.myriapod.milkywayexplorer.spaceexplorer.spaceobjects.GenPlanet;
import fr.myriapod.milkywayexplorer.spaceexplorer.spaceobjects.SpacePlanet;

import java.util.ArrayList;

public class Main extends JavaPlugin {

    public ArrayList<SpacePlanet> allPlanets = new ArrayList<>();

    public static Main plugin;

    @Override
    public void onEnable() {
        plugin = this;

        Bukkit.getLogger().info("Hello Bozo");

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage("._.");
        }

        getCommand("gen").setExecutor(new GenPlanet(this));

    }
}