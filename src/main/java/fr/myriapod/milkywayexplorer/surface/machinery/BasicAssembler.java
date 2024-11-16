package fr.myriapod.milkywayexplorer.surface.machinery;

import fr.myriapod.milkywayexplorer.surface.ressource.Generable;
import fr.myriapod.milkywayexplorer.surface.ressource.Iron;
import fr.myriapod.milkywayexplorer.surface.ressource.IronBar;
import fr.myriapod.milkywayexplorer.surface.ressource.Ressource;
import fr.myriapod.milkywayexplorer.techtree.Tech;
import org.bukkit.Material;
import org.joml.Vector3i;

import java.util.HashMap;
import java.util.Map;

public class BasicAssembler extends Assembler {


    public BasicAssembler() {
        setupInfo();
    }

    public BasicAssembler(Vector3i v) {
        this.pos = v;
        setupInfo();
        productionLoop();
    }

    void setupInfo() {
        this.name = "Assembleur Basique";
        this.material = Material.FURNACE;
        this.prerequis = Tech.AUTOMATISATION_ESSENTIALS;
        this.model = "basic_assembler";
        this.modelData = 1001;
        this.description.add("Permet de produire des ressources");
        price.put(new Iron(), 35);
        Map<Ressource, Integer> m = new HashMap<>();
        m.put(new Iron(), 40);
        recipes.put(new IronBar(), m);
        this.prod = 0.1;

    }

}
