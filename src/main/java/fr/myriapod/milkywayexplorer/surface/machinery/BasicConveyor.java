package fr.myriapod.milkywayexplorer.surface.machinery;

import fr.myriapod.milkywayexplorer.surface.ressource.Iron;
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
        conveyorLoop();
    }

    @Override
    void setupInfo() {
        name = "Tapis Roulant Basique";
        material = Material.GRAY_CARPET;
        prerequis = Tech.AUTOMATISATION_ESSENTIALS;
        model = "basic_conveyor";
        modelData = 1001;
        description.add("Permet de transferer des ressources d'une machine à une autre");
        price.put(new Iron(), 3);
    }
}
