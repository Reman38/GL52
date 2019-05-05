package fr.utbm.gl52.droneSimulator.model;

import java.util.ArrayList;

public class Simulation {
    private static ArrayList<Drone> drones = new ArrayList<>();
    private static ArrayList<Parcel> parcels = new ArrayList<>();
    private static ArrayList<Area> areas = new ArrayList<>();
    private static MainArea mainArea;

    private static Boolean play = true;

    private static Integer time;
    private static Float speed;
    private static Integer parcelNumber;
    private static Integer droneNumber;

    private static final Float mainAreaWidth = 16f;
    private static final Float mainAreaHeight = 9f;

    public Simulation() {
        time = 0;
        speed = 17f; // assez petit pour un déplacement qui semble naturel (contigu et non sacadé)
        droneNumber = 5;
        parcelNumber = 5;
    }

    public static void setSpeed(Float f) {
        if (speed >= 1)
            speed = f;
    }

    public static void addNumberToSpeed(Float nb) {
        setSpeed(getSpeed() + nb);
    }

    public static void addPercentageToSpeed(Float nb) {
        setSpeed(getSpeed() * (1 + nb));
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

    private static void initMainArea() {
        mainArea = new MainArea(0f, 0f, mainAreaWidth, mainAreaHeight);
    }

    public static void start() {
        initMainArea();
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

            for (Drone drone : drones) {
                drone.handleParcelInteractions();
                drone.handleDroneInteractions();
                drone.move();
            }
        }

        try {
            Thread.sleep(17); // TODO replace with getSpeed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void incrementTime() {
        setTime(getTime() + 1);
    }

    /* getteurs et setteurs triviaux */
    public static Boolean isPlay() {
        return play;
    }
    public static Float getSpeed() {
        return speed;
    }
    public static Integer getTime() {
        return time;
    }
    public static ArrayList<Drone> getDrones() {
        return drones;
    }
    public static Integer getDroneNumber() {
        return droneNumber;
    }
    public static ArrayList<Parcel> getParcels() {
        return parcels;
    }
    public static Integer getParcelNumber() {
        return parcelNumber;
    }
    public static Area getMainArea() {
        return mainArea;
    }
    public static ArrayList<Area> getAreas() {
        return areas;
    }
    public static void setPlay(Boolean b) {
        play = b;
    }
    public static void setTime(Integer i) {
        time = i;
    }
    public static void setDroneNumber(Integer i) {
        droneNumber = i;
    }
    public static void setParcelNumber(Integer i) {
        parcelNumber = i;
    }
    public static void setDrones(ArrayList<Drone> drones) {
        drones = drones;
    }
    public static void setParcels(ArrayList<Parcel> parcels) {
        parcels = parcels;
    }
}