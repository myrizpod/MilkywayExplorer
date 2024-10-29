package fr.myriapod.milkywayexplorer.mytools;

import de.articdive.jnoise.core.util.vectors.Vector;
import de.articdive.jnoise.generators.noisegen.worley.WorleyNoiseGenerator;
import de.articdive.jnoise.generators.noisegen.worley.WorleyNoiseResult;
import de.articdive.jnoise.modules.octavation.fractal_functions.FractalFunction;
import de.articdive.jnoise.pipeline.JNoise;
import de.articdive.jnoise.pipeline.JNoiseDetailed;

import java.util.ArrayList;

public class VornoiNoise extends ThisIsANoise{

    private ArrayList<Object> modifiers;
    private double scale;
    private int octaves;
    private double gain;
    private double lacunarity;
    private FractalFunction frac;
    private JNoiseDetailed<WorleyNoiseResult<Vector>> activeBuilder;
    private int power;

    public VornoiNoise(double scale, int power, int octaves, double gain, double lacunarity, FractalFunction frac) {
        this.scale = scale;
        this.octaves = octaves;
        this.gain = gain;
        this.lacunarity = lacunarity;
        this.frac = frac;
        this.power = power;
    }

    @Override
    public void setBuilder(int seed) {
        activeBuilder = JNoise.newBuilder().worley(WorleyNoiseGenerator.newBuilder().setSeed(seed).build()).scale(scale).addModifier(v -> (v + 1) / 2.0).clamp(0.0, 1.0).buildDetailed();
    }

    @Override
    public double evaluateNoise(double x, double y, double z, double w) {
        WorleyNoiseResult<Vector> noiseResult = activeBuilder.evaluateNoiseResult(x, y, z, w);
        Vector closestPoint = noiseResult.getClosestPoint();
        return ((closestPoint.x() * closestPoint.y() * closestPoint.z()*10000)%10000)/10000;//10000 is just an arbitrary large value (to get detail)

    }

    @Override
    public int getPower() {
        return power;
    }
}

