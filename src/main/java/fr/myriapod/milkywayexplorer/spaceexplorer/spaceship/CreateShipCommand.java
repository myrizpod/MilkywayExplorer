package fr.myriapod.milkywayexplorer.spaceexplorer.spaceship;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateShipCommand implements CommandExecutor {

    public Ship newShip;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        Player player = (Player) commandSender;
        commandSender.sendMessage("Created new ship!");

        newShip = new Ship(player);
        newShip.movementLoop();

        return true;
    }
}
