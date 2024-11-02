package fr.myriapod.milkywayexplorer.tools.noises;

import de.articdive.jnoise.generators.noisegen.worley.WorleyNoiseGenerator;
import de.articdive.jnoise.modules.octavation.fractal_functions.FractalFunction;
import de.articdive.jnoise.pipeline.JNoise;

import java.util.ArrayList;

public class WorleyNoise extends ThisIsANoise{

    private ArrayList<Object> modifiers;
    private double scale;
    private int octaves;
    private double gain;
    private double lacunarity;
    private FractalFunction frac;
    private JNoise activeBuilder;
    private int power;

    public WorleyNoise(double scale, int power, int octaves, double gain, double lacunarity, FractalFunction frac){
        this.scale = scale;
        this.octaves = octaves;
        this.gain = gain;
        this.lacunarity = lacunarity;
        this.frac = frac;
        this.power = power;
    }

    @Override
    public void setBuilder(int seed){
        activeBuilder = JNoise.newBuilder().worley(WorleyNoiseGenerator.newBuilder().setSeed(seed).build()).scale(scale).octavate(octaves,gain,lacunarity, FractalFunction.FBM,true).addModifier(v -> (v + 1) / 2.0).clamp(0.0, 1.0).build();
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
