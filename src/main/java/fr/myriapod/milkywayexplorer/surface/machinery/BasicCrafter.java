package fr.myriapod.milkywayexplorer.surface.machinery;

import fr.myriapod.milkywayexplorer.surface.ressource.Iron;
import fr.myriapod.milkywayexplorer.techtree.Tech;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.joml.Vector3i;

public class BasicCrafter extends Crafter {


    public BasicCrafter() {
        setupInfo();
    }

    public BasicCrafter(Vector3i pos) {
        this.pos = pos;
        setupInfo();
    }


    void setupInfo() {
        name = "Crafteur Basique";
        material = Material.CRAFTING_TABLE;
        prerequis = Tech.AUTOMATISATION_ESSENTIALS;
        model = null;
        id = "crafter";
        modelData = 1001;
        description.add(ChatColor.RESET + "Permet de creer les Machines de base");
        price.put(new Iron().getNormalized(), 10);

        for(Machinery m : new MachineryAnnotationProcessor().getIterator()) {
            if(m.prerequis.equals(Tech.AUTOMATISATION_ESSENTIALS)) {
                products.add(m);
            }
        }

    }
}
