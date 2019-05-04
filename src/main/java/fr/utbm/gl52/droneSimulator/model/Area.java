package fr.utbm.gl52.droneSimulator.model;

import java.util.Vector;

public class Area extends SimulationElement {
    private String biome;
    //TODO envisager factoriser avec animal
    private float width;
    private float height;

    public Area(float x, float y, float caseWidth, float caseHeight, Area precedentXArea, Area precedentYArea) {
        setX(x);
        setY(y);
        setWidth(caseWidth);
        setHeight(caseHeight);

        setRandBiome(precedentXArea, precedentYArea);
    }

    public float getWidth() {
        return width;
    }
    public float getHeight() {
        return height;
    }

    public void setWidth(float w) {
        width = w;
    }
    public void setHeight(float h) {
        height = h;
    }

    public String getBiome() {
        return biome;
    }
    private void setBiome(String b) {
        biome = b;
    }
    private void setRandBiome(Area precedentXArea, Area precedentYArea) {
        if (getRandInt(1,100) > 5){
            if (getRandBool()){
                if (precedentXArea != null)
                    setBiome(precedentXArea.getBiome());
                else
                    setRandBiome();
            }
            else{
                if (precedentYArea != null)
                    setBiome(precedentYArea.getBiome());
                else
                    setRandBiome();
            }
        }
        else
            setRandBiome();
    }
    private void setRandBiome() {
        Vector<String> biomeAllowed = new Vector<String>();

        if (Board.isDirt())
            biomeAllowed.add("dirt");
        if (Board.isGrass())
            biomeAllowed.add("grass");
        if (Board.isWater())
            biomeAllowed.add("water");
        if (Board.isSand())
            biomeAllowed.add("sand");
        if (Board.isSnow())
            biomeAllowed.add("snow");
        if (Board.isGap())
            biomeAllowed.add("gap");

        setBiome(getRandStringOf(biomeAllowed));
    }

}
