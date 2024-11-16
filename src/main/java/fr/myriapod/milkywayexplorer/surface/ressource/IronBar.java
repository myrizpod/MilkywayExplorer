package fr.myriapod.milkywayexplorer.surface.ressource;

import org.bukkit.Material;

public class IronBar extends Ressource {

    public IronBar() {
        setupInfo();
    }

    @Override
    void setupInfo() {
        name = "Bar de Fer";
        material = Material.IRON_BARS;
        modelData = 110;
    }
}
