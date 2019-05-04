package fr.utbm.gl52.droneSimulator.model;

import java.util.ArrayList;

public class Board extends SimulationElement {
    private int numberDivisionX;
    private int numberDivisionY;

    private static boolean dirt;
    private static boolean grass;
    private static boolean water;
    private static boolean snow;
    private static boolean sand;
    private static boolean gap;

    private static ArrayList<Area> areas = new ArrayList<Area>();

    public Board(){
        // création du board de jeu, le nombre de case est un multiple de la largeur et de la hauteur du modele pour avoir des areas carrés (raison purement esthétique)
        // 100 pour avoir des areas assez grande
        numberDivisionX = Simulation.getWidth()/125;
        numberDivisionY = Simulation.getHeight()/125;

        float caseWidth = (float) Math.ceil(getWidth()/numberDivisionX);
        float caseHeight = (float) Math.ceil(getHeight()/numberDivisionY);

        Area precedentXArea = null;
        Area precedentYArea = null;
        for (int i = 0; i < numberDivisionX; ++i) {
            for (int j = 0; j < numberDivisionY; ++j) {
                if (i>1)
                    precedentXArea = areas.get((i-1)*numberDivisionY+j-1);

                precedentYArea = new Area(i*caseWidth, j*caseHeight, (i==numberDivisionX-1)?(getWidth()-caseWidth*(numberDivisionX-1)):caseWidth, (j==numberDivisionY-1)?(getHeight()-caseHeight*(numberDivisionY-1)):caseHeight, precedentXArea, precedentYArea);
                areas.add(precedentYArea);
            }
        }
    }

    public static ArrayList<Area> getAreas() {
        return areas;
    }

    public static boolean isGap() {
        return gap;
    }

    public float getWidth() {
        return Simulation.getWidth();
    }

    public float getHeight() {
        return Simulation.getHeight();
    }

    public static boolean isDirt() {
        return dirt;
    }

    public static void setDirt(boolean d) {
        dirt = d;
    }

    public static boolean isGrass() {
        return grass;
    }

    public static void setGrass(boolean g) {
        grass = g;
    }

    public static boolean isWater() {
        return water;
    }

    public static void setWater(boolean w) {
        water = w;
    }

    public static boolean isSnow() {
        return snow;
    }

    public static void setSnow(boolean s) {
        snow = s;
    }

    public static boolean isSand() {
        return sand;
    }

    public static void setSand(boolean s) {
        sand = s;
    }

    public static boolean hasACompatibleBiome(Animal a) {
        Boolean hasACompatibleBiome = false;
        for (Area area: getAreas()){
            if (a.isApossibleBiome(area)){
                hasACompatibleBiome = true;
                break;
            }
        }

        return hasACompatibleBiome;
    }
}
