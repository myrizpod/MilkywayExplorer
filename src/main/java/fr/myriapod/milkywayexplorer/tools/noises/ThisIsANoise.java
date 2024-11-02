package fr.myriapod.milkywayexplorer.tools.noises;

public abstract class ThisIsANoise {

    private int power = 1;


    public abstract void setBuilder(int seed);

    public double evaluateNoise(double x,double y,double z,double w){
        return 0;
    }

    public int getPower(){
        return power;
    }
}
