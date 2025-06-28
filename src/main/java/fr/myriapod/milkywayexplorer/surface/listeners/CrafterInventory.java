package fr.myriapod.milkywayexplorer.surface.listeners;

import fr.myriapod.milkywayexplorer.Game;
import fr.myriapod.milkywayexplorer.Planet;
import fr.myriapod.milkywayexplorer.surface.machinery.Machinery;
import fr.myriapod.milkywayexplorer.surface.machinery.machinerytype.CrafterType;
import fr.myriapod.milkywayexplorer.surface.machinery.machinerytype.MachineryType;
import fr.myriapod.milkywayexplorer.surface.ressource.Ressource;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Map;

public class CrafterInventory {


    public void inCrafterInventory(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Planet planet = Game.getPlayerPlanet(player);

        if(planet == null) return;


        for(CrafterType c : CrafterType.values()) {
            if (event.getView().getTitle().equals(ChatColor.GOLD + c.getName())) {
                MachineryType buy = Machinery.getAsMachinery(event.getCurrentItem());

                if(buy != null) {
                    Map<Ressource, Integer> playerRessources = Ressource.inventoryToRessources(player.getInventory());
                    Map<Ressource, Integer> ressourcesManquantes = Ressource.ressourcesManquantes(playerRessources, buy.getPrice());

                    if(ressourcesManquantes.isEmpty()) {
                        player.getInventory().addItem(buy.getAsItem());

                        buy.getPrice().forEach((ressource, integer) -> {
                            player.getInventory().removeItem(ressource.getAsItem(integer));
//                            for (ItemStack i : player.getInventory().getContents()) {
//                                if (ressource.isEqual(i)) {
//                                    ItemStack it = i.clone();
//                                    it.setAmount(remove);
//                                    player.getInventory().removeItem(it);
//                                }
//                            }
                        });


                    } else {
                        String message = ChatColor.RED + "Il vous manque: ";
                        for(Ressource r : ressourcesManquantes.keySet()) {
                            message += "\n" + ChatColor.GOLD + ressourcesManquantes.get(r) + " " + ChatColor.AQUA + r.getName();
                        }

                        player.sendMessage(message);
                    }

                    event.setCancelled(true);

                }



            }
        }




    }


}
