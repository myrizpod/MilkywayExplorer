package fr.myriapod.milkywayexplorer.surface.machinery;

import fr.myriapod.milkywayexplorer.surface.machinery.machinerytype.TechtreeType;
import fr.myriapod.milkywayexplorer.surface.ressource.Ressource;
import fr.myriapod.milkywayexplorer.techtree.Tech;
import org.bukkit.Material;
import org.joml.Vector3i;

public class TechtreeBlock extends Machinery {


    public TechtreeBlock(TechtreeType type, Vector3i pos) {
        super(type, pos);
        this.pos = pos;
        this.id = type.getID();
    }

}
