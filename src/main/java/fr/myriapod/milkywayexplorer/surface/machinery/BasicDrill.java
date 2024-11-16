package fr.myriapod.milkywayexplorer.surface.machinery;


import fr.myriapod.milkywayexplorer.surface.ressource.Generable;
import fr.myriapod.milkywayexplorer.surface.ressource.Iron;
import fr.myriapod.milkywayexplorer.techtree.Tech;
import org.bukkit.Material;
import org.joml.Vector3i;

public class BasicDrill extends Drill {


    public BasicDrill() {
        setupInfo();
    }

    public BasicDrill(Vector3i v, Generable r) {
        this.pos = v;
        setupInfo();
        setProduction(r);
        productionLoop();
    }

    public BasicDrill(Vector3i v) {
        this.pos = v;
        setupInfo();
        productionLoop();
    }

    void setupInfo() {
        this.name = "Foreuse Basique";
        this.material = Material.LIGHTNING_ROD;
        this.prerequis = Tech.AUTOMATISATION_ESSENTIALS;
        this.model = "basic_drill";
        this.modelData = 1001;
        this.description.add("Peut etre posé sur un sol exploitable");
        price.put(new Iron(), 20);
        this.prod = 0.1;

    }



}
