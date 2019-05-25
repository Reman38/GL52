package fr.utbm.gl52.droneSimulator.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Simulation {
    public static final long secondsInAMinute = 60L;
    public static final long millisecondsInASecond = 1000L;
    private static ArrayList<Drone> drones = new ArrayList<>();
    private static ArrayList<Thread> droneThreads = new ArrayList<>();
    private static ArrayList<Parcel> parcels = new ArrayList<>();
    private static ArrayList<Area> areas = new ArrayList<>();
    private static List<ChargingStation> chargingStations = new ArrayList<>();
    private static MainArea mainArea;

    private static Boolean play = true;

    private static Integer time;
    private static Float simulationSpeed;
    private static Integer parcelNumber;
    private static Integer droneNumber;
    private static  Integer chargingStation;
    private static Float competitionDifficulty;
    private static Float[] droneWeightCapacity = new Float[2];
    private static Integer[] droneBatteryCapacity = new Integer[2];
    private static Integer[] simulationDurationRange = new Integer[2];
    private static Integer[] numberOfSimulationIterationRange = new Integer[2];
    private static Integer simulationDuration = 240;
    private static Integer numberOfSimulationIteration = numberOfSimulationIterationRange[0];
    private static Map<String, Float> competitionDifficultyLevels = new HashMap<>();


    private static final Float mainAreaWidth = 1600f;
    private static final Float mainAreaHeight = 900f;

    public static final String DEFAULT = "DEFAULT";
    public static final String RANDOM = "RANDOM";
    public static final String CUSTOM = "CUSTOM";

    private static Long t1 = System.nanoTime();
    private static Long t2 = System.nanoTime();
    private static Long deltaTSimStep = 0L;

    private static final Integer imagesPerSecond = 30;
    private static final Float maxThreadSleepAcceleration = 30f;

    private static Long lauchSimTime = Instant.now().toEpochMilli();
    private static Long currentTime = Instant.now().toEpochMilli();

    private static Thread simulationThread = new Thread(Simulation::update);

    public Simulation() {
        time = 0;
        setSimulationSpeed(1f); // pour voir les éléments se déplacer
        droneNumber = 3;
        parcelNumber = 1;
        chargingStation = 5;
        droneWeightCapacity[0] = 0.1f;
        droneWeightCapacity[1] = 20f;
        droneBatteryCapacity[0] = 5;
        droneBatteryCapacity[1] = 55;
        simulationDurationRange[0] = 240;
        simulationDurationRange[1] = 1440;
        numberOfSimulationIterationRange[0] = 1;
        numberOfSimulationIterationRange[1] = 10;
        setCompetitionDifficultyLevels();
    }

    private void setCompetitionDifficultyLevels() {
        competitionDifficultyLevels.put("soft", .025f);
        competitionDifficultyLevels.put("medium", 0.50f);
        competitionDifficultyLevels.put("hard", 0.75f);
    }

    public static void setSimulationSpeed(Float f) {
        simulationSpeed = f; // TODO ajouter controle et exception
    }

    public static void addNumberToSpeed(Float nb) {
        setSimulationSpeed(getSimulationSpeed() + nb);
    }

    public static void addPercentageToSpeed(Float nb) {
        setSimulationSpeed(getSimulationSpeed() * (1 + nb));
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

    public static void initMainArea() {
        mainArea = new MainArea(0f, 0f, mainAreaWidth, mainAreaHeight);
    }

    public static void startDefault() {
        initMainArea();
        popAreas();
        popParcels();
        popDrones();
        popChargingStations();
        globalStart();
    }

    public static void startRandom() {
        initMainArea();
        popAreas();
        popParcels();
        popDrones();
        popChargingStations();
        globalStart();
    }

    public static void startCustom() {
        popParcels();
        globalStart();
    }

    public static void globalStart(){
        //simulationThread.start();
        for(Drone drone: drones){
            Thread droneThread = new Thread(drone);
            droneThreads.add(droneThread);
            droneThread.start();
        }
    }

    public static void stop(){
        setPlay(false);
        for(Thread droneThread: droneThreads){
            droneThread.interrupt();
        }

        if(simulationThread != null && simulationThread.isAlive()){
            simulationThread.stop();
        }
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
            t1 = System.nanoTime();
            // TODO ajust
            incrementTime();
            handleDrones();
            manageThreadSleepAccordingToSimAcceleration();
            t2 = System.nanoTime();
            manageDronesSpeedCoefAccordingtoSimAcceleration();
            updatePlayStatusAccordingToDuration();
        }
    }


    /*public static void update() {
        while (isPlay()) {
            t1 = System.nanoTime();
            // TODO ajust
            incrementTime();
            handleDrones();
            manageThreadSleepAccordingToSimAcceleration();
            t2 = System.nanoTime();
            manageDronesSpeedCoefAccordingtoSimAcceleration();
            updatePlayStatusAccordingToDuration();
        }
    }*/

    private static void manageDronesSpeedCoefAccordingtoSimAcceleration() {
        if(simulationSpeed > maxThreadSleepAcceleration) {
            float additiveCoef = simulationSpeed / maxThreadSleepAcceleration;
            deltaTSimStep = (long)(StrictMath.abs(t2 - t1)* additiveCoef);
        } else {
            deltaTSimStep = StrictMath.abs(t2 - t1);
        }
    }

    private static Long returnDeltaTSecAccordingToSimAcceleration() {
        long deltaT;
        if(simulationSpeed > maxThreadSleepAcceleration) {
            float additiveCoef = simulationSpeed / maxThreadSleepAcceleration;
            deltaT = (long)(StrictMath.abs(t2 - t1)* additiveCoef);
        } else {
            deltaT = StrictMath.abs(t2 - t1);
        }

        return deltaT;
    }

    private static void manageThreadSleepAccordingToSimAcceleration() {
        try {
            if(simulationSpeed > maxThreadSleepAcceleration) {
                Thread.sleep((long) (1000 / imagesPerSecond / maxThreadSleepAcceleration));
            } else {
                Thread.sleep((long) (1000 / imagesPerSecond / simulationSpeed));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    private static void handleDrones() {
        for (Drone drone : drones) {
            //drone.handleParcelInteractions();
            //drone.handleDroneInteractions();
            //drone.move(deltaTSimStep);
        }
    }

    public static void setTimeSimulationParameters(Integer simulationDuration, Integer numberOfSimulationIteration){
        Simulation.simulationDuration = simulationDuration;
        Simulation.numberOfSimulationIteration = numberOfSimulationIteration;
    }

    public static void setCompetitionDifficulty(String difficulty){
        competitionDifficulty = competitionDifficultyLevels.get(difficulty);
        if(competitionDifficulty == null){
            throw new IllegalArgumentException(difficulty + " is not defined");
        }
    }

    private static void updatePlayStatusAccordingToDuration(){
        currentTime = Instant.now().toEpochMilli();
        long simulationDurationInMilli = simulationDuration * secondsInAMinute * millisecondsInASecond;
        long elapsedTime = (long)(StrictMath.abs(currentTime - lauchSimTime)*simulationSpeed);
        play = elapsedTime <= simulationDurationInMilli;
    }

    private static void incrementTime() {
        setTime(getTime() + 1);
    }

    /* getteurs et setteurs triviaux */
    public static Boolean isPlay() {
        return play;
    }
    public static Float getSimulationSpeed() {
        return simulationSpeed;
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
    public static Map<String, Float> getCompetitionDifficultyLevels() {
        return competitionDifficultyLevels;
    }

    public static Long getT1() {
        return t1;
    }

    public static Long getT2() {
        return t2;
    }
}