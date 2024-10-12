package fr.myriapod.milkywayexplorer;

import fr.myriapod.milkywayexplorer.spaceexplorer.Univers;
import fr.myriapod.milkywayexplorer.surface.SurfacePlanet;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DebugCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;


            //GIVES ALL THE POSSIBLE RESSOURCES TO PLAYER
//            for(Ressource r : Ressource.values()) {
//                ItemStack item = new ItemStack(r.getMaterial());
//                ItemMeta meta = item.getItemMeta();
//
//                meta.setCustomModelData(r.getModelData());
//                meta.setDisplayName(r.getName());
//                item.setAmount(64);
//
//                item.setItemMeta(meta);
//
//                player.getInventory().addItem(item);
//
//            }

            Planet p = Game.getAllLoadedSystems().get(0).getPlanet(0);
            p.getSurfacePlanet().generate();
            p.teleportPlayerToSurface(player);



        }

        return false;
    }
}
