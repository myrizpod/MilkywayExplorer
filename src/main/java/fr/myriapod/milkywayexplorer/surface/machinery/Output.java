package fr.myriapod.milkywayexplorer.surface.machinery;

import fr.myriapod.milkywayexplorer.surface.ressource.Ressource;

import java.util.Map;

public interface Output {

    Map<Ressource, Integer> getProducted();

}
