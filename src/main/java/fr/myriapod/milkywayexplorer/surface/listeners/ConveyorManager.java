package fr.myriapod.milkywayexplorer.surface.listeners;

import fr.myriapod.milkywayexplorer.Game;
import fr.myriapod.milkywayexplorer.Planet;
import fr.myriapod.milkywayexplorer.surface.machinery.BasicConveyor;
import fr.myriapod.milkywayexplorer.surface.machinery.Conveyor;
import fr.myriapod.milkywayexplorer.surface.machinery.Machinery;
import fr.myriapod.milkywayexplorer.surface.machinery.Producter;
import fr.myriapod.milkywayexplorer.surface.machinery.machinerytype.ConveyorType;
import fr.myriapod.milkywayexplorer.surface.machinery.machinerytype.MachineryType;
import fr.myriapod.milkywayexplorer.tools.SaveFile;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.joml.Vector3i;

import java.util.Set;

public class ConveyorManager {


    //TODO REWORK CONVEYOR CASE WITH SNAKE TECHNIC
    public void interactWithEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        Location entityLoc = entity.getLocation();
        Planet planet = Game.getPlayerPlanet(player);

        Entity interaction = event.getRightClicked();
        Set<String> tags = interaction.getScoreboardTags();

        if(tags.isEmpty()) return;

        if(planet == null) return;

        /*

        MachineryType actualMachineryType = Machinery.getAsMachinery(player.getInventory().getItemInMainHand());
        if(actualMachineryType instanceof ConveyorType conveyor) {
            Machinery mClicked = planet.getMachinery(entity.getUniqueId());
            if(mClicked instanceof Producter clicked) {
                if(conveyor.hasInput()) {
                    Machinery input = (Machinery) conveyor.getInput();
                    Vector3i iLoc = input.getLocation();
                    Vector3i cLoc = mClicked.getLocation();
                    Vector3i pos = iLoc.add(cLoc).div(2);

                    Interaction e = (Interaction) player.getWorld().spawnEntity(new Location(planet.getSurfacePlanet().getWorld(), pos.x, pos.y, pos.z), EntityType.INTERACTION);
                    e.setInteractionWidth(2.5f);
                    e.setInteractionHeight(1.5f);
                    e.addScoreboardTag("conveyor");
                    e.addScoreboardTag(SaveFile.formatVectorAsString(iLoc));
                    e.addScoreboardTag(SaveFile.formatVectorAsString(cLoc));

                    Conveyor c = new BasicConveyor(conveyor.getInput(), clicked, pos);
                    ((Conveyor) actualMachinery).setInput(null);

                    planet.addMachinery(e.getUniqueId(), c);

                    player.getInventory().remove(player.getInventory().getItemInMainHand());

                } else {
                    conveyor.setInput(clicked);
                    player.sendMessage(ChatColor.GREEN + "Vous avez lié votre tapis roulant à cette machine");

                }
                return;
            }

        }

         */

    }

}
