package fr.myriapod.milkywayexplorer.surface.ressource;

import org.bukkit.Material;

public class Coal extends Generable {

    public Coal() {
        setupInfo();
    }


    @Override
    void setupInfo() {
//        WOOD("Bois", Material.OAK_WOOD, 100, new RessourceGenerable.GenParameters(0.25, null)),
        name = "Charbon";
        material = Material.COAL;
        modelData = 100;
        modelName = null;
        rarity = 0.25;
    }
}
