package fr.myriapod.milkywayexplorer.surface.ressource;

import org.bukkit.Material;

public class Iron extends Generable {

    public Iron() {
        setupInfo();
    }


    @Override
    void setupInfo() {
        name = "Fer";
        material = Material.IRON_INGOT;
        modelData = 101;
        modelName = "iron_ore";
        rarity = 0.25;
    }
}
