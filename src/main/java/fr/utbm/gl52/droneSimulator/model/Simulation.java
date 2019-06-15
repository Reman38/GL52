package fr.utbm.gl52.droneSimulator.model;

import java.util.ArrayList;
import java.util.List;

public class Simulation {
    private static ArrayList<Drone> drones = new ArrayList<>();
    private static ArrayList<Parcel> parcels = new ArrayList<>();
    private static ArrayList<Area> areas = new ArrayList<>();
    private static List<ChargingStation> chargingStations = new ArrayList<>();
    private static MainArea mainArea;

    private static Boolean play = true;

    private static Integer time;
    private static Float speed;
    private static Integer parcelNumber;
    private static Integer droneNumber;
    private static  Integer chargingStation;
    private static Float[] droneWeightCapacity = new Float[2];
    private static Integer[] droneBatteryCapacity = new Integer[2];
    private static Integer[] simulationDurationRange = new Integer[2];
    private static Integer[] numberOfSimulationIterationRange = new Integer[2];
    private static Integer simulationDuration = simulationDurationRange[0];
    private static Integer numberOfSimulationIteration = numberOfSimulationIterationRange[0];


    private static final Float mainAreaWidth = 16000f;
    private static final Float mainAreaHeight = 9000f;

    public Simulation() {
        time = 0;
        setSpeed(10f); // pour voir les éléments se déplacer
        droneNumber = 1;
        parcelNumber = 10;
        chargingStation = 5;
        droneWeightCapacity[0] = 0.1f;
        droneWeightCapacity[1] = 20f;
        droneBatteryCapacity[0] = 5;
        droneBatteryCapacity[1] = 55;
        simulationDurationRange[0] = 20;
        simulationDurationRange[1] = 120;
        numberOfSimulationIterationRange[0] = 1;
        numberOfSimulationIterationRange[1] = 10;
    }

    public static void setSpeed(Float f) {
        speed = f; // TODO ajouter controle et exception
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
        //popAreas();
        //popParcels();
        //popDrones();
        //popChargingStations();
        //Thread simulationThread = new Thread(Simulation::update);
        //simulationThread.start();
    }

    private static void popParcels() {
        for (Integer i = 0; i < getParcelNumber(); ++i) {
            parcels.add(Parcel.createRandomized());
        }
    }

    private static void popAreas() {
        for (Integer i = 0; i < 1; ++i) {
            areas.add(Area.createRandomized());
        }
    }

    private static void popDrones() {
        for (Integer i = 0; i < getDroneNumber(); ++i) {
            drones.add(Drone.createRandomizedDrone());
        }
    }

    private static void popChargingStations(){
        for (Integer i = 0; i < getChargingStationNumber(); ++i) {
            chargingStations.add(ChargingStation.createRandomizedChargingStations());
        }
    }

    public static void update() {
        while (isPlay()) {
            // TODO ajust
            incrementTime();

            // TODO refactor
//            if (getTime() % 60 == 0){
//                System.out.println("ok");
//                parcels.add(Parcel.createRandomized());
//            }

            for (Drone drone : drones) {
                drone.handleParcelInteractions();
                drone.handleDroneInteractions();
                drone.move();
            }

            try {
                Thread.sleep((long) (1000 / 30 / getSpeed())); // TODO déplacer le drone en fonction du temps écoulé depuis le dernier rafraichissement du modèle
//              Thread.sleep(0, (int) (1000000000 / 30 / getSpeed()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setTimeSimulationParameters(Integer simulationDuration, Integer numberOfSimulationIteration){
        Simulation.simulationDuration = simulationDuration;
        Simulation.numberOfSimulationIteration = numberOfSimulationIteration;
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
    public static List<ChargingStation> getChargingStations() {
        return chargingStations;
    }
    public static Integer getChargingStationNumber() {
        return chargingStation;
    }
    public static void setChargingStationNumber(Integer chargingStation) {
        Simulation.chargingStation = chargingStation;
    }
    public static Float[] getDroneWeightCapacity() {
        return droneWeightCapacity;
    }
    public static Integer[] getDroneBatteryCapacity() {
        return droneBatteryCapacity;
    }
    public static Integer[] getSimulationDurationRange() {
        return simulationDurationRange;
    }
    public static Integer[] getNumberOfSimulationIterationRange() {
        return numberOfSimulationIterationRange;
    }
}