package fr.myriapod.milkywayexplorer.surface;

import de.articdive.jnoise.generators.noise_parameters.simplex_variants.Simplex2DVariant;
import de.articdive.jnoise.generators.noise_parameters.simplex_variants.Simplex3DVariant;
import de.articdive.jnoise.generators.noise_parameters.simplex_variants.Simplex4DVariant;
import de.articdive.jnoise.modules.octavation.fractal_functions.FractalFunction;
import de.articdive.jnoise.pipeline.JNoise;
import org.bukkit.Material;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;

import java.util.Random;

public class CustomPlanetGeneration extends ChunkGenerator {

    private JNoise noisePipeline;
    private double scale = 0.005;
    private JNoise colorPipeline;


    public CustomPlanetGeneration(int seed) {
        noisePipeline = JNoise.newBuilder().fastSimplex(seed, Simplex2DVariant.IMPROVE_X, Simplex3DVariant.IMPROVE_XY, Simplex4DVariant.IMRPOVE_XYZ).scale(scale).octavate(3,1.2,2.0, FractalFunction.FBM,true).addModifier(v -> (v + 1) / 2.0).clamp(0.0, 1.0).build();
        colorPipeline = JNoise.newBuilder().fastSimplex(seed+1, Simplex2DVariant.IMPROVE_X, Simplex3DVariant.IMPROVE_XY, Simplex4DVariant.IMRPOVE_XYZ).scale(scale/2).octavate(4,1.2,4.0, FractalFunction.FBM,true).addModifier(v -> (v + 1) / 2.0).clamp(0.0, 1.0).build();

    }


    @Override
    public void generateNoise(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
//        int y = 30;
        //TODO make shit work https://www.ronja-tutorials.com/post/029-tiling-noise/
        for(int y = 0; y < 130 && y < chunkData.getMaxHeight(); y++) { //can go from y = chunkData.getMinHeight() (-65)
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    double noise = noisePipeline.evaluateNoise(x + (chunkX * 16), z + (chunkZ * 16));

                    if (! (65 + (10 * noise) < y)) { //65 = normal lvl, 10 = variations, noise can go from 0 to 1
                        if (colorPipeline.evaluateNoise(x + (chunkX * 16),z + (chunkZ * 16)) < 0.4) {
                            chunkData.setBlock(x, y, z, Material.RED_TERRACOTTA);
                        } else if (colorPipeline.evaluateNoise(x + (chunkX * 16),z + (chunkZ * 16)) > 0.6) {
                            chunkData.setBlock(x, y, z, Material.NETHERRACK);
                        } else {
                            chunkData.setBlock(x, y, z, Material.STRIPPED_MANGROVE_WOOD);
                        }

                    }


                }
            }
        }
    }
}
