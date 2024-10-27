package fr.myriapod.milkywayexplorer.surface;

import de.articdive.jnoise.generators.noise_parameters.simplex_variants.Simplex2DVariant;
import de.articdive.jnoise.generators.noise_parameters.simplex_variants.Simplex3DVariant;
import de.articdive.jnoise.generators.noise_parameters.simplex_variants.Simplex4DVariant;
import de.articdive.jnoise.modules.octavation.fractal_functions.FractalFunction;
import de.articdive.jnoise.pipeline.JNoise;
import fr.myriapod.milkywayexplorer.mytools.SimplexNoise;
import org.bukkit.Material;
import org.bukkit.inventory.RecipeChoice;

public enum SurfaceTypes {

    RED_DUNES(Material.NETHERRACK, Material.STRIPPED_MANGROVE_WOOD, Material.RED_TERRACOTTA,
            new SimplexNoise(Simplex2DVariant.IMPROVE_X, Simplex3DVariant.IMPROVE_XY, Simplex4DVariant.IMRPOVE_XYZ,0.005,3,1.2,2.0, FractalFunction.FBM),
            new SimplexNoise(Simplex2DVariant.IMPROVE_X, Simplex3DVariant.IMPROVE_XY, Simplex4DVariant.IMRPOVE_XYZ,0.0025,4,1.2,4.0, FractalFunction.FBM)),
    WHITE_STONES(Material.DIORITE, Material.CALCITE, Material.SMOOTH_QUARTZ,
            new SimplexNoise(Simplex2DVariant.IMPROVE_X, Simplex3DVariant.IMPROVE_XY, Simplex4DVariant.IMRPOVE_XYZ,0.005,3,1.2,2.0,FractalFunction.FBM),
            new SimplexNoise(Simplex2DVariant.IMPROVE_X, Simplex3DVariant.IMPROVE_XY, Simplex4DVariant.IMRPOVE_XYZ,0.0025,4,1.2,4.0, FractalFunction.FBM));


    //TODO make it so we can have as much material as we want (its ez but im lazy)
    private Material mat1;
    private Material mat2;
    private Material mat3;
    //would also be nice to choose multiple noises (but not necessary for now)
    private SimplexNoise heightmapNoise;
    private SimplexNoise colorNoise;

    SurfaceTypes(Material mat1, Material mat2, Material mat3, SimplexNoise heightmapNoise, SimplexNoise colorNoise) {
        this.mat1 = mat1;
        this.mat2 = mat2;
        this.mat3 = mat3;
        this.heightmapNoise = heightmapNoise;
        this.colorNoise = colorNoise;
    }
    public Material getMat1() {
        return mat1;
    }

    public Material getMat2() {
        return mat2;
    }

    public Material getMat3() {
        return mat3;
    }

    public JNoise getHeightmapNoise(int seed) {
        return heightmapNoise.getBuilder(seed);

    }

    public JNoise getColorNoise(int seed) {
        return colorNoise.getBuilder(seed);

    }
}
