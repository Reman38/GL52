package fr.utbm.gl52.droneSimulator.model;

import java.util.ArrayList;

public class Simulation {
    private static ArrayList<Drone> drones = new ArrayList<>();
    private static ArrayList<Parcel> parcels = new ArrayList<>();
    private static ArrayList<Area> areas = new ArrayList<>();
    private static MainArea mainArea;

    private static Boolean play = true;

    private static Integer time;
    private static Float speed; // assez petit pour un déplacement qui semble naturel (contigu et non sacadé)
    private static Integer parcelNumber;
    private static Integer droneNumber;

    private static final Float mainAreaWidth = 160f;
    private static final Float mainAreaHeight = 90f;

    public Simulation() {
        time = 0;
        speed = 17;
        droneNumber = 5;
        parcelNumber = 5;
    }

    public static Float getSpeed() {
        return speed;
    }

    public static void setSpeed(Float speed) {
        if (speed >= 1)
            Simulation.speed = speed;
    }

    public static void addNumberToSpeed(Float nb) {
        setSpeed(getSpeed() + nb);
    }

    public static void addPercentageToSpeed(Float nb) {
        setSpeed(getSpeed() + getSpeed() * nb);
    }

    // TODO refactor : width and height are defined by mainArea singleton ones
//    public static Integer getWidth() {
//        return width;
//    }
//    public static Integer getHeight() {
//        return height;
//    }
//    public static void setWidth(Integer width) {
//        Simulation.width = width;
//    }
//    public static void setHeight(Integer height) {
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
        mainArea = new MainArea(0f, 0f, mainAreaWidth, mainAreaHeight);
    }

    public static void start() {
        setMainArea();
        popDrone();
        popParcel();
//        update();
    }

    private static void popParcel() {
        for (Integer i = 1; i < getParcelNumber(); ++i) {
            parcels.add(Parcel.createRandomized());
        }
    }

    private static void popDrone() {
        for (Integer i = 1; i < getDroneNumber(); ++i) {
            drones.add(Drone.createRandomizedDrone());
        }
    }

    public static void update() {
        // interactions entre les entités du jeu
        while (isPlay()) {
            incrementTime();

            // TODO refactor plutot que ce commentaire (qui a l'air faux au passage)
            if (getTime() % 1020 == 0) // toutes les 60 secondes
                Parcel.createRandomized();

            // TODO reessayer avec foreach, fix thread conflit
            for (Integer i = 0; i < drones.size(); ++i) {
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

    public static Boolean isPlay() {
        return play;
    }

    public static void setPlay(Boolean play) {
        Simulation.play = play;
    }

    public static Integer getTime() {
        return time;
    }

    public static void setTime(Integer time) {
        Simulation.time = time;
    }

    public static ArrayList<Drone> getDrones() {
        return drones;
    }

    public static Integer getDroneNumber() {
        return droneNumber;
    }

    public static void setDroneNumber(Integer animalNumber) {
        Simulation.droneNumber = animalNumber;
    }

    public static ArrayList<Parcel> getParcels() {
        return parcels;
    }

    public static Integer getParcelNumber() {
        return parcelNumber;
    }

    public static void setParcelNumber(Integer parcelNumber) {
        Simulation.parcelNumber = parcelNumber;
    }

    public static Area getMainArea() {
        return mainArea;
    }

    public static ArrayList<Area> getAreas() {
        return areas;
    }
}