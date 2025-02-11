package fr.myriapod.milkywayexplorer.surface.machinery;

import fr.myriapod.milkywayexplorer.surface.ressource.Ressource;

import java.util.Map;

public interface Input {

    void addIncomes(Map<Ressource, Integer> prod);

}
