package fr.myriapod.milkywayexplorer.surface;

import fr.myriapod.milkywayexplorer.Planet;
import fr.myriapod.milkywayexplorer.Game;
import fr.myriapod.milkywayexplorer.surface.ressource.Generable;
import fr.myriapod.milkywayexplorer.surface.ressource.Ressource;
import fr.myriapod.milkywayexplorer.tools.PasteSchem;
import fr.myriapod.milkywayexplorer.surface.listeners.CrafterInventory;
import fr.myriapod.milkywayexplorer.surface.listeners.LoopOnPlanet;
import fr.myriapod.milkywayexplorer.surface.machinery.*;
import fr.myriapod.milkywayexplorer.techtree.TechtreeInventories;
import fr.myriapod.milkywayexplorer.tools.SaveFile;
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
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.joml.Vector3i;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

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

        if(planet == null) return;



        //Conveyor case
        m = Machinery.getAsMachinery(player.getInventory().getItemInMainHand());
        if(m instanceof Conveyor conveyor) {
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
                    ((Conveyor) m).setInput(null);

                    planet.addMachinery(e.getUniqueId(), c);

                    player.getInventory().remove(player.getInventory().getItemInMainHand());

                } else {
                    conveyor.setInput(clicked);
                    player.sendMessage(ChatColor.GREEN + "Vous avez lié votre tapis roulant à cette machine");

                }
                return;
            }

        }




        /******* Tags Logic ********/
        if(tags.contains("techtree")) {
            player.openInventory(TechtreeInventories.getDefaultInventory());
        }


        else if(tags.contains("ship")) {
            planet.teleportPlayerToSpace(player);
        }


        else if(tags.contains("vein")) {
            m = Machinery.getAsMachinery(player.getInventory().getItemInMainHand());

            Set<String> t = new HashSet<>(tags);
            t.remove("vein");

            Generable ressource = (Generable) Ressource.nameToRessource((String) t.toArray()[0]);

            if(ressource == null) return;

            if(m != null) {
                if(m instanceof Drill) {
                    new PasteSchem().generate(entityLoc, m.getModel());
                    interaction.remove();

                    Interaction e = (Interaction) player.getWorld().spawnEntity(entityLoc, EntityType.INTERACTION);
                    e.setInteractionWidth(3);
                    e.setInteractionHeight(3);
                    e.addScoreboardTag("drill");
                    e.addScoreboardTag((String) t.toArray()[0]);


                    Machinery drill = new BasicDrill(new Vector3i((int) (entityLoc.getX()-0.5), (int) entityLoc.getY(), (int) (entityLoc.getZ()-0.5)), ressource);
                    Drill d = (Drill) drill;
                    d.startProduction();

                    planet.addMachinery(e.getUniqueId(), drill);

                    player.getInventory().remove(player.getInventory().getItemInMainHand());
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
            m = planet.getMachinery(entity.getUniqueId());

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
            }


        }

        else if (tags.contains("basic_assembler")) {
            m = planet.getMachinery(entity.getUniqueId());

            if(m instanceof Producter p) {
                Map<Ressource, Integer> prod = p.getProducted();


                Bukkit.getLogger().info("prod " + prod);
                for(Ressource r : prod.keySet()) {
                    if(prod.get(r) > 0) {
                        player.getInventory().addItem(r.getAsItem(prod.get(r)));
                        player.sendMessage(ChatColor.GREEN + "Vous avez obtenu " + ChatColor.AQUA + prod.get(r) + " " + ChatColor.GOLD + r.getName());
                    } else {
                        player.sendMessage(ChatColor.RED + "L'assembleur ne contient rien !");
                    }
                }
            }

        }


        else if(tags.contains("crafter")) {
            m = planet.getMachinery(entity.getUniqueId());

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


        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Machinery m = Machinery.getAsMachinery(item);
            if(m == null) {
                event.setCancelled(true);
                return;
            }

            Location blockLoc = event.getClickedBlock().getLocation();
            Location loc = new Location(player.getWorld(), blockLoc.getBlockX() + 0.5, blockLoc.getBlockY() + 1, blockLoc.getBlockZ() + 0.5);

            try {
                Machinery machinery = m.getClass().getDeclaredConstructor(Vector3i.class).newInstance(new Vector3i((int) loc.getX(), (int) loc.getY(), (int) loc.getZ()));

                new PasteSchem().generate(loc, m.getModel());

                Interaction e = (Interaction) player.getWorld().spawnEntity(loc, EntityType.INTERACTION);
                e.addScoreboardTag(m.getID());
                e.setInteractionHeight(1.3f);
                e.setInteractionWidth(1.3f);

                planet.addMachinery(e.getUniqueId(), machinery);

            } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                     NoSuchMethodException ex) {
                event.setCancelled(true);
                throw new RuntimeException(ex);
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

        Machinery surfaceObject = Machinery.getAsMachinery(item);

        if(item == null) { //TODO ENTITIES SHOULD BE BASE ON ALL MACHINERY UUID AND NOT LOADED CHUNKSrl

            Collection<Entity> entities = player.getWorld().getEntities();

            for(Entity e : entities) {
                if(e.getScoreboardTags().contains("vein")) {
                    e.remove();
                }
            }
            return;
        }

        if(item.hasItemMeta()) {
            if(surfaceObject instanceof Drill || (item.getItemMeta().getCustomModelData() == 1001 && item.getType().equals(Material.DIAMOND_PICKAXE))) { //TODO outillage

                for (Generable r : planet.getSurfacePlanet().getOresPose().keySet()) {
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
    public void playerJoinEvent(PlayerJoinEvent event) {
        SaveFile f = new SaveFile();
        Player p = event.getPlayer();
        if(f.isFirstTimePlayer(p)) {
            Game.startGame(p);
            f.savePlayer(p);
        }

    }


    @EventHandler
    public void foodEvent(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }


    @EventHandler
    public void healthEvent(EntityDamageEvent event) {
        event.setCancelled(true);
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
