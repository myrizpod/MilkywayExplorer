package fr.myriapod.milkywayexplorer.surface;

import de.articdive.jnoise.generators.noise_parameters.simplex_variants.Simplex2DVariant;
import de.articdive.jnoise.generators.noise_parameters.simplex_variants.Simplex3DVariant;
import de.articdive.jnoise.generators.noise_parameters.simplex_variants.Simplex4DVariant;
import de.articdive.jnoise.modules.octavation.fractal_functions.FractalFunction;
import fr.myriapod.milkywayexplorer.tools.noises.SimplexNoise;
import fr.myriapod.milkywayexplorer.tools.noises.ThisIsANoise;
import fr.myriapod.milkywayexplorer.tools.noises.VornoiNoise;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum SurfaceTypes {

    RED_DUNES(new SimplexNoise(Simplex2DVariant.IMPROVE_X, Simplex3DVariant.IMPROVE_XY, Simplex4DVariant.IMRPOVE_XYZ,0.005, 20,3,1.2,2.0, FractalFunction.FBM),
            new SimplexNoise(Simplex2DVariant.IMPROVE_X, Simplex3DVariant.IMPROVE_XY, Simplex4DVariant.IMRPOVE_XYZ,0.0025,20,4,1.2,4.0, FractalFunction.FBM),
            Material.NETHERRACK, Material.STRIPPED_MANGROVE_WOOD, Material.RED_TERRACOTTA),
    WHITE_STONES(new VornoiNoise(0.1, 10,5,1.2,4.0,FractalFunction.FBM),
            new SimplexNoise(Simplex2DVariant.IMPROVE_X, Simplex3DVariant.IMPROVE_XY, Simplex4DVariant.IMRPOVE_XYZ,0.0025,20,4,1.2,4.0, FractalFunction.FBM),
            Material.DIORITE, Material.CALCITE, Material.SMOOTH_QUARTZ
            );

    private List<Material> materials = new ArrayList<>();
    //would also be nice to choose multiple noises (but not necessary for now)
    private ThisIsANoise heightmapNoise;
    private ThisIsANoise colorNoise; //NOTE power is useless for color, only affects heightmap

    SurfaceTypes(ThisIsANoise heightmapNoise, ThisIsANoise colorNoise, Material... materials) {
        this.heightmapNoise = heightmapNoise;
        this.colorNoise = colorNoise;

        this.materials.addAll(Arrays.asList(materials));

    }
    public Material getMat1() {
        return materials.get(0);
    }

    public Material getMat2() {
        return materials.get(1);
    }

    public Material getMat3() {
        return materials.get(2);
    }

    public ThisIsANoise setHeightmapNoise(int seed) {
        heightmapNoise.setBuilder(seed);
        return heightmapNoise;

    }

    public ThisIsANoise setColorNoise(int seed) {
        colorNoise.setBuilder(seed);
        return colorNoise;

    }
}
