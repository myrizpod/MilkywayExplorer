package fr.myriapod.milkywayexplorer.surface.machinery;


import fr.myriapod.milkywayexplorer.techtree.Tech;
import org.bukkit.Material;

public class BasicDrill extends Machinery implements Drill {


    BasicDrill() {
        this.name = "Foreuse Basique";
        this.material = Material.LIGHTNING_ROD;
        this.prerequis = Tech.AUTOMATISATION_ESSENTIALS;
        this.model = "basic_drill";
        this.modelData = 1001;
    }
}
