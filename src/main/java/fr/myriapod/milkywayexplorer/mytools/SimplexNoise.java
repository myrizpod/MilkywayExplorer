package fr.myriapod.milkywayexplorer.mytools;

import de.articdive.jnoise.generators.noise_parameters.simplex_variants.*;
import de.articdive.jnoise.modules.octavation.fractal_functions.FractalFunction;
import de.articdive.jnoise.pipeline.JNoise;

import java.util.ArrayList;

//This class is literally just a rewrite of the jnoise simplex to customize the seed
//may not need as many parameters IDK

public class SimplexNoise {

    private ArrayList<Object> modifiers;
    private Simplex2DVariant S2Dvar;
    private Simplex3DVariant S3Dvar;
    private Simplex4DVariant S4Dvar;
    private double scale;
    private int octaves;
    private double gain;
    private double lacunarity;
    private FractalFunction frac;

    public SimplexNoise(Simplex2DVariant S2Dvar, Simplex3DVariant S3Dvar, Simplex4DVariant S4Dvar, double scale, int octaves, double gain, double lacunarity, FractalFunction frac){
        this.S2Dvar = S2Dvar;
        this.S3Dvar = S3Dvar;
        this.S4Dvar = S4Dvar;
        this.scale = scale;
        this.octaves = octaves;
        this.gain = gain;
        this.lacunarity = lacunarity;
        this.frac = frac;
    }


    public JNoise getBuilder(int seed){
            return JNoise.newBuilder().fastSimplex(seed,S2Dvar,S3Dvar,S4Dvar).scale(scale).octavate(octaves,gain,lacunarity, FractalFunction.FBM,true).addModifier(v -> (v + 1) / 2.0).clamp(0.0, 1.0).build();
    }

}
