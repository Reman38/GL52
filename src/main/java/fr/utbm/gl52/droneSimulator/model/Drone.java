package Model;

import java.util.ArrayList;
import java.util.Properties;

public class Animal extends GameElement{
    private int size;
    private float orientation = 0; // en radian
    private int speed;
    private int visibleDistance;
    private float visibleAngle;

    private String specie;
    private boolean carnivorous;
    private boolean herbivorous;
    private boolean insectivorous;

    private boolean insect;

    private boolean terrestrial;
    private boolean aquatic;

    private boolean busy;
    private int sexe;
    private float fertilityRate;

    /*
        Constructeurs
    */
    public Animal(){}
    public Animal(Properties prop) {
        super();
        setSpecie(prop.getProperty("specie"));
        setSize(Integer.parseInt(prop.getProperty("size")));
        setSpeed(Integer.parseInt(prop.getProperty("speed")));
        setVisibleDistance(Integer.parseInt(prop.getProperty("visibleDistance")));
        setVisibleAngle(Float.parseFloat(prop.getProperty("visibleAngle")));
        setRandOrientation();
        if (prop.getProperty("sexe") != null)
            setSexe(Integer.parseInt(prop.getProperty("sexe")));
        else
            setRandSexe();
        setCarnivorous(Boolean.parseBoolean(prop.getProperty("carnivorous")));
        setHerbivorous(Boolean.parseBoolean(prop.getProperty("herbivorous")));
        setInsectivorous(Boolean.parseBoolean(prop.getProperty("insectivorous")));
        setTerrestrial(Boolean.parseBoolean(prop.getProperty("terrestrial")));
        setAquatic(Boolean.parseBoolean(prop.getProperty("aquatic")));
        setInsect(Boolean.parseBoolean(prop.getProperty("insect")));
        setFertilityRate(Float.parseFloat(prop.getProperty("fertilityRate")));
    }

    public void saveSpecie(){
        SpecieManager.saveSpecie(this);
    }

    /*
        Gestion de l'affichage pour le débug
        TODO terminer une fois les attributs fixés
    */
    public String toString() {
        String s =
                super.toString()
                        + "size: " + getSize() + System.getProperty("line.separator")
                        + "orientation: " + getOrientation() + System.getProperty("line.separator")
                        + "speed: " + getSpeed() + System.getProperty("line.separator")
                        + "visibleDistance: " + getVisibleDistance() + System.getProperty("line.separator");
        return s;
    }

    /*
        Gestion de la position
        // TODO améliorer pour restreindre aux cases avec des biomes compatiblent
        // TODO lancer une exeption si le rayon n'est pas défini ou = à 0
        // TODO envisager séparer les cases terrestrent et aquatiques dans 2 listes dans board
    */
    public ArrayList<Case> getPossibleCases(){
        ArrayList<Case> possibleCases = new ArrayList<Case>();
        for (Case c:Board.getCases()){
            if (isApossibleBiome(c))
                possibleCases.add(c);
        }
        return possibleCases;
    }
    public void setRandCoord() {
        ArrayList<Case> possibleCases = getPossibleCases();

        Case c = possibleCases.get(getRandInt(0, possibleCases.size()-1));
        setRandCoord(c);
    }

    /*
        Gestion de la taille
    */
    public int getSize() {
        return size;
    }
    public void setSize(int t) {
        size = t;
    }
    public void setRandsize() {
        setSize(getRandInt(40, 50));
    }
    //    public void grow(int s) {
//        setSize(getSize() + s);
//    }
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
        setVisibleAngle(getRandFloat((float) (Math.PI/8), (float) (2*Math.PI)));
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
            if (getRandInt(1,100)<=10) {
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
        boolean aquaticBiome=false;
        boolean terrestrialBiome=false;

        for (Case c:Board.getCases()){
            if (c.getBiome()=="water")
                aquaticBiome = true;
            else
                terrestrialBiome = true;
        }

        if (aquaticBiome || terrestrialBiome){
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
        return ((c.getBiome()=="water" && isAquatic()) || (c.getBiome()!="gap" && c.getBiome()!="water" && isTerrestrial()));
    }

    /*
        Gestion du déplacement
    */
    public void move() {
        // TODO améliorer performances avec une agrégation de currentCase pour retirer les tests si le déplacement ne change pas de case (les 4 ?)
        float newX = (float) (getX() + (speed * Math.cos(orientation)));
        float newY = (float) (getY() + (speed * Math.sin(-orientation)));

        // récupération des cases sur lequel l'animal serait situé
        Case newTLCase = getCase(newX-getWidth()/2, newY-getHeight()/2);
        Case newTRCase = getCase(newX+getWidth()/2, newY-getHeight()/2);
        Case newBLCase = getCase(newX-getWidth()/2, newY+getHeight()/2);
        Case newBRCase = getCase(newX+getWidth()/2, newY+getHeight()/2);

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

        if (impossiblePlaceNb == 0){
            setX(newX);
            setY(newY);
        }
        else{
            double angle = 0;
            if(!isApossibleCase(newTLCase) && !isApossibleCase(newTRCase)){
                if (orientation < Math.PI/2){
                    angle = -Math.PI/2;
                }
                else{
                    angle = Math.PI/2;
                }
            }
            else if(!isApossibleCase(newBLCase) && !isApossibleCase(newBRCase)){
                if (orientation < 3 * Math.PI/2){
                    angle = -Math.PI/2;
                }
                else{
                    angle = Math.PI/2;
                }
            }
            else if(!isApossibleCase(newTLCase) && !isApossibleCase(newBLCase)){
                if (orientation < Math.PI){
                    angle = -Math.PI/2;
                }
                else{
                    angle = Math.PI/2;
                }
            }
            else if(!isApossibleCase(newTRCase) && !isApossibleCase(newBRCase)){
                if (orientation > 3*Math.PI/2 && orientation < 2*Math.PI){
                    angle = -Math.PI/2;
                }
                else{
                    angle = Math.PI/2;
                }
            }
            else
                angle = -Math.PI;

            rotation((float) angle);
        }

    }
    public void goTo(GameElement ge) {
        setOrientation(ge, false);
    }
    public void flee(Animal a) {
        setOrientation(a, true);
    }

    /*
        Gestion des caractéristiques
    */
    public String getSpecie() {
        return specie;
    }
    public static String getRandSpecie() {
        return getRandValueOf(new String[]{"angler","anteater","bear","bee","chameleon","crab","crocodile","duck","eagle","elephant","fly","fox","frog","giraffe","globefish","hedgehog","hippopotamus","lion","mosquito","mouse","owl","piranha","rabbit","scorpio","sea-urchin","shark","sheep","skunk","sloth","snake","spider","squid","starfish","tiger","turtle","wasp","wolf"});
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
        Gestion de l'orientation
    */
    public float getOrientation() {
        return orientation;
    }
    public void setOrientation(float radian) {
        orientation = simplifyAngle(radian);
    }
    public void setOrientation(GameElement ge, boolean goAwayFrom) {
        float newOrientation = angleCalcul(ge);

        if (goAwayFrom)
            newOrientation += Math.PI;

        setOrientation(newOrientation);
    }
    public void setOrientation(int x, int y) {
        setOrientation(angleCalcul(x, y));
    }

    public void setRandOrientation() {
        setOrientation(getRandInt(0, (int) (2 * Math.PI)));
    }
    public void rotation(float radian) {
        setOrientation(getOrientation() + radian);
    }
    public void setNaturalOrientation() {
        if (getRandInt(1,10)<9)
            rotation(getRandFloat(-0.075f, 0.075f));
    }

    /*
        Fonctions d'interaction
    */
    public boolean see(GameElement ge) {
        float angle = angleCalcul(ge);
        float angleWithTwoPi = (float) (angle+2*Math.PI);
        float halfVisibleAngle = getVisibleAngle()/2;
        float angleMin = getOrientation() - halfVisibleAngle;
        float angleMax = getOrientation() + halfVisibleAngle;

        return
                distanceCalcul(ge) < getVisibleDistance()
                        &&(
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
    public boolean reactToAnimal(Animal a) {
        Boolean react = false;
        if (isSameSpecie(a)){
            goTo(a);
            react = true;
        }
        else{
            if (isPredator(a)){
                goTo(a);
                react = true;
            }
            else if (isPrey(a)){
                flee(a);
                react = true;
            }
        }

        return react;
    }

    public boolean meet(Animal a) {
        return (distanceCalcul(a) < (getSize()/2 + a.getSize()/2));
    }

    public float getLukeToKill(Animal a) {
        float luke = getSize()/a.getSize();

        if (getSize() > a.getSize())
            luke = 1/luke;

        return luke;
    }
    public boolean isPredator(Animal a) {
        boolean isPredator = false;

        if ( (isCarnivorous() && a.isVertebrate()) || (isInsectivorous() && a.isInsect()) )
            isPredator = true;

        return isPredator;
    }
    public boolean isPrey(Animal a) {
        boolean isPrey = false;

        if ( (a.isCarnivorous() && isVertebrate()) || (a.isInsectivorous() && isInsect()) )
            isPrey = true;

        return isPrey;
    }
    private void makeLove(Animal a) {
        // TODO ajouter dans l'ajout d'eespece et dans la recuperation des caracs d'especes
        if(getRandInt(1,100) < getFertilityRate())
            makeBaby();
    }
    private void makeBaby() {
        Animal a = SpecieManager.getSpecie(getSpecie());
        a.setRandCoord(getCase());
        Vivarium.getAnimals().add(a);
    }

    private Case getCase() {
        return GameElement.getCase(getX(), getY());
    }

    public int getSexe() {
        return sexe;
    }
    private void setSexe(int s) {
        sexe = s;
    }
    private void setRandSexe() {
        setSexe(getRandInt(0,1));
    }

    public void interact(Animal a) {
        if (!isSameSpecie(a)){
            if (isPredator(a)){
//                if (getRandInt(1,100) < getLukeToKill(a))
                kill(a);
            }
            else if (isPrey(a)){
//                if (getRandInt(1,100) < a.getLukeToKill(this))
                a.kill(this);
            }
            else{
                flee(a);
                a.flee(this);
            }
        }
        else{
            if ((getSexe() == 2 && a.getSexe() == 2) || (getSexe() == 0 && a.getSexe() == 1) || (a.getSexe() == 0 && getSexe() == 1))
                makeLove(a);

            flee(a);
            a.flee(this);
        }
    }

    public void kill(Animal a) {
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
        if (isEatable(f)){
            goTo(f);
            react = true;
        }
        return react;
    }
    public boolean meet(Food f) {
        return (distanceCalcul(f) < (getSize()/2 + f.getSize()/2));
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

    public boolean isSameSpecie(Animal a) {
        return getSpecie().equals(a.getSpecie());
    }

    public float getFertilityRate() {
        return fertilityRate;
    }
    public void setFertilityRate(float fr) {
        fertilityRate = fr;
    }
}