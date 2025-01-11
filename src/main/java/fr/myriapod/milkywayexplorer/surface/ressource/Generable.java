package fr.myriapod.milkywayexplorer.surface.ressource;

public enum Generable {

    COAL(null, 0.25, Ressource.COAL),
    COPPER(null, 0.25, Ressource.COPPER),
    IRON("iron_ore", 0.25, Ressource.IRON);

    String modelName;
    double rarity;
    Ressource product;

    Generable(String modelName, double rarity, Ressource ressource) {
        this.modelName = modelName;
        this.rarity = rarity;
        this.product = ressource;

    }

    public static Generable nameToRessource(String s) {
        for(Generable r : Generable.values()) {
            if(r.getModelName().equals(s)) {
                return r;
            }
        }
        return null;
    }

    public String getModelName() {
        return modelName;
    }

    public double getRarity() {
        return rarity;
    }

    public Ressource getProduct() {
        return product;
    }
}
