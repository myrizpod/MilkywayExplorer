package fr.myriapod.milkywayexplorer.surface.machinery.machinerytype;

import fr.myriapod.milkywayexplorer.surface.machinery.*;
import fr.myriapod.milkywayexplorer.surface.ressource.Ressource;
import fr.myriapod.milkywayexplorer.techtree.Tech;
import fr.myriapod.milkywayexplorer.tools.Tuple;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.joml.Vector3i;

import java.util.*;

public interface MachineryType {

    SchematicSetting SCHEMATIC_SETTING_ONE_BLOCK = new SchematicSetting(1.3f,1.3f);

    static Machinery createMachineryByID(String id, Vector3i pos) {
        for(TechtreeType t : TechtreeType.values()) {
            if(t.getID().equals(id)) {
                return new TechtreeBlock(t, pos);
            }
        }
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

        Bukkit.getLogger().info("Miss created machinery: " + id + "   " + pos);

        return null;
    }


    static Set<MachineryType> getAllTypes() {
        Set<MachineryType> allTypes = new HashSet<>();

        allTypes.addAll(List.of(AssemblerType.values()));
        allTypes.addAll(List.of(CrafterType.values()));
        allTypes.addAll(List.of(DrillType.values()));
        allTypes.addAll(List.of(TechtreeType.values()));
        allTypes.addAll(List.of(ConveyorType.values()));

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
    SchematicSetting getSchematicSetting();
    ItemStack getAsItem();
    boolean isItemEqual(ItemStack item);


    class Production {

        private Map<Ressource, Map<Ressource, Integer>> recipes = new HashMap<>();

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


    class SchematicSetting {

        private float width;
        private float height;

        public SchematicSetting(float width, float height) {
            this.width = width;
            this.height = height;
        }

        public float getWidth() {
            return width;
        }

        public float getHeight() {
            return height;
        }

    }

}
