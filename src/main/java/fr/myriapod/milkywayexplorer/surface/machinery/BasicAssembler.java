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
        pos = v;
        setupInfo();
        startProduction();
        productionLoop();
    }

    void setupInfo() {
        name = "Assembleur Basique";
        material = Material.FURNACE;
        prerequis = Tech.AUTOMATISATION_ESSENTIALS;
        id = "basic_assembler";
        model = null;
        modelData = 1001;
        description.add("Permet de produire des ressources");
        price.put(new Iron().getNormalized(), 35);
        Map<Ressource, Integer> m = new HashMap<>();
        m.put(new Iron(), 40);
        recipes.put(new IronBar(), m);
        prod = 0.1;

    }

}
