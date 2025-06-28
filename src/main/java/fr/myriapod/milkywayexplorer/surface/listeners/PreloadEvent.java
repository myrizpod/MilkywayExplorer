package fr.myriapod.milkywayexplorer.surface.listeners;

import fr.myriapod.milkywayexplorer.Game;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.EntitiesLoadEvent;

import java.util.HashSet;
import java.util.Set;

public class PreloadEvent implements Listener {

    public static Set<Chunk> loadedChunk = new HashSet<>();
    public static Set<Chunk> loadedEntitiesChunk = new HashSet<>();

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
