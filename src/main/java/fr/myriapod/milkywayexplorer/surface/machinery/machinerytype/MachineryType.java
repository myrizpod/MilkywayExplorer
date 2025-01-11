package fr.myriapod.milkywayexplorer.surface.machinery.machinerytype;

import fr.myriapod.milkywayexplorer.surface.machinery.Assembler;
import fr.myriapod.milkywayexplorer.surface.machinery.Crafter;
import fr.myriapod.milkywayexplorer.surface.machinery.Drill;
import fr.myriapod.milkywayexplorer.surface.machinery.Machinery;
import fr.myriapod.milkywayexplorer.surface.ressource.Ressource;
import fr.myriapod.milkywayexplorer.techtree.Tech;
import fr.myriapod.milkywayexplorer.tools.Tuple;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.joml.Vector3i;

import java.util.*;

public interface MachineryType {

    static Machinery createMachineryByID(String id, Vector3i pos) {
        for(AssemblerType a : AssemblerType.values()) {
            if(a.getID().equals(id)) {
                return new Assembler(a, pos);
            }
        }
        for(CrafterType c : CrafterType.values()) {
            if(c.getID().equals(id)) {
                return new Crafter(c, pos);
            }
        }
        for(DrillType d : DrillType.values()) {
            if(d.getID().equals(id)) {
                return new Drill(d, pos);
            }
        }

        return null;
    }


    static Set<MachineryType> getAllTypes() {
        Set<MachineryType> allTypes = new HashSet<>();

        allTypes.addAll(List.of(AssemblerType.values()));
        allTypes.addAll(List.of(CrafterType.values()));
        allTypes.addAll(List.of(DrillType.values()));

        return allTypes;
    }



    String getName();
    Material getMaterial();
    Tech getPrerequis();
    String getID();
    String getModel();
    int getModelData();
    List<String> getDescription();
    Map<Ressource, Integer> getPrice();
    ItemStack getAsItem();
    boolean isItemEqual(ItemStack item);


    class Production {

        Map<Ressource, Map<Ressource, Integer>> recipes = new HashMap<>();

        @SafeVarargs
        Production(Ressource result, Tuple<Ressource, Integer>... prices) {
            Map<Ressource, Integer> price = new HashMap<>();
            for(Tuple<Ressource, Integer> p : prices) {
                price.put(p.getA(), p.getB());
            }
            recipes.put(result, price);
        }

        public Map<Ressource, Map<Ressource, Integer>> getRecipes() {
            return recipes;
        }
    }

}
