package fr.myriapod.milkywayexplorer.techtree;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class Techtree {

    public static final String INVENTORY_NAME = ChatColor.GOLD + "Techtree";


    private final List<Tech> techsUnlocked;

    public Techtree() {
        techsUnlocked = new ArrayList<>();
    }

    public void unlockTech(Tech tech) {
        techsUnlocked.add(tech);
    }

}
