package fr.myriapod.milkywayexplorer.spaceexplorer.spaceobjects;

import fr.myriapod.milkywayexplorer.Main;
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
        Vector3d pos = new Vector3d(player.getLocation().getX(),player.getLocation().getY(),player.getLocation().getZ());

        newPlanet = new SpacePlanet(pos, Integer.valueOf(args[0]), Double.valueOf(args[1]), new Random().nextInt(),new Vector3d(0,100,0),0.05,-0.05);
        newPlanet.create();
        newPlanet.rotatePlanet();

        return true;
    }

}
