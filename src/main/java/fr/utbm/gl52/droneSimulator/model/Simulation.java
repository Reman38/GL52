package fr.utbm.gl52.droneSimulator.model;

import java.util.ArrayList;

public class Simulation {
    private static ArrayList<Drone> drones = new ArrayList<>();
    private static ArrayList<Parcel> parcels = new ArrayList<>();
    private static ArrayList<Area> areas = new ArrayList<>();

    private static boolean play = true;

    private static int time;
    private static float speed; // assez petit pour un déplacement qui semble naturel (contigu et non sacadé)
    private static int parcelNumber;
    private static int droneNumber;

    private static final float mainAreaWidth = 160;
    private static final float mainAreaHeight = 90;

    //    private static Board board; // TODO terminer de refactor
    private static Area mainArea;

    public Simulation() {
        time = 0;
        speed = 17;
        droneNumber = 5;
        parcelNumber = 5;
    }

    public static float getSpeed() {
        return speed;
    }

    public static void setSpeed(float speed) {
        if (speed >= 1)
            Simulation.speed = speed;
    }

    public static void addNumberToSpeed(float nb) {
        setSpeed(getSpeed() + nb);
    }

    public static void addPercentageToSpeed(float nb) {
        setSpeed(getSpeed() + getSpeed() * nb);
    }

    // TODO refactor : width and height are defined by mainArea singleton ones
//    public static int getWidth() {
//        return width;
//    }
//    public static int getHeight() {
//        return height;
//    }
//    public static void setWidth(int width) {
//        Simulation.width = width;
//    }
//    public static void setHeight(int height) {
//        Simulation.height = height;
//    }

    public static void setDrones(ArrayList<Drone> drones) {
        Simulation.drones = drones;
    }

    public static void setParcels(ArrayList<Parcel> parcels) {
        Simulation.parcels = parcels;
    }

    public static void removeParcel(Parcel parcel) {
        parcels.remove(parcel);
    }

    public static void removeAllParcels() {
        parcels.clear();
    }

    public static void removeDrone(Drone drone) {
        drones.remove(drone);
    }

    public static void removeAllDrones() {
        drones.clear();
    }

    private static void setMainArea() {
        mainArea = new Area(0, 0, mainAreaWidth, mainAreaHeight);
    }

    public static void start() {
        setMainArea();
        popDrone();
        popParcel();
        update();
    }

    private static void popParcel() {
        for (int i = 1; i < getParcelNumber(); ++i) {
            parcels.add(Parcel.createRandomizedParcel());
        }
    }

    private static void popDrone() {
        for (int i = 1; i < getDroneNumber(); ++i) {
            drones.add(Drone.createRandomizedDrone());
        }
    }

    public static void update() {
        // interactions entre les entités du jeu
        while (isPlay()) {
            incrementTime();

            // TODO refactor plutot que ce commentaire (qui a l'air faux au passage)
            if (getTime() % 1020 == 0) // toutes les 60 secondes
                Parcel.createRandomizedParcel();

            // TODO reessayer avec foreach, fix thread conflit
            for (int i = 0; i < drones.size(); ++i) {
                Drone drone1 = drones.get(i);
                drone1.handleParcelsInteraction();
                drone1.handleDroneInteraction();
                drone1.move();
            }
        }

        try {
            Thread.sleep((int) getSpeed());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static void incrementTime() {
        setTime(getTime() + 1);
    }

    public static boolean isPlay() {
        return play;
    }

    public static void setPlay(boolean play) {
        Simulation.play = play;
    }

    public static int getTime() {
        return time;
    }

    public static void setTime(int time) {
        Simulation.time = time;
    }

    public static ArrayList<Drone> getDrones() {
        return drones;
    }

    public static int getDroneNumber() {
        return droneNumber;
    }

    public static void setDroneNumber(int animalNumber) {
        Simulation.droneNumber = animalNumber;
    }

    public static ArrayList<Parcel> getParcels() {
        return parcels;
    }

    public static int getParcelNumber() {
        return parcelNumber;
    }

    public static void setParcelNumber(int parcelNumber) {
        Simulation.parcelNumber = parcelNumber;
    }

    public static Area getMainArea() {
        return mainArea;
    }

    public static ArrayList<Area> getAreas() {
        return areas;
    }
}