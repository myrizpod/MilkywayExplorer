package fr.myriapod.milkywayexplorer.surface.machinery;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

public class MachineryAnnotationProcessor {

    private static final Set<Machinery> allMachines = new HashSet<>();


    public void process() {
        Reflections reflections = new Reflections("fr.myriapod.milkywayexplorer.surface.machinery", new SubTypesScanner(false));

        for (Class<? extends Machinery> m : reflections.getSubTypesOf(Machinery.class)) {
            Bukkit.getLogger().info("Element: " + m.getSimpleName());
            Bukkit.getLogger().info("annoted ? " + Machinery.class.isAnnotationPresent(MachineryAnnotation.class));
            if (Machinery.class.isAnnotationPresent(MachineryAnnotation.class)) {

                try {
                    Machinery o = m.getDeclaredConstructor().newInstance();
                    allMachines.add(o);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }


    public Machinery getAsMachinery(ItemStack item) {
        for(Machinery m : allMachines) {
            Bukkit.getLogger().info("macjeh " + m.name);

            if(m.isItemEqual(item)) {
                return m;
            }
        }
        return null;
    }



}
