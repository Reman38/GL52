package fr.utbm.gl52.droneSimulator.model;

import java.util.ArrayList;

public class Simulation {
    private static ArrayList<Drone> drones = new ArrayList<>();
    private static ArrayList<Parcel> parcels = new ArrayList<>();

    private static int parcelNumber;
    private static int droneNumber;

    private static boolean play = true;
    private static int time = 0;
    private static float speed = 17; // assez petit pour un déplacement qui semble naturel (contigu et non sacadé)

//    private static Board board; // TODO terminer de refactor
    private static Area mainArea;

    public static float getSpeed() {
        return speed;
    }
    public static void setSpeed(float speed) {
        if (speed >= 1)
            Simulation.speed = speed;
    }
    public static void addToSpeed(int nb) {
        setSpeed(getSpeed()+nb);
    }
    public static void changeSpeed(float c) {
        setSpeed(getSpeed()+getSpeed()*c);
    }

    public static int getWidth() {
        return width;
    }
    public static int getHeight() {
        return height;
    }
    public static void setWidth(int width) {
        Simulation.width = width;
    }
    public static void setHeight(int height) {
        Simulation.height = height;
    }

    Simulation() {
        SimulationElement.setSimulation(this); // rattachement du Pan au manageur graphique
    }

    public static void setDrones(ArrayList<Drone> as) {
        drones = as;
    }

    public static void setParcels(ArrayList<Parcel> as) {
        parcels = as;
    }

    public static ArrayList<Drone> getDrones() { return drones;}

    public static ArrayList<Parcel> getParcels() { return parcels;}

    public static void removeParcel(Parcel f) {
        parcels.remove(f);
    }

    public static void removeDrone(Drone a) {
        drones.remove(a);
    }

    public static void removeAllDrones() { drones.clear(); }

    public static void live() {
        mainArea = new Board();
        setBoard(mainArea);

        for (int i=1; i<getDroneNumber(); ++i){
            Drone a;

            do
                a = SpecieManager.getSpecie(Drone.getRandSpecie());
            while(!Board.hasACompatibleBiome(a));

            a.setRandCoord();
            drones.add(a);
        }

        for (int i = 1; i< getParcelNumber(); ++i){
            parcels.add(new Parcel());
        }

        // interactions entre les entités du jeu
        while (true) {

            if (isPlay()) {
                addToTime(1);
                
                if (getTime()%1020==0) // toutes les 60 secondes
                    parcels.add(new Parcel("vegetable"));

                // améliorer pour ne pas faire 2 fois les mêmes tests : tableau dédié et pop animal testé ou break
                // TODO reessayer avec forach, fix thread conflit

                for (int i = 0; i < drones.size(); ++i) {
                    Drone ai = drones.get(i);
                    ai.setBusy(false);

                    // action entre animaux, concerne les deux
                    // interactions prioritaire
                    // j=i car on ne duplique pas les tests a1 avec a2 et a2 avec a1
                    for (int j = i + 1; j < drones.size(); ++j) {
                        Drone aj = drones.get(j);

                        if (ai.meet(aj)) {
                            ai.interact(aj);
                            ai.setBusy(true);
                            break;
                        }
                    }
                    if (!ai.isBusy()) {
                        for (int k = 0; k < parcels.size(); ++k) {
                            Parcel f = parcels.get(k);

                            if (ai.meet(f)) {
                                ai.interact(f);
                                ai.setBusy(true);
                                break;
                            }
                        }
                        if (!ai.isBusy()) {
                            // j=0 car un animal peut voir un autre sans que l'autre ne le voit, il faut donc tester les 2 possibilités
                            for (int j = 0; j < drones.size(); ++j) {
                                if (i != j) {
                                    Drone aj = drones.get(j);

                                    // ne concerne qu'un animal
                                    // interactions secondaire
                                    if (ai.see(aj)) {
                                        boolean react = ai.reactToDrone(aj);
                                        if (react) {
                                            ai.setBusy(true);
                                            break; // si on réagit, on arrête les tests pour cet animal
                                        }

                                        // rmq, ce n'est pas le plus près mais le premier dans la liste d'animaux, facilement à améliorable
                                    }
                                }
                            }
                            if (!ai.isBusy()) {
                                for (int k = 0; k < parcels.size(); ++k) {
                                    if (i != k) {
                                        Parcel fk = parcels.get(k);

                                        if (ai.see(fk)) {
                                            boolean react = ai.reactToFood(fk);
                                            if (react) {
                                                ai.setBusy(true);
                                                break; // si on réagit, on arrête les tests pour cet animal
                                            }
                                        }
                                    }
                                }
                                if (!ai.isBusy())
                                    ai.setNaturalOrientation();
                            }
                        }
                    }
                }

                // modification du modèle
                for (int i = 0; i < drones.size(); ++i) {
                    (drones.get(i)).move(); // déplacement des animaux
                }
            }

            try {
                Thread.sleep((int) getSpeed());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private static void addToTime(int i) {
        setTime(getTime()+1);
    }

    public static Board getBoard() {
        return board;
    }

    public static void setBoard(Board p) {
        board = p;
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

    public static void removeAllFoods() {
        parcels.clear();
    }

    public static void setDroneNumber(int animalNumber) {
        Simulation.droneNumber = animalNumber;
    }

    public static void setParcelNumber(int parcelNumber) {
        Simulation.parcelNumber = parcelNumber;
    }

    public static int getDroneNumber() {
        return droneNumber;
    }

    public static int getParcelNumber() {
        return parcelNumber;
    }

    public static Area getMainArea() {
        return mainArea;
    }
}