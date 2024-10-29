package fr.myriapod.milkywayexplorer.surface;

import fr.myriapod.milkywayexplorer.Planet;
import fr.myriapod.milkywayexplorer.Game;
import fr.myriapod.milkywayexplorer.Ressource;
import fr.myriapod.milkywayexplorer.mytools.PasteSchem;
import fr.myriapod.milkywayexplorer.surface.listeners.CrafterInventory;
import fr.myriapod.milkywayexplorer.surface.listeners.LoopOnPlanet;
import fr.myriapod.milkywayexplorer.surface.machinery.*;
import fr.myriapod.milkywayexplorer.techtree.TechtreeInventories;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.joml.Vector3i;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SurfaceListener implements Listener {

    @EventHandler
    public void interactWithEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        Location entityLoc = entity.getLocation();
        Planet planet = Game.getPlayerPlanet(player);
        Machinery m;

        Entity interaction = event.getRightClicked();
        Set<String> tags = interaction.getScoreboardTags();

        if(tags.isEmpty()) return;

        /******* Tags Logic ********/
        if(tags.contains("techtree")) {
            player.openInventory(TechtreeInventories.getDefaultInventory());
        }


        else if(tags.contains("ship")) {
            if(planet == null) return;

            planet.teleportPlayerToSpace(player);
        }


        else if(tags.contains("vein")) {
            m = new MachineryAnnotationProcessor().getAsMachinery(player.getInventory().getItemInMainHand());

            Set<String> t = new HashSet<>(tags);
            t.remove("vein");

            Ressource ressource = Ressource.nameToRessource((String) t.toArray()[0]);

            if(ressource == null) return;

            if(m != null) {
                if(m instanceof Drill) {
                    if(planet == null) return;

                    new PasteSchem().generate(entityLoc, m.getModel());
                    interaction.remove();

                    Interaction e = (Interaction) player.getWorld().spawnEntity(entityLoc, EntityType.INTERACTION);
                    e.setInteractionWidth(3);
                    e.setInteractionHeight(3);
                    e.addScoreboardTag("drill");


                    Machinery drill = new BasicDrill(new Vector3i((int) (entityLoc.getX()-0.5), (int) entityLoc.getY(), (int) (entityLoc.getZ()-0.5)), ressource, 0.1);

                    planet.addMachinery(drill);
                }

            } else {
                ItemStack item = player.getInventory().getItemInMainHand();

                if(item.getItemMeta() == null) return;

                // Cas particulier de la pioche, faire apres une classe outillage qui va gerer tous les outils
                if(! player.hasCooldown(Material.DIAMOND_PICKAXE)) {
                    if (item.getType().equals(Material.DIAMOND_PICKAXE) && item.getItemMeta().getCustomModelData() == 1001) {
                        player.getInventory().addItem(ressource.getAsItem(1));
                        player.setCooldown(Material.DIAMOND_PICKAXE, 5 * 20);
                    }
                }

            }
        }

        else if(tags.contains("drill")) {
            if(planet == null) return;

            m = planet.getMachinery(entityLoc);

            if(m instanceof BasicDrill) {
                Drill drill = (Drill) m;
                Map<Ressource, Integer> prod = drill.getProducted();

                for(Ressource r : prod.keySet()) {
                    if(prod.get(r) > 0) {
                        player.getInventory().addItem(r.getAsItem(prod.get(r)));
                        player.sendMessage(ChatColor.GREEN + "Vous avez obtenu " + ChatColor.AQUA + prod.get(r) + " " + ChatColor.GOLD + r.getName());
                    } else {
                        player.sendMessage(ChatColor.RED + "La foreuse ne contient rien !");
                    }
                }
            } else {
                //TODO check if tapis roulant en main et faire des connecteurs
            }
        }

        else if(tags.contains("crafter")) {
            if(planet == null) return;

            m = planet.getMachinery(entityLoc);

            if(m instanceof BasicCrafter) {
                Crafter c = (Crafter) m;

                player.openInventory(c.getCrafterInventory());

            }



        }



    }




    @EventHandler
    public void playerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Planet planet = Game.getPlayerPlanet(player);
        ItemStack item = event.getItem();

        if(item == null) return;

        if(planet == null) return;

//        if(item.getType().equals(Material.FLETCHING_TABLE) && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
//            Location blockLoc = event.getClickedBlock().getLocation();
//            Location loc = new Location(player.getWorld(), blockLoc.getBlockX() + 0.5, blockLoc.getBlockY() + 1, blockLoc.getBlockZ() + 0.5);
//
//            Interaction e = (Interaction) player.getWorld().spawnEntity(loc, EntityType.INTERACTION);
//            e.addScoreboardTag("techtree");
//            e.setInteractionHeight(1.3f);
//            e.setInteractionWidth(1.3f);
//
//        }

        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Machinery m = new MachineryAnnotationProcessor().getAsMachinery(item);
            if(m == null) {
                event.setCancelled(true);
                return;
            }

            if(m instanceof BasicCrafter || m instanceof TechtreeBlock) {
                Location blockLoc = event.getClickedBlock().getLocation();
                Location loc = new Location(player.getWorld(), blockLoc.getBlockX() + 0.5, blockLoc.getBlockY() + 1, blockLoc.getBlockZ() + 0.5);

                Interaction e = (Interaction) player.getWorld().spawnEntity(loc, EntityType.INTERACTION);
                e.addScoreboardTag(m.getModel());
                e.setInteractionHeight(1.3f);
                e.setInteractionWidth(1.3f);

                try {
                    Machinery machinery = m.getClass().getDeclaredConstructor(Vector3i.class).newInstance(new Vector3i((int) loc.getX(), (int) loc.getY(), (int) loc.getZ()));
                    planet.addMachinery(machinery);
                } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                         NoSuchMethodException ex) {
                    throw new RuntimeException(ex);
                }


            }

        }

        else {
            event.setCancelled(true);

        }


    }




    @EventHandler
    public void changeSlot(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        Planet planet = Game.getPlayerPlanet(player);
        ItemStack item = player.getInventory().getItem(event.getNewSlot());

        if(planet == null) {
            return;
        }

        Machinery surfaceObject = new MachineryAnnotationProcessor().getAsMachinery(item);

        if(item == null) {
            Collection<Entity> entities = player.getWorld().getEntities();

            for(Entity e : entities) {
                if(e.getScoreboardTags().contains("vein")) {
                    e.remove();
                }
            }
            return;
        }

        if(item.hasItemMeta()) {
            if(surfaceObject instanceof Drill || (item.getItemMeta().getCustomModelData() == 1001 && item.getType().equals(Material.DIAMOND_PICKAXE))) {

                for (Ressource r : planet.getSurfacePlanet().getOresPose().keySet()) {
                    for (Vector3i v : planet.getSurfacePlanet().getOresPose().get(r)) {
                        Interaction e = (Interaction) player.getWorld().spawnEntity(new Location(player.getWorld(), v.x+0.5, v.y, v.z+0.5), EntityType.INTERACTION);

                        e.setInteractionHeight(2.5f);
                        e.setInteractionWidth((float) 11);
                        e.addScoreboardTag("vein");
                        e.addScoreboardTag(r.getName().toLowerCase());

                    }
                }
            } else {
                Collection<Entity> entities = player.getWorld().getEntities();

                for(Entity e : entities) {
                    if(e.getScoreboardTags().contains("vein")) {
                        e.remove();
                    }
                }
            }

        } else {
            Collection<Entity> entities = player.getWorld().getEntities();

            for(Entity e : entities) {
                if(e.getScoreboardTags().contains("vein")) {
                    e.remove();
                }
            }
        }


    }


    @EventHandler
    public void inventoryEvent(InventoryClickEvent event) {
        new CrafterInventory().inCrafterInventory(event);
    }



    @EventHandler
    public void playerMoveEvent(PlayerMoveEvent event) {
        new LoopOnPlanet().loopOnPlanet(event);
    }


}
