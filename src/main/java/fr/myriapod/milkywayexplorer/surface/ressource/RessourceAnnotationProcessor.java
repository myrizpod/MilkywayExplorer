package fr.myriapod.milkywayexplorer.surface.ressource;

import fr.myriapod.milkywayexplorer.surface.machinery.Machinery;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

public class RessourceAnnotationProcessor {

    private static final Set<Ressource> allRessources = new HashSet<>();
    private static final Set<Generable> allGenerables = new HashSet<>();


    public void process() {
        Reflections reflections = new Reflections("fr.myriapod.milkywayexplorer.surface.ressource", new SubTypesScanner(false));

        for (Class<? extends Ressource> m : reflections.getSubTypesOf(Ressource.class)) {
            if (Ressource.class.isAnnotationPresent(RessourceAnnotation.class)) {

                try {
                    if(! Modifier.isAbstract(m.getModifiers())) {
                        Ressource o = m.getDeclaredConstructor().newInstance();
                        allRessources.add(o);
                        if(o instanceof Generable) {
                            allGenerables.add((Generable) o);
                        }
                    }
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }


    public Set<Ressource> getIterator() {
        return allRessources;
    }

    public Generable[] getGenerableIterator() {
        int n = allGenerables.size();
        Generable arr[] = new Generable[n];

        int i = 0;
        for (Generable x : allGenerables)
            arr[i++] = x;

        return arr;
    }
}
