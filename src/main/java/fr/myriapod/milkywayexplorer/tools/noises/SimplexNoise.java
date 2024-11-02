package fr.myriapod.milkywayexplorer.tools.noises;

import de.articdive.jnoise.generators.noise_parameters.simplex_variants.*;
import de.articdive.jnoise.modules.octavation.fractal_functions.FractalFunction;
import de.articdive.jnoise.pipeline.JNoise;

import java.util.ArrayList;

//This class is literally just a rewrite of the jnoise simplex to customize the seed
//may not need as many parameters IDK

public class SimplexNoise extends ThisIsANoise{

    private ArrayList<Object> modifiers;
    private Simplex2DVariant S2Dvar;
    private Simplex3DVariant S3Dvar;
    private Simplex4DVariant S4Dvar;
    private double scale;
    private int octaves;
    private double gain;
    private double lacunarity;
    private FractalFunction frac;
    private JNoise activeBuilder;
    private int power;

    public SimplexNoise(Simplex2DVariant S2Dvar, Simplex3DVariant S3Dvar, Simplex4DVariant S4Dvar, double scale, int power, int octaves, double gain, double lacunarity, FractalFunction frac){
        this.S2Dvar = S2Dvar;
        this.S3Dvar = S3Dvar;
        this.S4Dvar = S4Dvar;
        this.scale = scale;
        this.octaves = octaves;
        this.gain = gain;
        this.lacunarity = lacunarity;
        this.frac = frac;
        this.power = power;
    }

    @Override
    public void setBuilder(int seed){
        activeBuilder = JNoise.newBuilder().fastSimplex(seed,S2Dvar,S3Dvar,S4Dvar).scale(scale).octavate(octaves,gain,lacunarity, FractalFunction.FBM,true).addModifier(v -> (v + 1) / 2.0).clamp(0.0, 1.0).build();
    }

    @Override
    public double evaluateNoise(double x, double y, double z, double w) {
        return activeBuilder.evaluateNoise(x, y, z, w);
    }

    @Override
    public int getPower() {
        return power;
    }
}
