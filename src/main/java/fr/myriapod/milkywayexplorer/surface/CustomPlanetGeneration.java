package fr.myriapod.milkywayexplorer.surface;

import de.articdive.jnoise.core.api.functions.Interpolation;
import de.articdive.jnoise.generators.noise_parameters.fade_functions.FadeFunction;
import de.articdive.jnoise.modules.octavation.fractal_functions.FractalFunction;
import de.articdive.jnoise.pipeline.JNoise;
import org.bukkit.Material;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;

import java.util.Random;

public class CustomPlanetGeneration extends ChunkGenerator {

    private JNoise noisePipeline;
    private double scale = 0.01;


    public CustomPlanetGeneration(int seed) {
        noisePipeline = JNoise.newBuilder().perlin(seed, Interpolation.COSINE, FadeFunction.QUINTIC_POLY).scale(scale).octavate(3,1.2,1.5, FractalFunction.FBM,false).build();


    }


    @Override
    public void generateNoise(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
//        int y = 30;
        for(int y = 0; y < 130 && y < chunkData.getMaxHeight(); y++) { //can go from y = chunkData.getMinHeight() (-65)
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    double noise = noisePipeline.evaluateNoise(x + (chunkX * 16), z + (chunkZ * 16));

                    if (! (65 + (10 * noise) < y)) { //65 = normal lvl, 10 = variations, noise can go from -1 to 1
                        chunkData.setBlock(x, y, z, Material.STONE);
                    }


                }
            }
        }
    }
}
