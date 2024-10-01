package fr.myriapod.milkywayexplorer.spaceexplorer.spaceobjects;

import fr.myriapod.milkywayexplorer.Main;
import fr.myriapod.milkywayexplorer.spaceexplorer.spaceship.Ship;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.joml.Vector3d;

import java.util.Random;

public class GenPlanet implements CommandExecutor {
    Main main;
    public GenPlanet(Main main) {
        this.main = main;

    }

    public SpacePlanet newPlanet;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        Player player = (Player) commandSender;
        Vector3d pos = new Vector3d(Double.parseDouble(args[2]),Double.parseDouble(args[3]),Double.parseDouble(args[4]));

        newPlanet = new SpacePlanet(pos, Integer.parseInt(args[0]), Double.parseDouble(args[1]), new Random().nextInt(),new Vector3d(0,100,0),0.05,-0.05,main.ship);
        newPlanet.create();
        newPlanet.rotatePlanet();

        return true;
    }

}
