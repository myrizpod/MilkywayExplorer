package fr.myriapod.milkywayexplorer.surface.machinery;

import fr.myriapod.milkywayexplorer.Ressource;

import java.util.HashMap;
import java.util.Map;

public class Drill extends Machinery {

    Map<Ressource, Double> production = new HashMap<>();


    public void setProduction(Ressource r, double prod) {
        production.put(r, prod);
    }






}
