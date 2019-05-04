package fr.utbm.gl52.droneSimulator.model;

//import Controller.MouseFood;
//import Controller.MouseOther;
import java.util.ArrayList;

public class Vivarium{
    //    private ArrayList<SimulationElement> gameElements = new ArrayList<SimulationElement>();
    private static ArrayList<Animal> animals = new ArrayList<Animal>();
    private static ArrayList<Food> foods = new ArrayList<Food>();
    private static Board board;
    // on pivilégie un tableau en 16/9 pour la plupart des écrans d'ordinateurs
    private static int width;
    private static int height;

    private static int foodNumber;
    private static int animalNumber;

    private static boolean live = true;
    private static int time = 0;
    private static float speed = 17; // assez petit pour un déplacement qui semble plus naturel (contigu et non sacadé)

    public static float getSpeed() {
        return speed;
    }
    public static void setSpeed(float speed) {
        if (speed >= 1)
            Vivarium.speed = speed;
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
        Vivarium.width = width;
    }
    public static void setHeight(int height) {
        Vivarium.height = height;
    }

    Vivarium() {
        SimulationElement.setVivarium(this); // rattachement du Pan au manageur graphique
    }

    public static void setAnimals(ArrayList<Animal> as) {
        animals = as;
    }

    public static void setFoods(ArrayList<Food> as) {
        foods = as;
    }

    public static ArrayList<Animal> getAnimals() { return animals;}

    public static ArrayList<Food> getFoods() { return foods;}

    public static void removeFood(Food f) {
        foods.remove(f);
    }

    public static void removeAnimal(Animal a) {
        animals.remove(a);
    }

    public static void removeAllAnimals() { animals.clear(); }

    public static void live() {
        board = new Board();
        setBoard(board);

        for (int i=1; i<getAnimalNumber(); ++i){
            Animal a;

            do
                a = SpecieManager.getSpecie(Animal.getRandSpecie());
            while(!Board.hasACompatibleBiome(a));

            a.setRandCoord();
            animals.add(a);
        }

        for (int i=1; i<getFoodNumber(); ++i){
            foods.add(new Food());
        }

        // interactions entre les entités du jeu
        while (true) {

            if (isLive()) {
                addToTime(1);
                
                if (getTime()%1020==0) // toutes les 60 secondes
                    foods.add(new Food("vegetable"));

                // améliorer pour ne pas faire 2 fois les mêmes tests : tableau dédié et pop animal testé ou break
                // TODO reessayer avec forach, fix thread conflit

                for (int i = 0; i < animals.size(); ++i) {
                    Animal ai = animals.get(i);
                    ai.setBusy(false);

                    // action entre animaux, concerne les deux
                    // interactions prioritaire
                    // j=i car on ne duplique pas les tests a1 avec a2 et a2 avec a1
                    for (int j = i + 1; j < animals.size(); ++j) {
                        Animal aj = animals.get(j);

                        if (ai.meet(aj)) {
                            ai.interact(aj);
                            ai.setBusy(true);
                            break;
                        }
                    }
                    if (!ai.isBusy()) {
                        for (int k = 0; k < foods.size(); ++k) {
                            Food f = foods.get(k);

                            if (ai.meet(f)) {
                                ai.interact(f);
                                ai.setBusy(true);
                                break;
                            }
                        }
                        if (!ai.isBusy()) {
                            // j=0 car un animal peut voir un autre sans que l'autre ne le voit, il faut donc tester les 2 possibilités
                            for (int j = 0; j < animals.size(); ++j) {
                                if (i != j) {
                                    Animal aj = animals.get(j);

                                    // ne concerne qu'un animal
                                    // interactions secondaire
                                    if (ai.see(aj)) {
                                        boolean react = ai.reactToAnimal(aj);
                                        if (react) {
                                            ai.setBusy(true);
                                            break; // si on réagit, on arrête les tests pour cet animal
                                        }

                                        // rmq, ce n'est pas le plus près mais le premier dans la liste d'animaux, facilement à améliorable
                                    }
                                }
                            }
                            if (!ai.isBusy()) {
                                for (int k = 0; k < foods.size(); ++k) {
                                    if (i != k) {
                                        Food fk = foods.get(k);

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
                for (int i = 0; i < animals.size(); ++i) {
                    (animals.get(i)).move(); // déplacement des animaux
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

    public static boolean isLive() {
        return live;
    }
    public static void setLive(boolean live) {
        Vivarium.live = live;
    }

    public static int getTime() {
        return time;
    }

    public static void setTime(int time) {
        Vivarium.time = time;
    }

    public static void removeAllFoods() {
        foods.clear();
    }

    public static void setAnimalNumber(int animalNumber) {
        Vivarium.animalNumber = animalNumber;
    }

    public static void setFoodNumber(int foodNumber) {
        Vivarium.foodNumber = foodNumber;
    }

    public static int getAnimalNumber() {
        return animalNumber;
    }

    public static int getFoodNumber() {
        return foodNumber;
    }
}