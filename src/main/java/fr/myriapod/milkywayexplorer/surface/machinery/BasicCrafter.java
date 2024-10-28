package fr.myriapod.milkywayexplorer.surface.machinery;

import fr.myriapod.milkywayexplorer.Ressource;
import fr.myriapod.milkywayexplorer.techtree.Tech;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.joml.Vector3i;

public class BasicCrafter extends Crafter {


    BasicCrafter() {
        setupInfo();
    }

    public BasicCrafter(Vector3i pos) {
        this.pos = pos;
    }


    void setupInfo() {
        this.name = "Crafteur Basique";
        this.material = Material.CRAFTING_TABLE;
        this.prerequis = Tech.AUTOMATISATION_ESSENTIALS;
        this.model = "crafter";
        this.modelData = 1001;
        this.description.add(ChatColor.RESET + "Permet de creer les Machines de base");
        price.put(Ressource.IRON, 10);

        for(Machinery m : new MachineryAnnotationProcessor().getIterator()) {
            if(m.prerequis.equals(Tech.AUTOMATISATION_ESSENTIALS)) {
                products.add(m);
            }
        }

    }
}
