package fr.myriapod.milkywayexplorer.surface;

import fr.myriapod.milkywayexplorer.Planet;
import fr.myriapod.milkywayexplorer.Game;
import fr.myriapod.milkywayexplorer.Ressource;
import fr.myriapod.milkywayexplorer.mytools.PasteSchem;
import fr.myriapod.milkywayexplorer.surface.machinery.BasicDrill;
import fr.myriapod.milkywayexplorer.surface.machinery.Drill;
import fr.myriapod.milkywayexplorer.surface.machinery.Machinery;
import fr.myriapod.milkywayexplorer.surface.machinery.MachineryAnnotationProcessor;
import fr.myriapod.milkywayexplorer.techtree.TechtreeInventories;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;
import org.joml.Vector3i;

import java.util.Collection;
import java.util.Set;

public class SurfaceListener implements Listener {

    @EventHandler
    public void interactWithEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Planet planet = Game.getPlayerPlanet(player);

        Entity interaction = event.getRightClicked();
        Set<String> tags = interaction.getScoreboardTags();

        if(interaction.getScoreboardTags().isEmpty()) return;

        String tag = (String) tags.toArray()[0];
        switch (tag) {
            case "techtree":
                player.openInventory(TechtreeInventories.getDefaultInventory());
                break;

            case "ship":
                if(planet == null) return;

                planet.teleportPlayerToSpace(player);


            case "vein":
                Machinery m = new MachineryAnnotationProcessor().getAsMachinery(player.getInventory().getItemInMainHand());

                if(m != null) {
                    if(m instanceof Drill) {
                        if(planet == null) return;

                        Location loc = player.getTargetBlockExact(20).getLocation();

                        new PasteSchem().generate(loc, m.getModel());
                        interaction.remove();

                        Interaction e = (Interaction) player.getWorld().spawnEntity(loc, EntityType.INTERACTION);
                        e.setInteractionWidth(3);
                        e.setInteractionHeight(3);
                        e.addScoreboardTag("basic_drill");

                        Machinery drill = new BasicDrill(Ressource.IRON, 0.1);

                        planet.addMachinery(drill);

                    }

                }


            }



    }




    /*
    @EventHandler
    public void playerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if(item == null) return;

        if(SurfaceObject.BASIC_FURNACE.isItemEqual(item)) {

        } else if (SurfaceObject.BASIC_DRILL.isItemEqual(item)) {
            SurfaceObject so = SurfaceObject.BASIC_DRILL;

            new PasteSchem().generate(player.getTargetBlockExact(20).getLocation(), so.getModel());

        }


    }
    */



    @EventHandler
    public void changeSlot(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        Planet planet = Game.getPlayerPlanet(player);

        if(planet == null) {
            return;
        }

        int size = planet.getSurfacePlanet().getSide()/2;

        Machinery surfaceObject = new MachineryAnnotationProcessor().getAsMachinery(player.getInventory().getItem(event.getNewSlot()));

        if(surfaceObject == null) {
            Collection<Entity> entities = player.getWorld().getNearbyEntities(new BoundingBox(-size, -size, -size, size, size, size));

            for(Entity e : entities) {
                if(e.getScoreboardTags().contains("vein")) {
                    e.remove();
                }
            }
            return;
        }

        if(surfaceObject instanceof Drill) {

            for(Set<Vector3i> s : planet.getSurfacePlanet().getOresPose().values()) {
                for(Vector3i v : s) {
                    Interaction e = (Interaction) player.getWorld().spawnEntity(new Location(player.getWorld(), v.x, v.y, v.z), EntityType.INTERACTION);

                    e.setInteractionHeight(4);
                    e.setInteractionWidth((float) 11 /2);
                    e.addScoreboardTag("vein");

                }
            }

        }

    }







    @EventHandler
    public void playerMoveEvent(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if(! player.getWorld().equals(Game.getUniversWorld())) {
            Planet planet = Game.getPlayerPlanet(player);
            Location loc = player.getLocation();

            int area = planet.getSurfacePlanet().getSide();

            if(loc.getX() > area) {
                loc.setX(-loc.getX()+5);
                player.teleport(loc);

            } else if (loc.getX() < -area) {
                loc.setX(-(loc.getX()+5));
                player.teleport(loc);

            } else if (loc.getZ() > area) {
                loc.setZ(-loc.getZ()+5);
                player.teleport(loc);

            } else if (loc.getZ() < -area) {
                loc.setZ(-(loc.getZ()+5));
                player.teleport(loc);

            }
        }


    }


}
