package fr.myriapod.milkywayexplorer.surface;

import fr.myriapod.milkywayexplorer.techtree.TechtreeInventories;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class BlockInteractionListener implements Listener {

    @EventHandler
    public void interactWithEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();

        Entity interaction = event.getRightClicked();
        Set<String> tags = interaction.getScoreboardTags();

        if(interaction.getScoreboardTags().isEmpty()) return;

        String tag = (String) tags.toArray()[0];
        switch (tag) {
            case "techtree":
                player.openInventory(TechtreeInventories.getDefaultInventory());

            }



    }





    @EventHandler
    public void playerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if(item == null) return;

        //TODO check for material and model data of item and easer print schem or I dont know...

        if(SurfaceObject.BASIC_FURNACE.isItemEqual(item)) {

        }






    }


}
