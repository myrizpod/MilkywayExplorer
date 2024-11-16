package fr.myriapod.milkywayexplorer.surface.machinery;

import fr.myriapod.milkywayexplorer.surface.ressource.Iron;
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
        this.name = "Arbre De Competence";
        this.material = Material.FLETCHING_TABLE;
        this.prerequis = Tech.AUTOMATISATION_ESSENTIALS;
        this.model = "techtree";
        this.modelData = 1001;
        this.description.add("Permet d'ouvrir l'arbre de competence");
        price.put(new Iron(), 5);
    }
}
