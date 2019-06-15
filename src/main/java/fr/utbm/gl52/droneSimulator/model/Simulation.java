package fr.utbm.gl52.droneSimulator.model;

import fr.utbm.gl52.droneSimulator.service.DbDroneService;
import fr.utbm.gl52.droneSimulator.service.DbParameterService;
import fr.utbm.gl52.droneSimulator.service.entity.DbParameter;
import fr.utbm.gl52.droneSimulator.view.SimulationWindowView;
import fr.utbm.gl52.droneSimulator.view.graphicElement.ParcelGraphicElement;
import javafx.application.Platform;

import java.time.Instant;
import java.util.*;

public class Simulation {
    public static final long secondsInAMinute = 60L;
    public static final long millisecondsInASecond = 1000L;
    private static final Integer DEFAULT_DURATION = 240;

    private static ArrayList<Drone> drones = new ArrayList<>();
    private static ArrayList<Thread> droneThreads = new ArrayList<>();
    private static ArrayList<Parcel> parcels = new ArrayList<>();
    private static ArrayList<Area> areas = new ArrayList<>();
    private static List<ChargingStation> chargingStations = new ArrayList<>();
    private static MainArea mainArea;

    private static DbParameterService parameterService = new DbParameterService();
    private static DbDroneService droneService = new DbDroneService();
    private static DbParameter parameters;

    private static Boolean play = true;

    private static Integer time;
    private static Float simulationSpeed;
    private static Float minSimulationSpeedAcceleration = 1f;
    private static Float maxSimulationSpeedAcceleration = 20f;
    private static Integer parcelNumber;
    private static Integer droneNumber;
    private static  Integer chargingStation;

    private static Float[] droneWeightCapacity = new Float[2];
    private static Float[] droneBatteryCapacity = new Float[2];
    private static Integer[] simulationDurationRange = new Integer[2];
    private static Integer[] numberOfSimulationIterationRange = new Integer[2];
    private static Integer simulationDuration = 240;
    private static Integer numberOfSimulationIteration = numberOfSimulationIterationRange[0];
    private static Integer currentIteration = 1;

    private static Map<String, Integer[]> parcelTimeToDisappearRangeLinkedToDifficulty = new HashMap<>();
    private static Integer[] parcelTimeToDisappearRange = new Integer[2];

    private static final Float mainAreaWidth = 1800f;
    private static final Float mainAreaHeight = 600f;

    public static final String DEFAULT = "DEFAULT";
    public static final String RANDOM = "RANDOM";
    public static final String CUSTOM = "CUSTOM";

    private static final Integer imagesPerSecond = 30;
    private static final Float maxThreadSleepAcceleration = 30f;

    private static Long launchSimTime = Instant.now().toEpochMilli();
    private static Long currentTime = Instant.now().toEpochMilli();
    private static Long elapsedTime = 0L;

    private static Thread simulationThread = new Thread(Simulation::manageSimulationStop);

    public Simulation() {
        time = 0;
        setSimulationSpeed(1f);
        droneNumber = 1;
        parcelNumber = 25;
        chargingStation = 5;
        droneWeightCapacity[0] = 0.1f;
        droneWeightCapacity[1] = 20f;
        droneBatteryCapacity[0] = 5f;
        droneBatteryCapacity[1] = 55f;
        simulationDurationRange[0] = 240;
        simulationDurationRange[1] = 1440;
        numberOfSimulationIterationRange[0] = 1;
        numberOfSimulationIterationRange[1] = 10;
        initCompetitionDifficultyLevels();
        parcelTimeToDisappearRange = parcelTimeToDisappearRangeLinkedToDifficulty.get("soft");
    }

    private void initCompetitionDifficultyLevels() {
        Integer[] range = {60, 260};
        parcelTimeToDisappearRangeLinkedToDifficulty.put("soft", range);
        range[0] = 30;
        range[1] = 120;
        parcelTimeToDisappearRangeLinkedToDifficulty.put("medium", range);
        range[0] = 15;
        range[1] = 60;
        parcelTimeToDisappearRangeLinkedToDifficulty.put("hard", range);
    }

    private static void initIteration() {

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

    public static void flushParameters(){
        parameters = parameterService.save(
                simulationDuration,
                numberOfSimulationIteration,
                parcelTimeToDisappearRange[0],
                parcelTimeToDisappearRange[1]
        );
        flushDroneData();
    }

    public static void flushDroneData(){
        for(Drone drone: drones){
            droneService.save(
                    parameters.getIdSimu(),
                    currentIteration,
                    drone.getId(),
                    0,
                    drone.getBatteryCapacity(),
                    drone.getWeightCapacity(),
                    0,
                    drone.getX(),
                    drone.getY()
            );
        }
    }

    public static void startDefault() {
        initMainArea();
        //popAreas();
        popParcels();
        popDrones();
        popChargingStations();
        globalStart();
    }

    public static void startRandom() {
        initMainArea();
        //popAreas();
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
        simulationThread.start();
        for(Drone drone: drones){
            Thread droneThread = new Thread(drone);
            droneThreads.add(droneThread);
            droneThread.start();
        }
    }

    /**
     * Simulate the competition by removing parcels form the map.
     */
    private static void makeParcelDisappearWhenPickedByCompetitors() { //TODO: régler les confilts de Threads
        Parcel parcel;
        List<ParcelGraphicElement> parcelsGraphicToRemove = new ArrayList<>();

        for(ParcelGraphicElement parcelGraphic : SimulationWindowView.getParcelGraphicElements()){
            parcel = (Parcel) parcelGraphic.getSimulationElement();
            if(isParcelDisappearing(parcel)){
                parcelsGraphicToRemove.add(parcelGraphic);
                Parcel finalParcel = parcel;
                Platform.runLater(() -> getParcels().remove(finalParcel));
                System.out.println("Removing parcel " + finalParcel.getId() + " after " + finalParcel.getTimeToDisappear() + " min");
            }
        }
        Platform.runLater(() -> SimulationWindowView.removeParcelsGraphic(parcelsGraphicToRemove));
    }

    /**
     * Check if the parcel has reach is end of life (catch by other competitors) and it is not already loaded
     *
     * @param parcel Parcel to check
     *
     * @return True if yes
     */
    private static boolean isParcelDisappearing(Parcel parcel) {
        return parcel.getTimeToDisappear() < MathHelper.convertMillisecondsToMinutes(elapsedTime) && !parcel.isInJourney();
    }

    public static void stop(){
        setPlay(false);
        for(Thread droneThread: droneThreads){
            droneThread.interrupt();
        }
        simulationThread.interrupt();
    }

    private static void popParcels() {
        for (Integer i = 0; i < getParcelNumber(); ++i) {
            parcels.add(Parcel.createRandomized(i));
        }
        System.out.println(parcels.toString());
    }

    private static void popAreas() {
        for (Integer i = 0; i < 1; ++i) {
            areas.add(Area.createRandomized());
        }
    }

    private static void popDrones() {
        for (Integer i = 0; i < getDroneNumber(); ++i) {
            drones.add(Drone.createRandomizedDrone(i));
        }
    }

    private static void popChargingStations(){
        for (Integer i = 0; i < getChargingStationNumber(); ++i) {
            chargingStations.add(ChargingStation.createRandomizedChargingStations(i));
        }
    }

    public static void manageSimulationStop(){
        while (isPlay()){
            updatePlayStatusAccordingToDuration();
            makeParcelDisappearWhenPickedByCompetitors();
            popRandomParcels();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("Time elapsed");
        rebootSimulationForNextIteration();
        stop();
    }

    private static void rebootSimulationForNextIteration() {
        if(!isPlay()){
            if(currentIteration < numberOfSimulationIteration){
                initIteration();
            } else {
                stop();
            }
        }
    }

    private static void popRandomParcels() {
        Integer random = RandomHelper.getRandInt(0, 100);

        if(random == 42){
            Integer id = parcelNumber++;

            Parcel parcel = Parcel.createRandomized(id, elapsedTime);
            parcels.add(parcel);
            Platform.runLater(() -> SimulationWindowView.addParcelToView(parcel));
            System.out.println("parcel n " + id + "  has popped");
        }
    }

    public static void setTimeSimulationParameters(Integer numberOfSimulationIteration){
        setTimeSimulationParameters(Simulation.DEFAULT_DURATION, numberOfSimulationIteration);
    }

    public static void setTimeSimulationParameters(Integer simulationDuration, Integer numberOfSimulationIteration){
        Simulation.simulationDuration = simulationDuration;
        Simulation.numberOfSimulationIteration = numberOfSimulationIteration;
    }

    public static void setCompetitionDifficulty(String difficulty){
        parcelTimeToDisappearRange = parcelTimeToDisappearRangeLinkedToDifficulty.get(difficulty);
        if(parcelTimeToDisappearRange == null){
            throw new IllegalArgumentException(difficulty + " is not defined");
        }
    }

    /**
     * Iterate the clock according to elapsed time and simulation speed.
     */
    private static void updatePlayStatusAccordingToDuration(){
        currentTime = Instant.now().toEpochMilli();
        long simulationDurationInMilli = simulationDuration * secondsInAMinute * millisecondsInASecond;
        elapsedTime = (long)(StrictMath.abs(currentTime - launchSimTime)*simulationSpeed);
        //System.out.println("time elapsed " + elapsedTime/60000);
        play = elapsedTime <= simulationDurationInMilli;
    }

    private static void incrementTime() {
        setTime(getTime() + 1);
    }

    /* trivial getters et setters */
    public static Boolean isPlay() {
        return play;
    }
    public static Float getSimulationSpeed() {
        return simulationSpeed;
    }
    public static Float getMinSimulationSpeedAcceleration() {
        return minSimulationSpeedAcceleration;
    }
    public static Float getMaxSimulationSpeedAcceleration() {
        return maxSimulationSpeedAcceleration;
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
    public static Float[] getDroneBatteryCapacity() {
        return droneBatteryCapacity;
    }
    public static Integer[] getSimulationDurationRange() {
        return simulationDurationRange;
    }
    public static Integer[] getNumberOfSimulationIterationRange() {
        return numberOfSimulationIterationRange;
    }

    public static Float getMaxThreadSleepAcceleration() {
        return maxThreadSleepAcceleration;
    }

    public static Integer getImagesPerSecond() {
        return imagesPerSecond;
    }

    public static Long getCurrentTime() {
        return currentTime;
    }

    public static Long getElapsedTime() {
        return elapsedTime;
    }

    public static Integer[] getParcelTimeToDisappearRange() {
        return parcelTimeToDisappearRange;
    }

    public static Map<String, Integer[]> getCompetitionDifficultyLevels(){
        return parcelTimeToDisappearRangeLinkedToDifficulty;
    }
}