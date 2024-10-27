package fr.myriapod.milkywayexplorer.surface.machinery;


import fr.myriapod.milkywayexplorer.Ressource;
import fr.myriapod.milkywayexplorer.techtree.Tech;
import org.bukkit.Material;

public class BasicDrill extends Drill {


    BasicDrill() {
        setupInfo();
    }

    public BasicDrill(Ressource r, double prod) {
        setupInfo();
        setProduction(r, prod);
    }

    void setupInfo() {
        this.name = "Foreuse Basique";
        this.material = Material.LIGHTNING_ROD;
        this.prerequis = Tech.AUTOMATISATION_ESSENTIALS;
        this.model = "basic_drill";
        this.modelData = 1001;
    }



}
