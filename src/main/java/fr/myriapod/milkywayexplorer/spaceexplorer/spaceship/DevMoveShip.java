package fr.myriapod.milkywayexplorer.spaceexplorer.spaceship;


import fr.myriapod.milkywayexplorer.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.joml.Vector3d;

import java.util.Random;

public class DevMoveShip implements CommandExecutor {
    Main main;
    public DevMoveShip(Main main) {
        this.main = main;

    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {




        return true;
    }

}
