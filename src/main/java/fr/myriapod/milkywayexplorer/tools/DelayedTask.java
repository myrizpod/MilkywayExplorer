package fr.myriapod.milkywayexplorer.tools;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class DelayedTask  {

    private static Plugin plugin = null;
    private int id = -1;

    public DelayedTask(Plugin plugin) {
        DelayedTask.plugin = plugin;
    }

    public DelayedTask(Runnable runnable, long delay) {
        if(plugin.isEnabled()) {
            id = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, runnable, delay);
        } else {
            runnable.run();
        }
    }

}
