package fr.myriapod.milkywayexplorer.techtree;

import fr.myriapod.milkywayexplorer.Ressource;
import fr.myriapod.milkywayexplorer.mytools.Tuple;

import java.util.*;

import org.bukkit.Material;

public enum Tech {
    AUTOMATISATION_ESSENTIALS("Essentiels de l'automatisation", Material.GRAY_CARPET, null, new Tuple<>(Ressource.IRON, 10), new Tuple<>(Ressource.WOOD, 10)),
    SMELTING("Smelting", Material.FURNACE, AUTOMATISATION_ESSENTIALS, new Tuple<>(Ressource.IRON, 10), new Tuple<>(Ressource.COPPER, 15)),
    COMPLEX_CRAFTING("Crafting", Material.CRAFTING_TABLE,AUTOMATISATION_ESSENTIALS, new Tuple<>(Ressource.IRON, 20)),
    ADVANCED_CRAFTNG("Crafting Avancé", Material.CRAFTER, COMPLEX_CRAFTING, new Tuple<>(Ressource.IRON, 10), new Tuple<>(Ressource.COPPER, 10), new Tuple<>(Ressource.SULFUR, 10));


    private final String name;
    private final Material material;
    private final Tech father;
    private final Map<Ressource, Integer> price = new HashMap<>();

    @SafeVarargs
    Tech(String name, Material mat, Tech father, Tuple<Ressource, Integer>... price) {
        this.name = name;
        material = mat;
        this.father = father;

        for(Tuple<Ressource, Integer> t : price) {
            this.price.put(t.getA(), t.getB());
        }

    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }

    public Tech getFather() {
        return father;
    }

    public Map<Ressource, Integer> getPrice() {
        return price;
    }

    public List<Tech> getSons() {
        List<Tech> sons = new ArrayList<>();

        for(Tech t : Tech.values()) {
            if(Objects.equals(t.father, this)) {
                sons.add(t);
            }
        }

        return sons;
    }


    public static List<Tech> getMajorBranches() {
        List<Tech> returned = new ArrayList<>();

        for(Tech t : Tech.values()) {
            if(t.father == null) {
                returned.add(t);
            }
        }

        return returned;
    }


}