package fr.myriapod.milkywayexplorer.surface;

import de.articdive.jnoise.generators.noise_parameters.simplex_variants.Simplex2DVariant;
import de.articdive.jnoise.pipeline.JNoise;
import fr.myriapod.milkywayexplorer.surface.ressource.Generable;
import fr.myriapod.milkywayexplorer.tools.noises.ThisIsANoise;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.joml.Vector2i;

import java.util.*;

public class CustomPlanetGeneration extends ChunkGenerator {

    private ThisIsANoise noisePipeline;
    private double scale = 0.005;
    private ThisIsANoise colorPipeline;
    private Map<Generable, JNoise> oresPipeline = new HashMap<>();
    private final int TILE_WIDTH;
    private final int TILE_HEIGHT;
    private SurfaceTypes currentSurface;


    private Map<Generable, Set<Vector2i>> oresPose = new HashMap<>(); // Where Vector2i is chunk pos


    public CustomPlanetGeneration(int seed, int side, SurfaceTypes currentSurface, Generable[] ores) {
        TILE_WIDTH = side;
        TILE_HEIGHT = side;
        this.currentSurface = currentSurface;

        noisePipeline = currentSurface.setHeightmapNoise(seed);
        colorPipeline = currentSurface.setColorNoise(seed);
        for(Generable ore : ores) {
            oresPipeline.put(ore, JNoise.newBuilder().fastSimplex(seed + ore.getProduct().getModelData(), Simplex2DVariant.CLASSIC, null, null).addModifier(v -> (v + 1) / 2.0).clamp(0.0, 1.0).build());
            oresPose.computeIfAbsent(ore, k -> new HashSet<>());
        }

        calculateOrePos();
        
    }


    @Override
    public void generateNoise(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {

        for(int y = 0; y < 130 && y < chunkData.getMaxHeight(); y++) { //can go from y = chunkData.getMinHeight() (-65)
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {

                    double actualX = x + chunkX * 16;
                    double actualZ = z + chunkZ * 16;

                    /********* High and Color Noises *********/
                    // Sample noise at smaller intervals
                    double s = actualX / TILE_WIDTH;
                    double t = actualZ / TILE_HEIGHT;

                    // Calculate our 4D coordinates - I have no clue on how that works

                    double nx = 1 + Math.cos(s*2*Math.PI) * 1/(2*Math.PI) * TILE_WIDTH;
                    double ny = 1 + Math.cos(t*2*Math.PI) * 1/(2*Math.PI) * TILE_HEIGHT;
                    double nz = 1 + Math.sin(s*2*Math.PI) * 1/(2*Math.PI) * TILE_WIDTH;
                    double nw = 1 + Math.sin(t*2*Math.PI) * 1/(2*Math.PI) * TILE_HEIGHT;

                    double noise = noisePipeline.evaluateNoise(nx, ny, nz, nw);
                    double colorNoise = colorPipeline.evaluateNoise(nx, ny, nz, nw);

                    if (! (65 + (noisePipeline.getPower() * noise) < y)) { //65 = normal lvl, 20 = variations, noise can go from 0 to 1
                        if (colorNoise < 0.43) {
                            chunkData.setBlock(x, y, z, currentSurface.getMat3());
                        } else if (colorNoise > 0.57) {
                            chunkData.setBlock(x, y, z, currentSurface.getMat1());
                        } else {
                            chunkData.setBlock(x, y, z, currentSurface.getMat2());
                        }

                    }

                }
            }
        }

    }


    private void calculateOrePos() {
        /******** Ressource noises ********/
        for(int chunkZ = -TILE_HEIGHT/16; chunkZ < TILE_HEIGHT/16; chunkZ++) {
            for (int chunkX = -TILE_WIDTH/16; chunkX < TILE_WIDTH/16; chunkX++) {

                for (Generable ore : oresPipeline.keySet()) {
                    double oreNoise = oresPipeline.get(ore).evaluateNoise(chunkX, chunkZ);

                    if (oreNoise <= ore.getRarity()) {

                        Set<Vector2i> poses = new HashSet<>(oresPose.get(ore));

                        //si non alors ajouter à poses
                        poses.add(new Vector2i(chunkX, chunkZ));
                        oresPose.put(ore, poses);

                    }

                }
            }
        }
    }


    public Map<Generable, Set<Vector2i>> getOrePose() {
        return oresPose;
    }

}
