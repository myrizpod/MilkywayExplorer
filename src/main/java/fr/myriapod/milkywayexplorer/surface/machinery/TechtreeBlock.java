package fr.myriapod.milkywayexplorer.surface.machinery;

import fr.myriapod.milkywayexplorer.surface.ressource.Ressource;
import fr.myriapod.milkywayexplorer.techtree.Tech;
import org.bukkit.Material;
import org.joml.Vector3i;

public class TechtreeBlock extends Machinery {

    public TechtreeBlock() {
        setupInfo();
    }

    public TechtreeBlock(Vector3i pos) {
        this.pos = pos;
        setupInfo();
    }

    void setupInfo() {
        name = "Arbre De Competence";
        material = Material.FLETCHING_TABLE;
        prerequis = Tech.AUTOMATISATION_ESSENTIALS;
        model = null;
        id = "techtree";
        modelData = 1001;
        description.add("Permet d'ouvrir l'arbre de competence");
        price.put(Ressource.IRON, 5);
    }
}
