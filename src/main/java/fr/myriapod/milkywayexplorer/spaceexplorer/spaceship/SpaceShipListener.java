package fr.myriapod.milkywayexplorer.spaceexplorer.spaceship;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDismountEvent;

public class SpaceShipListener implements Listener {

    @EventHandler
    public void quitPassenger(EntityDismountEvent event) {
        Entity dismounter = event.getEntity();
        Entity dismounted = event.getDismounted();

        if(dismounter instanceof Player) {
            if(dismounted.getScoreboardTags().contains("ship") && ! dismounter.isOp()) {
                event.setCancelled(true);
            }
        }


    }


}
