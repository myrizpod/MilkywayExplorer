package fr.myriapod.milkywayexplorer.techtree;

import fr.myriapod.milkywayexplorer.Game;
import fr.myriapod.milkywayexplorer.Ressource;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class TechtreeListener implements Listener {


    @EventHandler
    public void inventoryClickEvent(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        String invName = event.getView().getTitle();
        ItemStack item = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();


        if(invName.equals(Techtree.INVENTORY_NAME)) {
            List<Tech> majorTechs = Tech.getMajorBranches();

            for(Tech t : majorTechs) {
                if(item.getType().equals(t.getMaterial()) && item.getItemMeta().getDisplayName().equals(t.getName())) {
                    player.openInventory(TechtreeInventories.getTree(t));
                }
            }
        }


        for(Tech t : Tech.getMajorBranches()) {
            if (invName.equals(Techtree.INVENTORY_NAME + t.getName())) {
                for(Tech son : t.getSons()) {
                    if(item.getType().equals(son.getMaterial()) && item.getItemMeta().getDisplayName().equals(son.getName())) {

                        Map<Ressource, Integer> playerRessources = Ressource.inventoryToRessources(player.getInventory());

                        if(son.getPrice().equals(playerRessources)) {
                            Game.unlockTech(son);
                        }
                    }
                }
            }
        }

    }

}
