package fr.myriapod.milkywayexplorer.spaceexplorer.spaceship;

import fr.myriapod.milkywayexplorer.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.joml.Vector3d;

import java.util.Random;

public class CreateShip implements CommandExecutor {
    Main main;
    public CreateShip(Main main) {
        this.main = main;

    }

    public Ship newShip;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        Player player = (Player) commandSender;

        newShip = new Ship(player);

        return true;
    }

}
