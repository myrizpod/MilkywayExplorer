package fr.myriapod.milkywayexplorer;

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

            for(Ressource r : Ressource.values()) {
                ItemStack item = new ItemStack(r.getMaterial());
                ItemMeta meta = item.getItemMeta();

                meta.setCustomModelData(r.getModelData());
                meta.setDisplayName(r.getName());
                item.setAmount(64);

                item.setItemMeta(meta);

                player.getInventory().addItem(item);

            }

        }

        return false;
    }
}
