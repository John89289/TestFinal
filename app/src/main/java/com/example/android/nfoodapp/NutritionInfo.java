package com.example.android.nfoodapp;

import android.graphics.Bitmap;
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

    private double energy;
    private String energyUnit;

    private double fat;
    private String fatUnit;
    private int fatLevel;

    private double protein;
    private String proteinUnit;

    private double carbohydrates;
    private String carbohydratesUnit;

    private double sugar;
    private String sugarUnit;
    private int sugarLevel;

    private double salt;
    private String saltUnit;
    private int saltLevel;


   // private String imageUrl;


    protected NutritionInfo(Parcel in) {
        productName = in.readString();
        nutritionGrade = in.readString();
        categoryHierarchy = in.createStringArray();
        energy = in.readDouble();
        energyUnit = in.readString();
        fat = in.readDouble();
        fatUnit = in.readString();
        fatLevel = in.readInt();
        protein = in.readDouble();
        proteinUnit = in.readString();
        carbohydrates = in.readDouble();
        carbohydratesUnit = in.readString();
        sugar = in.readDouble();
        sugarUnit = in.readString();
        sugarLevel = in.readInt();
        salt = in.readDouble();
        saltUnit = in.readString();
        saltLevel = in.readInt();
      //  imageUrl = in.readString();
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

    public double getEnergy() {
        return energy;
    }

    public String getEnergyUnit() {
        return energyUnit;
    }

    public double getFat() {
        return fat;
    }

    public String getFatUnit() {
        return fatUnit;
    }

    public double getProtein() {
        return protein;
    }

    public String getProteinUnit() {
        return proteinUnit;
    }

    public double getCarbohydrates() {
        return carbohydrates;
    }

    public String getCarbohydratesUnit() {
        return carbohydratesUnit;
    }

    public double getSugar() {
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

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public void setEnergyUnit(String energyUnit) {
        this.energyUnit = energyUnit;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public void setFatUnit(String fatUnit) {
        this.fatUnit = fatUnit;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public void setProteinUnit(String proteinUnit) {
        this.proteinUnit = proteinUnit;
    }

    public void setCarbohydrates(double carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public void setCarbohydratesUnit(String carbohydratesUnit) {
        this.carbohydratesUnit = carbohydratesUnit;
    }

    public void setSugar(double sugar) {
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

  /*  public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    */




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
        parcel.writeDouble(energy);
        parcel.writeString(energyUnit);
        parcel.writeDouble(fat);
        parcel.writeString(fatUnit);
        parcel.writeInt(fatLevel);
        parcel.writeDouble(protein);
        parcel.writeString(proteinUnit);
        parcel.writeDouble(carbohydrates);
        parcel.writeString(carbohydratesUnit);
        parcel.writeDouble(sugar);
        parcel.writeString(sugarUnit);
        parcel.writeInt(sugarLevel);
        parcel.writeDouble(salt);
        parcel.writeString(saltUnit);
        parcel.writeInt(saltLevel);
     //   parcel.writeString(imageUrl);
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
