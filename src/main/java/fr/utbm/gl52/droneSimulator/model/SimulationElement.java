package fr.utbm.gl52.droneSimulator.model;

import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

public abstract class SimulationElement {
    // un tableau car si on est amené à faire un environnement 3D, on aura juste à manipuler cet attribut et pas en créer un autre
    // valeur par défault pour ne pas planter getX() et getY() // TODO indiquer la dimension et retourner 0 ou null serait mieux
    protected float[] coord = {Simulation.getWidth()/2, Simulation.getHeight()/2};

    protected String picture;
    public static Simulation simulation; // sert à délimiter les valeurs des coordonnées à la zone du simulation // TODO passer seulement les dimensions

    SimulationElement(){}

    // getteurs
    public float getX() {
        return coord[0];
    }
    public float getY() {
        return coord[1];
    }
    // nous laissons la possibilté de positionner un élément en dehors du simulation, et gérons les cas spécifiques en fonctions des entités manipulées (animal, nourriture, case du board, etc.)
    public void setX(float x) {
        coord[0] = x;
    }
    public void setY(float y) {
        coord[1] = y;
    }

    public static void setSimulation(Simulation v) {
        simulation = v;
    }
    public static Simulation getSimulation() {
        return simulation;
    }

    public String getPicture() {
        return picture;
    }

    public String toString() {
        String s =
            "coords: ["+getX()+","+getY()+"]"+System.getProperty("line.separator")
            +"picture: "+getPicture()+System.getProperty("line.separator")
        ;
//        Pan // pas besoin de l'afficher
        return s;
    }



    // TODO À DÉPLACER AILLEURS J'IMAGINE
    public static boolean getRandBool() {
        Boolean bool = false;
        if (getRandInt(0, 1)==1){
            bool = true;
        }
        return bool;
    }

    public static int getRandInt(int min, int max) {
        int nb = 0;
        try{
            nb = ThreadLocalRandom.current().nextInt(min, max + 1);
        }
        catch (Exception e){
        }
        return nb;
    }
    public static double getRandDouble(double min, double max) {
        double nb = 0;
        try{
            nb = ThreadLocalRandom.current().nextDouble(min, max + 1);
        }
        catch (Exception e){
        }
        return nb;
    }
    public static float getRandFloat(float min, float max) {
        float nb = 0;
        try{
            nb = min + (max - min) * (ThreadLocalRandom.current().nextFloat());
        }
        catch (Exception e){
        }
        return nb;
    }

    // TODO faire la même avec des string si besoin, ou une qui gèrerait une tableau/ une range de type générique
//    public static int getRandValueOf(int[] it) {
//        return it[getRandInt(0, it.length-1)];
//    }

    public static String getRandValueOf(String[] ts) {
        return ts[getRandInt(0, ts.length-1)];
    }
    public static String getRandStringOf(Vector<String> v) {
        return v.get(getRandInt(0, v.size()-1));
    }

    public double distanceCalcul(SimulationElement ge) {
        return Math.sqrt(Math.pow(distanceXCalcul(ge), 2) + Math.pow(distanceYCalcul(ge), 2));
    }
    public double distanceXCalcul(SimulationElement ge) {
        return Math.abs(getX() - ge.getX());
    }
    public double distanceYCalcul(SimulationElement ge) {
        return Math.abs(getY() - ge.getY());
    }

    public double distanceCalcul(float x, float y) {
        return Math.sqrt(Math.pow(distanceXCalcul(x), 2) + Math.pow(distanceYCalcul(y), 2));
    }
    private double distanceXCalcul(float x) {
        return Math.abs(getX() - x);
    }
    private double distanceYCalcul(float y) {
        return Math.abs(getY() - y);
    }

    public static Area getCase(double x, double y){
        Area areaReturn = null;
        for (Area area: Board.getAreas()){
            if (
                x >= area.getX() && x < area.getX()+area.getWidth()
                && y >= area.getY() && y < area.getY()+area.getHeight()
            ){
                areaReturn = area;
            }
        }
        return areaReturn;
    }

    public void setRandX(Area area) {
        setX(getRandFloat(area.getX()+getWidth() / 2,(area.getX() + area.getWidth())-getWidth() / 2));
    }
    public void setRandY(Area area) {
        setY(getRandFloat(area.getY()+getHeight() / 2, (area.getY() + area.getHeight())-getHeight() / 2));
    }
    public void setRandCoord(Area area){
        setRandX(area);
        setRandY(area);
    }

    public abstract float getWidth();
    public abstract float getHeight();

    /*
        Fonctions mathématiques
    */
    // Modulo
    protected float simplifyAngle(float angle) {
        // angle %= 2*Math.PI;
        while (angle > 2*Math.PI)
            angle -= 2*Math.PI;
        while (angle < 0)
            angle += 2*Math.PI;
        return angle;
    }

    // ajuste le calcul trigonométrique de l'angle en fonction de la position relative du second objet
    public float angleCalcul(SimulationElement ge) {
        return angleCalcul(ge.getX(), ge.getY());
    }
    public float angleCalcul(float x, float y) {
        float YmaY = getY() - y;
        float XmaX = getX() - x;

        float angle;
        if (XmaX == 0) {
            if (YmaY < 0)
                angle = (float) (Math.PI / 2);
            else
                angle = (float) (-Math.PI / 2);
        } else if (YmaY == 0) {
            if (XmaX < 0)
                angle = 0;
            else
                angle = (float) Math.PI;
        } else{
            angle = (float) Math.atan(distanceYCalcul(y) / distanceXCalcul(x));

            if (XmaX < 0 && YmaY < 0)
                angle = -angle;
            else if (XmaX > 0 && YmaY < 0)
                angle += Math.PI;
            else if (XmaX > 0 && YmaY > 0)
                angle = (float) (Math.PI - angle);
        }

        return simplifyAngle(angle);
    }

    public static float degreeToRadian(int degres) {
        return (float) Math.toRadians(degres);
    }
}
