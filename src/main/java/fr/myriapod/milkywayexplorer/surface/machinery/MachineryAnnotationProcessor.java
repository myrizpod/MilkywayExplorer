package fr.myriapod.milkywayexplorer.surface.machinery;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

public class MachineryAnnotationProcessor {

    private static final Set<Machinery> allMachines = new HashSet<>();


    public void process() {
        Reflections reflections = new Reflections("fr.myriapod.milkywayexplorer.surface.machinery", new SubTypesScanner(false));

        for (Class<? extends Machinery> m : reflections.getSubTypesOf(Machinery.class)) {
            if (Machinery.class.isAnnotationPresent(MachineryAnnotation.class)) {

                try {
                    if(! Modifier.isAbstract(m.getModifiers())) {
                        Machinery o = m.getDeclaredConstructor().newInstance();
                        allMachines.add(o);
                    }
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }


    public Set<Machinery> getIterator() {
        return allMachines;
    }
}
