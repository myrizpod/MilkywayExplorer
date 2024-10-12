package fr.myriapod.milkywayexplorer.spaceexplorer.spaceobjects;

import fr.myriapod.milkywayexplorer.Game;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.joml.Vector3d;

public class GenPlanetCommand implements CommandExecutor {

    public SpacePlanet newPlanet;
    private final String ERROR = ChatColor.RED + "Usage: /gen <nb_pixel> <radius_space_scaled> <space_pos_x> <space_pos_y> <space_pos_z>";

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

//        if(args.length != 5) {
//            commandSender.sendMessage(ERROR);
//            return false;
//        }

        Player player = (Player) commandSender;
//        Vector3d pos = new Vector3d(Double.parseDouble(args[2]),Double.parseDouble(args[3]),Double.parseDouble(args[4]));


        Game.shipEnters(0, Game.getShipByPlayer(player));

//        newPlanet = new SpacePlanet(pos, Integer.parseInt(args[0]), Double.parseDouble(args[1]), new Random().nextInt(),new Vector3d(0,100,0),0.05,-0.05, Game.getShipByPlayer(player));
//        newPlanet.create();
//        newPlanet.rotatePlanet();

        return true;
    }

}
