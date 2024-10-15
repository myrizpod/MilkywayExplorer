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
    private final double TILE_WIDTH;
    private final double TILE_HEIGHT;


    public CustomPlanetGeneration(int seed) {
        TILE_WIDTH = 100;
        TILE_HEIGHT = 100;

        noisePipeline = JNoise.newBuilder().fastSimplex(seed, Simplex2DVariant.IMPROVE_X, Simplex3DVariant.IMPROVE_XY, Simplex4DVariant.IMRPOVE_XYZ).scale(scale).octavate(3,1.2,2.0, FractalFunction.FBM,true).addModifier(v -> (v + 1) / 2.0).clamp(0.0, 1.0).build();
        colorPipeline = JNoise.newBuilder().fastSimplex(seed+1, Simplex2DVariant.IMPROVE_X, Simplex3DVariant.IMPROVE_XY, Simplex4DVariant.IMRPOVE_XYZ).scale(scale/2).octavate(4,1.2,4.0, FractalFunction.FBM,true).addModifier(v -> (v + 1) / 2.0).clamp(0.0, 1.0).build();

    }


    @Override
    public void generateNoise(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
//        int y = 30;
        //TODO make shit work (I think it works now but test cuz we never know)
        for(int y = 0; y < 130 && y < chunkData.getMaxHeight(); y++) { //can go from y = chunkData.getMinHeight() (-65)
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {


                    // Sample noise at smaller intervals
                    double actualX = x + chunkX * 16;
                    double actualZ = z + chunkZ * 16;
                    double s = actualX / TILE_WIDTH;
                    double t = actualZ / TILE_HEIGHT;

                    // Calculate our 4D coordinates - I have no clue on how that works

                    double nx = 1 + Math.cos(s*2*Math.PI) * 1/(2*Math.PI) * TILE_WIDTH;
                    double ny = 1 + Math.cos(t*2*Math.PI) * 1/(2*Math.PI) * TILE_HEIGHT;
                    double nz = 1 + Math.sin(s*2*Math.PI) * 1/(2*Math.PI) * TILE_WIDTH;
                    double nw = 1 + Math.sin(t*2*Math.PI) * 1/(2*Math.PI) * TILE_HEIGHT;

                    double noise = noisePipeline.evaluateNoise(nx, ny, nz, nw);
                    double colorNoise = colorPipeline.evaluateNoise(nx, ny, nz, nw);

                    if (! (65 + (20 * noise) < y)) { //65 = normal lvl, 10 = variations, noise can go from 0 to 1
                        if (colorNoise < 0.43) {
                            chunkData.setBlock(x, y, z, Material.RED_TERRACOTTA);
                        } else if (colorNoise > 0.57) {
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
