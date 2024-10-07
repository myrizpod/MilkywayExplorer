package fr.myriapod.milkywayexplorer.surface;

import fr.myriapod.milkywayexplorer.techtree.TechtreeInventories;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.Set;

public class BlockInteractionListener implements Listener {

    @EventHandler
    public void interactWithEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();

        if(event.getRightClicked() instanceof Interaction) {
            Interaction interaction = (Interaction) event.getRightClicked();
            Set<String> tags = interaction.getScoreboardTags();

            if(interaction.getScoreboardTags().size() > 1) return;

            String tag = (String) tags.toArray()[0];
            switch (tag) {
                case "techtree":
                    player.openInventory(TechtreeInventories.getDefaultInventory());

            }

        }

    }


}
