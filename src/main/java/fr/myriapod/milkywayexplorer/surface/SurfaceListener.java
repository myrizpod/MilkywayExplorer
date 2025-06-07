package fr.myriapod.milkywayexplorer.surface;

import fr.myriapod.milkywayexplorer.Planet;
import fr.myriapod.milkywayexplorer.Game;
import fr.myriapod.milkywayexplorer.spaceexplorer.spaceobjects.StarSystem;
import fr.myriapod.milkywayexplorer.surface.listeners.ConveyorManager;
import fr.myriapod.milkywayexplorer.surface.listeners.MachineryDestructorLogic;
import fr.myriapod.milkywayexplorer.surface.machinery.machinerytype.*;
import fr.myriapod.milkywayexplorer.surface.ressource.Generable;
import fr.myriapod.milkywayexplorer.surface.ressource.Ressource;
import fr.myriapod.milkywayexplorer.tools.PasteSchem;
import fr.myriapod.milkywayexplorer.surface.listeners.CrafterInventory;
import fr.myriapod.milkywayexplorer.surface.listeners.LoopOnPlanet;
import fr.myriapod.milkywayexplorer.surface.machinery.*;
import fr.myriapod.milkywayexplorer.techtree.TechtreeInventories;
import fr.myriapod.milkywayexplorer.tools.SaveFile;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.EntitiesLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.joml.Vector3i;

import java.util.*;

public class SurfaceListener implements Listener {

    @EventHandler
    public void interactWithEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        Location entityLoc = entity.getLocation();
        Planet planet = Game.getPlayerPlanet(player);

        Entity interaction = event.getRightClicked();
        Set<String> tags = interaction.getScoreboardTags();

        if(tags.isEmpty()) return;

        if(planet == null) return;

        Machinery actualMachinery = planet.getMachinery(entity.getUniqueId());
        MachineryType actualMachineryType = Machinery.getAsMachinery(player.getInventory().getItemInMainHand());


        if(Tools.MACHINERY_DESTRUCTOR.isEqual(player.getItemInUse())) {
            new MachineryDestructorLogic().playerInteractWithDestructorEvent(event);
            return;
        }


        /******* Tags Logic ********/
        if(tags.contains(TechtreeType.TECHTREE_BLOCK.getID())) {
            player.openInventory(TechtreeInventories.getDefaultInventory());
        }


        else if(tags.contains("ship")) {
            planet.teleportPlayerToSpace(player);
        }


        else if(tags.contains("vein")) {
            Set<String> t = new HashSet<>(tags);
            t.remove("vein");

            Generable ressource = Generable.nameToRessource((String) t.toArray()[0]);

            if(ressource == null) return;

            if(actualMachineryType != null) {
                if(actualMachineryType instanceof DrillType) {
                    new PasteSchem().generate(entityLoc, actualMachineryType.getModel());
                    interaction.remove();

                    Interaction e = (Interaction) player.getWorld().spawnEntity(entityLoc, EntityType.INTERACTION);
                    MachineryType.SchematicSetting setting = actualMachineryType.getSchematicSetting();
                    e.setInteractionWidth(setting.getWidth());
                    e.setInteractionHeight(setting.getHeight());
                    e.addScoreboardTag(actualMachineryType.getID());
                    e.addScoreboardTag((String) t.toArray()[0]);


                    Drill drill = new Drill((DrillType) actualMachineryType,new Vector3i((int) (entityLoc.getX()-0.5), (int) entityLoc.getY(), (int) (entityLoc.getZ()-0.5)));
                    drill.setProduction(ressource);
                    drill.startProduction();

                    planet.addMachinery(e.getUniqueId(), drill);

                    player.getInventory().remove(player.getInventory().getItemInMainHand());
                }

            } else {
                ItemStack item = player.getInventory().getItemInMainHand();

                if(item.getItemMeta() == null) return;

                // Cas particulier de la pioche, faire apres une classe outillage qui va gerer tous les outils
                if(Tools.MANUAL_DRILL.canBeUsedByPlayer(player)) {
                    if (Tools.MANUAL_DRILL.isEqual(item)) {
                        player.getInventory().addItem(ressource.getProduct().getAsItem(1));
                        Tools.MANUAL_DRILL.setCooldownForPlayer(player);
                    }
                }

            }
        }


        else if(tags.contains(DrillType.BASIC.getID())) {
            if(actualMachinery instanceof Drill drill) {
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

        else if (tags.contains(AssemblerType.BASIC.getID())) {
            if(actualMachinery instanceof Producter p) {
                Map<Ressource, Integer> prod = p.getProducted();


                Bukkit.getLogger().info("prod " + prod);
                for(Ressource r : prod.keySet()) {
                    if(prod.get(r) > 0) {
                        player.getInventory().addItem(r.getAsItem(prod.get(r)));
                        player.sendMessage(ChatColor.GREEN + "Vous avez obtenu " + ChatColor.AQUA + prod.get(r) + " " + ChatColor.GOLD + r.getName());
                    } else {
                        player.sendMessage(ChatColor.RED + "Le " + actualMachineryType.getName() + " ne contient rien !");
                    }
                }
            }

        }


        else if(tags.contains(CrafterType.BASIC.getID())) {
            if(actualMachinery instanceof Crafter c) {
                player.openInventory(c.getCrafterInventory());

            }

        }

    }




    @EventHandler
    public void playerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Planet planet = Game.getPlayerPlanet(player);
        ItemStack item = event.getItem();

        if(item == null) {
            event.setCancelled(true);
            return;
        }
        if(planet == null) {
            event.setCancelled(true);
            return;
        }


        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if(Tools.MACHINERY_DESTRUCTOR.isEqual(item)) {
                new MachineryDestructorLogic().playerInteractWithDestructorEvent(event);
                return;
            }

            MachineryType m = Machinery.getAsMachinery(item);
            if(m == null) {
                event.setCancelled(true);
                return;
            }
            if(m instanceof ConveyorType c) {
                new ConveyorManager().playerPlaceConveyorEvent(event, c);
                return;
            }
            if(m instanceof DrillType) {
                event.setCancelled(true);
                return;
            }

            Location blockLoc = event.getClickedBlock().getLocation();
            Location loc = new Location(player.getWorld(), blockLoc.getBlockX() + 0.5, blockLoc.getBlockY() + 1, blockLoc.getBlockZ() + 0.5);

            Machinery machinery = MachineryType.createMachineryByID(m.getID(), new Vector3i((int) loc.getX(), (int) loc.getY(), (int) loc.getZ()));

            new PasteSchem().generate(loc, m.getModel());

            Interaction e = (Interaction) player.getWorld().spawnEntity(loc, EntityType.INTERACTION);
            MachineryType.SchematicSetting setting = m.getSchematicSetting();
            e.addScoreboardTag(m.getID());
            e.setInteractionHeight(setting.getHeight());
            e.setInteractionWidth(setting.getWidth());

            planet.addMachinery(e.getUniqueId(), machinery);

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

        MachineryType surfaceObject = Machinery.getAsMachinery(item);

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
            if(surfaceObject instanceof DrillType || Tools.MANUAL_DRILL.isEqual(item)) {

                for (Generable r : planet.getSurfacePlanet().getOresPose().keySet()) {
                    for (Vector3i v : planet.getSurfacePlanet().getOresPose().get(r)) {
                        Interaction e = (Interaction) player.getWorld().spawnEntity(new Location(player.getWorld(), v.x+0.5, v.y, v.z+0.5), EntityType.INTERACTION);

                        e.setInteractionHeight(2.5f);
                        e.setInteractionWidth((float) 11);
                        e.addScoreboardTag("vein");
                        e.addScoreboardTag(r.getModelName());

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
    public void playerQuitEvent(PlayerQuitEvent event) {
        SaveFile f = new SaveFile();
        Player p = event.getPlayer();
        StarSystem ss = Game.getSystemByWorld(p.getWorld());

        assert ss != null;
        f.registerPlayerPos(ss);
    }

    @EventHandler
    public void poseBlockEvent(BlockPlaceEvent event) { //new ConveyorManager().playerBlockPlace(event); }
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


    public static Set<Chunk> loadedChunk = new HashSet<>();
    public static Set<Chunk> loadedEntitiesChunk = new HashSet<>();

//    @EventHandler
//    public void chunkLoadEvent(ChunkLoadEvent event) {
//        if(Game.isUniversPreloaded()) {
//            return;
//        }
//
//        try {
//            if (Game.getSystemByWorld(event.getWorld()) != null) {
//                Bukkit.getLogger().info("Load chunk at: " + event.getChunk().getX() + "    " + event.getChunk().getZ());
//                loadedChunk.add(event.getChunk());
//            }
//        } catch (NullPointerException e) {
//
//        }
//    }

    @EventHandler
    public void entitiesLoadedEvent(EntitiesLoadEvent event) {
        if(Game.stopLoadEvent()) {
            return;
        }

        try {
            if (Game.getSystemByWorld(event.getWorld()) != null) {
                Bukkit.getLogger().info("Load entities at: " + event.getWorld().getName() + "   " + event.getChunk().getX() + "    " + event.getChunk().getZ());
                loadedEntitiesChunk.add(event.getChunk());
            }
        } catch (NullPointerException e) {

        }
    }

}
