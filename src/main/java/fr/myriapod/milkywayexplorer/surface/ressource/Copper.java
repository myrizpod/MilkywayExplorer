package fr.myriapod.milkywayexplorer.surface.ressource;

import org.bukkit.Material;

public class Copper extends Generable {

    public Copper() {
        setupInfo();
    }


    @Override
    void setupInfo() {
//        COPPER("Cuivre", Material.COPPER_INGOT, 102, new RessourceGenerable.GenParameters(0.25, null)),
        name = "Cuivre";
        material = Material.COPPER_INGOT;
        modelData = 102;
        modelName = null;
        rarity = 0.25;
    }
}
