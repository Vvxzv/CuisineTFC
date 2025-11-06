package net.vvxzv.cuisinetfc.common;

public interface TFCNutrientsHolder {
    void addTFCNutrients(float[] nutrients);
    float[] getTFCNutrients();
    void reset();
    void addRottenFood(boolean rotten);
    boolean hasRottenFood();

}
