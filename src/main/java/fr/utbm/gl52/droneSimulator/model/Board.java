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

    private static ArrayList<Case> cases = new ArrayList<Case>();

    public Board(){
        // création du board de jeu, le nombre de case est un multiple de la largeur et de la hauteur du modele pour avoir des cases carrés (raison purement esthétique)
        // 100 pour avoir des cases assez grande
        numberDivisionX = Vivarium.getWidth()/125;
        numberDivisionY = Vivarium.getHeight()/125;

        float caseWidth = (float) Math.ceil(getWidth()/numberDivisionX);
        float caseHeight = (float) Math.ceil(getHeight()/numberDivisionY);

        Case precedentXCase = null;
        Case precedentYCase = null;
        for (int i = 0; i < numberDivisionX; ++i) {
            for (int j = 0; j < numberDivisionY; ++j) {
                if (i>1)
                    precedentXCase = cases.get((i-1)*numberDivisionY+j-1);

                precedentYCase = new Case(i*caseWidth, j*caseHeight, (i==numberDivisionX-1)?(getWidth()-caseWidth*(numberDivisionX-1)):caseWidth, (j==numberDivisionY-1)?(getHeight()-caseHeight*(numberDivisionY-1)):caseHeight, precedentXCase, precedentYCase);
                cases.add(precedentYCase);
            }
        }
    }

    public static ArrayList<Case> getCases() {
        return cases;
    }

    public static boolean isGap() {
        return gap;
    }

    public float getWidth() {
        return Vivarium.getWidth();
    }

    public float getHeight() {
        return Vivarium.getHeight();
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
        for (Case c: getCases()){
            if (a.isApossibleBiome(c)){
                hasACompatibleBiome = true;
                break;
            }
        }

        return hasACompatibleBiome;
    }
}
