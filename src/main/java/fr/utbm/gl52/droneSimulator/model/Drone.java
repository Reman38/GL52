package fr.utbm.gl52.droneSimulator.model;

public class Drone extends SimulationElement {
    // constantes de classe
    static final private int size = 5;
    static final private int speed = 5;
    static final private int visibleDistance = 10000; // TODO v2

    // attributs
    private float rotation; // TODO degres ou radian ?
    private boolean busy; // TODO conserver ?

    public Drone() {
        rotation = 0;
    }

    /*
        Gestion de l'affichage pour le débug
        TODO terminer une fois les attributs fixés
    */
    public String toString() {
        String s =
                super.toString()
                        + "rotation: " + getRotation() + System.getProperty("line.separator")
        return s;
    }

    public void setRandCoord() {
        setRandCoord(c); // TODO change Case to ManagedZone
    }

    /*
        Gestion de la taille
    */
    public float getWidth() {
        return getSize();
    }

    public float getHeight() {
        return getSize();
    }

    /*
        Gestion de la vitesse
    */
    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int s) {
        speed = s;
    }

    public void setRandSpeed() {
        setSpeed(getRandInt(1, 3));
    }

    /*
        Gestion de la distance de vision
    */
    public int getVisibleDistance() {
        return visibleDistance;
    }

    public void setVisibleDistance(int vd) {
        visibleDistance = vd;
    }
//    public void setRandVisibleDistance() {
//        setVisibleDistance((int) getRandDouble(getSize() * 2, getSize() * 3));
//    }

    /*
        Gestion de l'angle de vision
    */
    public float getVisibleAngle() {
        return visibleAngle;
    }

    public void setVisibleAngle(float radian) {
        visibleAngle = radian;
    }

    public void setRandVisibleAngle() {
        setVisibleAngle(getRandFloat((float) (Math.PI / 8), (float) (2 * Math.PI)));
    }

    /*
        Gestion du régime alimentaire
    */
    public boolean isCarnivorous() {
        return carnivorous;
    }

    public boolean isHerbivorous() {
        return herbivorous;
    }

    public boolean isInsectivorous() {
        return insectivorous;
    }

    public void setCarnivorous(boolean carnivorous) {
        this.carnivorous = carnivorous;
    }

    public void setHerbivorous(boolean herbivorous) {
        this.herbivorous = herbivorous;
    }

    public void setInsectivorous(boolean insectivorous) {
        this.insectivorous = insectivorous;
    }

    public boolean hasARegime() {
        return isCarnivorous() || isHerbivorous() || isInsectivorous();
    }

    public void setRandRegime() {
        while (!hasARegime()) {
            if (getRandInt(1, 100) <= 10) {
                setCarnivorous(true);
            }
            if (getRandBool()) {
                setHerbivorous(true);
            }
            if (getRandBool()) {
                setInsectivorous(true);
            }
        }
    }

    /*
        Gestion de de la relation avec l'environnement
    */
    public void setTerrestrial(boolean terrestrial) {
        this.terrestrial = terrestrial;
    }

    public void setAquatic(boolean aquatic) {
        this.aquatic = aquatic;
    }

    public void setRandPossibleBiome() {
        boolean aquaticBiome = false;
        boolean terrestrialBiome = false;

        for (Case c : Board.getCases()) {
            if (c.getBiome() == "water")
                aquaticBiome = true;
            else
                terrestrialBiome = true;
        }

        if (aquaticBiome || terrestrialBiome) {
            while (!(isTerrestrial() || isAquatic())) {
                if (terrestrialBiome && getRandBool())
                    setTerrestrial(true);
                if (aquaticBiome && getRandBool())
                    setAquatic(true);
            }
        }
    }

    public boolean isApossibleCase(Case c) {
        return (c != null && isApossibleBiome(c));
    }

    public boolean isApossibleBiome(Case c) {
        return ((c.getBiome() == "water" && isAquatic()) || (c.getBiome() != "gap" && c.getBiome() != "water" && isTerrestrial()));
    }

    /*
        Gestion du déplacement
    */
    public void move() {
        // TODO améliorer performances avec une agrégation de currentCase pour retirer les tests si le déplacement ne change pas de case (les 4 ?)
        float newX = (float) (getX() + (speed * Math.cos(rotation)));
        float newY = (float) (getY() + (speed * Math.sin(-rotation)));

        // récupération des cases sur lequel l'animal serait situé
        Case newTLCase = getCase(newX - getWidth() / 2, newY - getHeight() / 2);
        Case newTRCase = getCase(newX + getWidth() / 2, newY - getHeight() / 2);
        Case newBLCase = getCase(newX - getWidth() / 2, newY + getHeight() / 2);
        Case newBRCase = getCase(newX + getWidth() / 2, newY + getHeight() / 2);

        // est-ce que les cases d'arrivé potentielles ont un biome compatible ?
        int impossiblePlaceNb = 0;
        if (!isApossibleCase(newTLCase))
            ++impossiblePlaceNb;
        if (!isApossibleCase(newTRCase))
            ++impossiblePlaceNb;
        if (!isApossibleCase(newBLCase))
            ++impossiblePlaceNb;
        if (!isApossibleCase(newBRCase))
            ++impossiblePlaceNb;

        if (impossiblePlaceNb == 0) {
            setX(newX);
            setY(newY);
        } else {
            double angle = 0;
            if (!isApossibleCase(newTLCase) && !isApossibleCase(newTRCase)) {
                if (rotation < Math.PI / 2) {
                    angle = -Math.PI / 2;
                } else {
                    angle = Math.PI / 2;
                }
            } else if (!isApossibleCase(newBLCase) && !isApossibleCase(newBRCase)) {
                if (rotation < 3 * Math.PI / 2) {
                    angle = -Math.PI / 2;
                } else {
                    angle = Math.PI / 2;
                }
            } else if (!isApossibleCase(newTLCase) && !isApossibleCase(newBLCase)) {
                if (rotation < Math.PI) {
                    angle = -Math.PI / 2;
                } else {
                    angle = Math.PI / 2;
                }
            } else if (!isApossibleCase(newTRCase) && !isApossibleCase(newBRCase)) {
                if (rotation > 3 * Math.PI / 2 && rotation < 2 * Math.PI) {
                    angle = -Math.PI / 2;
                } else {
                    angle = Math.PI / 2;
                }
            } else
                angle = -Math.PI;

            rotation((float) angle);
        }

    }

    public void goTo(SimulationElement ge) {
        setOrientation(ge, false);
    }

    public void flee(fr.utbm.gl52.droneSimulator.model.Drone a) {
        setOrientation(a, true);
    }

    /*
        Gestion des caractéristiques
    */
    public String getSpecie() {
        return specie;
    }

    public static String getRandSpecie() {
        return getRandValueOf(new String[]{"angler", "anteater", "bear", "bee", "chameleon", "crab", "crocodile", "duck", "eagle", "elephant", "fly", "fox", "frog", "giraffe", "globefish", "hedgehog", "hippopotamus", "lion", "mosquito", "mouse", "owl", "piranha", "rabbit", "scorpio", "sea-urchin", "shark", "sheep", "skunk", "sloth", "snake", "spider", "squid", "starfish", "tiger", "turtle", "wasp", "wolf"});
    }

    public void setRandSpecie() {
        specie = getRandSpecie();
    }

    public boolean isAquatic() {
        return aquatic;
    }

    public boolean isTerrestrial() {
        return terrestrial;
    }

    /*
        Gestion de l'état
    */
    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean b) {
        busy = b;
    }

    /*
        Gestion de l'rotation
    */
    public float getRotation() {
        return rotation;
    }

    public void setRotation(float radian) {
        rotation = simplifyAngle(radian);
    }

    public void setOrientation(SimulationElement ge, boolean goAwayFrom) {
        float newOrientation = angleCalcul(ge);

        if (goAwayFrom)
            newOrientation += Math.PI;

        setRotation(newOrientation);
    }

    public void setOrientation(int x, int y) {
        setRotation(angleCalcul(x, y));
    }

    public void setRandOrientation() {
        setRotation(getRandInt(0, (int) (2 * Math.PI)));
    }

    public void rotation(float radian) {
        setRotation(getRotation() + radian);
    }

    public void setNaturalOrientation() {
        if (getRandInt(1, 10) < 9)
            rotation(getRandFloat(-0.075f, 0.075f));
    }

    /*
        Fonctions d'interaction
    */
    public boolean see(SimulationElement ge) {
        float angle = angleCalcul(ge);
        float angleWithTwoPi = (float) (angle + 2 * Math.PI);
        float halfVisibleAngle = getVisibleAngle() / 2;
        float angleMin = getRotation() - halfVisibleAngle;
        float angleMax = getRotation() + halfVisibleAngle;

        return
                distanceCalcul(ge) < getVisibleDistance()
                        && (
                        (angleMin < angle && angleMax > angle)
                                ||
                                (angleMin < angleWithTwoPi && angleMax > angleWithTwoPi)
                )
                ;
    }

    /*
        Fonctions d'interaction - animaux
    */
    // si dans le rayon et dans l'angle
    public boolean reactToAnimal(fr.utbm.gl52.droneSimulator.model.Drone a) {
        Boolean react = false;
        if (isSameSpecie(a)) {
            goTo(a);
            react = true;
        } else {
            if (isPredator(a)) {
                goTo(a);
                react = true;
            } else if (isPrey(a)) {
                flee(a);
                react = true;
            }
        }

        return react;
    }

    public boolean meet(fr.utbm.gl52.droneSimulator.model.Drone a) {
        return (distanceCalcul(a) < (getSize() / 2 + a.getSize() / 2));
    }

    public float getLukeToKill(fr.utbm.gl52.droneSimulator.model.Drone a) {
        float luke = getSize() / a.getSize();

        if (getSize() > a.getSize())
            luke = 1 / luke;

        return luke;
    }

    public boolean isPredator(fr.utbm.gl52.droneSimulator.model.Drone a) {
        boolean isPredator = false;

        if ((isCarnivorous() && a.isVertebrate()) || (isInsectivorous() && a.isInsect()))
            isPredator = true;

        return isPredator;
    }

    public boolean isPrey(fr.utbm.gl52.droneSimulator.model.Drone a) {
        boolean isPrey = false;

        if ((a.isCarnivorous() && isVertebrate()) || (a.isInsectivorous() && isInsect()))
            isPrey = true;

        return isPrey;
    }

    private void makeLove(fr.utbm.gl52.droneSimulator.model.Drone a) {
        // TODO ajouter dans l'ajout d'eespece et dans la recuperation des caracs d'especes
        if (getRandInt(1, 100) < getFertilityRate())
            makeBaby();
    }

    private void makeBaby() {
        fr.utbm.gl52.droneSimulator.model.Drone a = SpecieManager.getSpecie(getSpecie());
        a.setRandCoord(getCase());
        Vivarium.getAnimals().add(a);
    }

    private Case getCase() {
        return SimulationElement.getCase(getX(), getY());
    }

    public int getSexe() {
        return sexe;
    }

    private void setSexe(int s) {
        sexe = s;
    }

    private void setRandSexe() {
        setSexe(getRandInt(0, 1));
    }

    public void interact(fr.utbm.gl52.droneSimulator.model.Drone a) {
        if (!isSameSpecie(a)) {
            if (isPredator(a)) {
//                if (getRandInt(1,100) < getLukeToKill(a))
                kill(a);
            } else if (isPrey(a)) {
//                if (getRandInt(1,100) < a.getLukeToKill(this))
                a.kill(this);
            } else {
                flee(a);
                a.flee(this);
            }
        } else {
            if ((getSexe() == 2 && a.getSexe() == 2) || (getSexe() == 0 && a.getSexe() == 1) || (a.getSexe() == 0 && getSexe() == 1))
                makeLove(a);

            flee(a);
            a.flee(this);
        }
    }

    public void kill(fr.utbm.gl52.droneSimulator.model.Drone a) {
        Vivarium.removeAnimal(a);
    }

    /*
        Fonctions d'interaction - nourriture
        TODO faire une action sur l'animal mangeur sur la vie, la taille ou autre
    */
    public boolean isEatable(Food f) {
        return (
                (f.getType() == "vegetable" && isHerbivorous())
                        || (f.getType() == "meat" && isCarnivorous())
                        || (f.getType() == "deadInsecte" && isInsectivorous())
        );
    }

    public boolean reactToFood(Food f) {
        Boolean react = false;
        if (isEatable(f)) {
            goTo(f);
            react = true;
        }
        return react;
    }

    public boolean meet(Food f) {
        return (distanceCalcul(f) < (getSize() / 2 + f.getSize() / 2));
    }

    public void interact(Food f) {
        if (isEatable(f))
            eat(f);
    }

    public void eat(Food f) {
        Vivarium.removeFood(f);
        // TODO activer ou supprimer grow(aj.getSize());
    }

    public void setSpecie(String specie) {
        this.specie = specie;
    }

    public boolean isInsect() {
        return insect;
    }

    public boolean isVertebrate() {
        return !isInsect();
    }

    public void setInsect(boolean insect) {
        this.insect = insect;
    }

    public boolean isSameSpecie(fr.utbm.gl52.droneSimulator.model.Drone a) {
        return getSpecie().equals(a.getSpecie());
    }

    public float getFertilityRate() {
        return fertilityRate;
    }

    public void setFertilityRate(float fr) {
        fertilityRate = fr;
    }
}