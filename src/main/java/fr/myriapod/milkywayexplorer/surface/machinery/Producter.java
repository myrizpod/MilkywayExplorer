package fr.myriapod.milkywayexplorer.surface.machinery;

import fr.myriapod.milkywayexplorer.surface.ressource.Ressource;

import java.util.Map;

public interface Producter {

    void startProduction();

    void stopProduction();

    Map<Ressource, Integer> getProducted();

    void productionLoop();

    void addIncomes(Map<Ressource, Integer> producted);
}
