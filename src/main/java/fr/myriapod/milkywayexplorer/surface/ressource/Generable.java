package fr.myriapod.milkywayexplorer.surface.ressource;

public enum Generable {

    COAL("coal_ore", 0.25, Ressource.COAL),
    COPPER("copper_ore", 0.25, Ressource.COPPER),
    GOLD("gold_ore", 0.25, Ressource.GOLD),
    IRON("iron_ore", 0.25, Ressource.IRON);

    final String modelName;
    final double rarity;
    final Ressource product;

    Generable(String modelName, double rarity, Ressource ressource) {
        this.modelName = modelName;
        this.rarity = rarity;
        this.product = ressource;

    }

    public static Generable nameToRessource(String s) {
        if(s == null) {
            return null;
        }
        for(Generable r : Generable.values()) {
            if(s.equals(r.modelName)) {
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
