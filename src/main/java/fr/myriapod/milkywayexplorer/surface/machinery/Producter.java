package fr.myriapod.milkywayexplorer.surface.machinery;

import fr.myriapod.milkywayexplorer.surface.ressource.Ressource;
import fr.myriapod.milkywayexplorer.tools.Tuple;

import java.util.Map;

public interface Producter extends Output {

    void startProduction();

    void stopProduction();

    Map<Ressource, Integer> getProducted();
    Tuple<Ressource, Integer> getProducted(Ressource r, int nb);

    void productionLoop();

}
