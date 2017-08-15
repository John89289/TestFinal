package com.example.android.nfoodapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by johnkay on 14/08/2017.
 */
// simple class to hold all the information about a particular product. and provides getters for
// for nutritional information
public class NutritionInfo implements Parcelable {

    private String productName;
    private String nutritionGrade;

    private String[] categoryHierarchy;

    private long energy;
    private String energyUnit;

    private long fat;
    private String fatUnit;
    private int fatLevel;

    private long protein;
    private String proteinUnit;

    private long carbohydrates;
    private String carbohydratesUnit;

    private long sugar;
    private String sugarUnit;
    private int sugarLevel;

    private double salt;
    private String saltUnit;
    private int saltLevel;


    protected NutritionInfo(Parcel in) {
        productName = in.readString();
        nutritionGrade = in.readString();
        categoryHierarchy = in.createStringArray();
        energy = in.readLong();
        energyUnit = in.readString();
        fat = in.readLong();
        fatUnit = in.readString();
        fatLevel = in.readInt();
        protein = in.readLong();
        proteinUnit = in.readString();
        carbohydrates = in.readLong();
        carbohydratesUnit = in.readString();
        sugar = in.readLong();
        sugarUnit = in.readString();
        sugarLevel = in.readInt();
        salt = in.readDouble();
        saltUnit = in.readString();
        saltLevel = in.readInt();
    }

    public  NutritionInfo(){

    }

    public static final Creator<NutritionInfo> CREATOR = new Creator<NutritionInfo>() {
        @Override
        public NutritionInfo createFromParcel(Parcel in) {
            return new NutritionInfo(in);
        }

        @Override
        public NutritionInfo[] newArray(int size) {
            return new NutritionInfo[size];
        }
    };

    public String getProductName() {
        return productName;
    }

    public String getNutritionGrade() {
        return nutritionGrade;
    }

    public long getEnergy() {
        return energy;
    }

    public String getEnergyUnit() {
        return energyUnit;
    }

    public long getFat() {
        return fat;
    }

    public String getFatUnit() {
        return fatUnit;
    }

    public long getProtein() {
        return protein;
    }

    public String getProteinUnit() {
        return proteinUnit;
    }

    public long getCarbohydrates() {
        return carbohydrates;
    }

    public String getCarbohydratesUnit() {
        return carbohydratesUnit;
    }

    public long getSugar() {
        return sugar;
    }

    public String getSugarUnit() {
        return sugarUnit;
    }

    public double getSalt() {
        return salt;
    }

    public String getSaltUnit() {
        return saltUnit;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setNutritionGrade(String nutritionGrade) {
        this.nutritionGrade = nutritionGrade;
    }

    public void setEnergy(long energy) {
        this.energy = energy;
    }

    public void setEnergyUnit(String energyUnit) {
        this.energyUnit = energyUnit;
    }

    public void setFat(long fat) {
        this.fat = fat;
    }

    public void setFatUnit(String fatUnit) {
        this.fatUnit = fatUnit;
    }

    public void setProtein(long protein) {
        this.protein = protein;
    }

    public void setProteinUnit(String proteinUnit) {
        this.proteinUnit = proteinUnit;
    }

    public void setCarbohydrates(long carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public void setCarbohydratesUnit(String carbohydratesUnit) {
        this.carbohydratesUnit = carbohydratesUnit;
    }

    public void setSugar(long sugar) {
        this.sugar = sugar;
    }

    public void setSugarUnit(String sugarUnit) {
        this.sugarUnit = sugarUnit;
    }

    public void setSalt(double salt) {
        this.salt = salt;
    }

    public void setSaltUnit(String saltUnit) {
        this.saltUnit = saltUnit;
    }




    public String[] getCategoryHierarchy() {
        return categoryHierarchy;
    }

    public void setCategoryHierarchy(String[] categoryHierarchy) {
        this.categoryHierarchy = categoryHierarchy;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(productName);
        parcel.writeString(nutritionGrade);
        parcel.writeStringArray(categoryHierarchy);
        parcel.writeLong(energy);
        parcel.writeString(energyUnit);
        parcel.writeLong(fat);
        parcel.writeString(fatUnit);
        parcel.writeInt(fatLevel);
        parcel.writeLong(protein);
        parcel.writeString(proteinUnit);
        parcel.writeLong(carbohydrates);
        parcel.writeString(carbohydratesUnit);
        parcel.writeLong(sugar);
        parcel.writeString(sugarUnit);
        parcel.writeInt(sugarLevel);
        parcel.writeDouble(salt);
        parcel.writeString(saltUnit);
        parcel.writeInt(saltLevel);
    }

    public int getFatLevel() {
        return fatLevel;
    }

    public void setFatLevel(int fatLevel) {
        this.fatLevel = fatLevel;
    }

    public int getSugarLevel() {
        return sugarLevel;
    }

    public void setSugarLevel(int sugarLevel) {
        this.sugarLevel = sugarLevel;
    }

    public int getSaltLevel() {
        return saltLevel;
    }

    public void setSaltLevel(int saltLevel) {
        this.saltLevel = saltLevel;
    }
}
