package fr.myriapod.milkywayexplorer.surface.machinery;

import fr.myriapod.milkywayexplorer.surface.ressource.Ressource;
import fr.myriapod.milkywayexplorer.techtree.Tech;
import org.bukkit.Material;
import org.joml.Vector3i;

public class BasicConveyor extends Conveyor {

    public BasicConveyor() {
        setupInfo();
    }

    public BasicConveyor(Producter input, Producter output, Vector3i pos) {
        super(input, output);
        this.pos = pos;
        rate = 0.2;
        conveyorLoop();
    }

    @Override
    void setupInfo() {
        name = "Tapis Roulant Basique";
        material = Material.GRAY_CARPET;
        prerequis = Tech.AUTOMATISATION_ESSENTIALS;
        model = null;
        id = "basic_conveyor";
        modelData = 1001;
        description.add("Permet de transferer des ressources d'une machine à une autre");
        price.put(Ressource.IRON, 3);
    }
}
