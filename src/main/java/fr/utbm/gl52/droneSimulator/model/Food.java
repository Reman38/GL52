package fr.utbm.gl52.droneSimulator.model;

public class Food extends SimulationElement {
    private int size;
    private String type;
    private String specie;

    public Food(int x, int y, String type, String specie) {
        super();
        setRandsize();
        setX(x);
        setY(y);
        setType(type);
        setSpecie(specie);
    }
    public Food(String type) {
        super();
        setRandsize();
        setRandX();
        setRandY();
        setType(type);
        setRandSpecie();
    }
    public Food() {
        super();
        setRandsize();
        // après le setSize ! sinon contrainte de zone pas prise en compte
        setRandX();
        setRandY();
        setRandType();
        setRandSpecie();
    }

    public int getSize() {
        return size;
    }
    public void setSize(int t) {
        size = t;
    }
    public void setRandsize() {
        setSize(getRandInt(40, 50));
    }

    public String toString() {
        String s =
            super.toString()
            +"size: "+getSize()+System.getProperty("line.separator")
        ;
        return s;
    }

//    public void setX(int x) {
//        coord[0] = x;
//    }
//    public void setY(int y) {
//        coord[1] = y;
//    }
    public void setRandX() {
        super.setX(getRandFloat(getWidth()/2, getSimulation().getWidth() - getWidth()/2));
    }
    public void setRandY(){
        super.setY(getRandFloat(getHeight()/2, getSimulation().getHeight() - getHeight()/2));
    }

    public float getWidth() {
        return getSize();
    }
    public float getHeight() {
        return getSize();
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public boolean isVegetable() {
        return getType()=="vegetable";
    }
    public boolean isMeat() {
        return getType()=="meat";
    }

    public void setRandSpecie() {
        if (isVegetable())
            specie = getRandValueOf(new String[]{"apple","avocado","banana","beet","bell-pepper","broccoli","brussels-sprouts","cabbage-1","cabbage","carrot","cherry","corn","cucumbers","eggplant","garlic","grapes","hot-pepper","kiwi","lemon","mango","mushrooms","olives","onion","orange","peach","pear","peas","pineapple","pomegranate","potato","pumpkin","radish","raspberries","strawberry","tomato","watermelon"});
        else if (isMeat())
            specie = getRandValueOf(new String[]{"bacon","eggs","fish","ham","ribs","sausage-1","sausage-2","sausage-3","sausage","steak"});
    }
    private void setRandType() {
        if (getRandBool())
            setVegetable(true);
        else
            setMeat(true);
    }

    public void setVegetable(boolean vegetable) {
        this.type = "vegetable";
    }
    public void setMeat(boolean meat) {
        this.type = "meat";
    }

    public String getSpecie() {
        return specie;
    }

    public void setSpecie(String specie) {
        this.specie = specie;
    }
}
