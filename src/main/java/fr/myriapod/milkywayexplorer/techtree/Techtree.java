package fr.myriapod.milkywayexplorer.techtree;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class Techtree {

    public static final String INVENTORY_NAME = ChatColor.RESET + "Techtree";


    private final List<Tech> techsUnlocked;

    public Techtree() {
        techsUnlocked = new ArrayList<>();
    }

    public Techtree(List<Tech> teches) {
        techsUnlocked = new ArrayList<>(teches);
    }

    public void unlockTech(Tech tech) {
        techsUnlocked.add(tech);
    }

    public boolean hasTech(Tech tech) {
        return techsUnlocked.contains(tech);
    }

    public List<Tech> getUnlocked() {
        return techsUnlocked;
    }
}
