package fr.myriapod.milkywayexplorer.spaceexplorer.spaceship;


import fr.myriapod.milkywayexplorer.Game;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.joml.Vector3d;

public class DevMoveShipCommand implements CommandExecutor {

    private final String ERROR = ChatColor.RED + "Usage: /goto <space_momentum_x> <space_momentum_y> <space_momentum_z>";

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if(args.length != 3) {
            commandSender.sendMessage(ERROR);
            return false;
        }

        Player player = (Player) commandSender;
        Ship ship = Game.getShipByPlayer(player);
        ship.setMomentum(new Vector3d(Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2])));
        return true;
    }
}
