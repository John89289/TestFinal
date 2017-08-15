package com.example.android.nfoodapp.utilities;

import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;

import com.example.android.nfoodapp.NutritionInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Objects;

/**
 * Created by johnkay on 01/08/2017.
 */
// utility functions to help with JSON parsing
public class JsonUtilities {


    // compare sugar of a lookup product and an alternate product
    public static double compareSugar(String origJson, String altJson) throws JSONException{

        JSONObject origJsonObject = new JSONObject(origJson);
        JSONObject altJsonObject = new JSONObject(altJson);
        JSONObject origProduct = origJsonObject.getJSONObject("product");
        double origSugar = origProduct.getDouble("sugars");
        double altSugar = altJsonObject.getDouble("sugars");

        return altSugar - origSugar;
    }
    // take in a a json string and create a nutritionInfo object
    // fill in all the fields with names and nutrition info
    public static NutritionInfo generateNewNutritionInfo(String offJson){

        NutritionInfo productInfo = new NutritionInfo();

        try {
            JSONObject offJsonObject = new JSONObject(offJson);
            JSONObject product = offJsonObject.getJSONObject("product");
            JSONObject nutriments = product.getJSONObject("nutriments");
            JSONObject nutrientLevels = product.getJSONObject("nutrient_levels");

            // get and format product name (brand - product) then set
            String productName = product.getString("product_name");
            String brandName = product.getString("brands");
            String fullName = brandName + " - " + productName;

            productInfo.setProductName(fullName);

            //get + set nutrition grade
            String nutritionGrade = product.getString("nutrition_grades");
            productInfo.setNutritionGrade(nutritionGrade);

            //get+ set energy /w units
            long energy = nutriments.getLong("energy");
            String energyUnit = nutriments.getString("energy_unit");

            productInfo.setEnergy(energy);
            productInfo.setEnergyUnit(energyUnit);

            //get+ set protein /w units
            long protein = nutriments.getLong("proteins");
            String proteinUnit = nutriments.getString("proteins_unit");

            productInfo.setProtein(protein);
            productInfo.setProteinUnit(proteinUnit);

            ///get+ set fat /w units
            long fat = nutriments.getLong("fat");
            String fatUnit = nutriments.getString("fat_unit");
            int fatLevel = nutrientLevelToInt(nutrientLevels.getString("fat"));

            productInfo.setFat(fat);
            productInfo.setFatUnit(fatUnit);
            productInfo.setFatLevel(fatLevel);


            //get+ set carb /w units
            long carbohydrates = nutriments.getLong("carbohydrates");
            String carbohydratesUnit = nutriments.getString("carbohydrates_unit");

            productInfo.setCarbohydrates(carbohydrates);
            productInfo.setCarbohydratesUnit(carbohydratesUnit);

            //get+ set sugar /w units
            long sugar = nutriments.getLong("sugars");
            String sugarUnit = nutriments.getString("sugars_unit");
            int sugarLevel = nutrientLevelToInt(nutrientLevels.getString("sugars"));

            productInfo.setSugar(sugar);
            productInfo.setSugarUnit(sugarUnit);
            productInfo.setSugarLevel(sugarLevel);


            //get categories
            int catLength = product.getJSONArray("categories_hierarchy").length();
            String[] categories = new String[catLength];
            for (int i = 0; i < catLength; i++){
                categories[i] = product.getJSONArray("categories_hierarchy").getString(i);
                Log.d("test",categories[i]);
            }

            //set categories
            productInfo.setCategoryHierarchy(categories);


            //get+ set salt /w units
            double salt = nutriments.getDouble("salt");
            String saltUnit = nutriments.getString("salt_unit");
            int saltLevel = nutrientLevelToInt(nutrientLevels.getString("salt"));

            productInfo.setSalt(salt);
            productInfo.setSaltUnit(saltUnit);
            productInfo.setSaltLevel(saltLevel);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return productInfo;

    }

    public static NutritionInfo generateNewNutritionInfoNoProduct(String offJson){

        NutritionInfo productInfo = new NutritionInfo();

        try {
            JSONObject offJsonObject = new JSONObject(offJson);
            JSONObject nutriments = offJsonObject.getJSONObject("nutriments");
            JSONObject nutrientLevels = offJsonObject.getJSONObject("nutrient_levels");

            // get and format product name (brand - product) then set
            String productName = offJsonObject.getString("product_name");
            String brandName = offJsonObject.getString("brands");
            String fullName = brandName + " - " + productName;

            productInfo.setProductName(fullName);

            //get + set nutrition grade
            String nutritionGrade = offJsonObject.getString("nutrition_grades");
            productInfo.setNutritionGrade(nutritionGrade);

            //get+ set energy /w units
            long energy = nutriments.getLong("energy");
            String energyUnit = nutriments.getString("energy_unit");

            productInfo.setEnergy(energy);
            productInfo.setEnergyUnit(energyUnit);

            //get+ set protein /w units
            long protein = nutriments.getLong("proteins");
            String proteinUnit = nutriments.getString("proteins_unit");

            productInfo.setProtein(protein);
            productInfo.setProteinUnit(proteinUnit);

            //get+ set fat /w units
            long fat = nutriments.getLong("fat");
            String fatUnit = nutriments.getString("fat_unit");
            int fatLevel = nutrientLevelToInt(nutrientLevels.getString("fat"));

            productInfo.setFat(fat);
            productInfo.setFatUnit(fatUnit);
            productInfo.setFatLevel(fatLevel);


            //get+ set carb /w units
            long carbohydrates = nutriments.getLong("carbohydrates");
            String carbohydratesUnit = nutriments.getString("carbohydrates_unit");

            productInfo.setCarbohydrates(carbohydrates);
            productInfo.setCarbohydratesUnit(carbohydratesUnit);

            //get+ set sugar /w units
            long sugar = nutriments.getLong("sugars");
            String sugarUnit = nutriments.getString("sugars_unit");
            int sugarLevel = nutrientLevelToInt(nutrientLevels.getString("sugars"));

            productInfo.setSugar(sugar);
            productInfo.setSugarUnit(sugarUnit);
            productInfo.setSugarLevel(sugarLevel);

            //get+ set salt /w units
            double salt = nutriments.getDouble("salt");
            String saltUnit = nutriments.getString("salt_unit");
            int saltLevel = nutrientLevelToInt(nutrientLevels.getString("salt"));

            productInfo.setSalt(salt);
            productInfo.setSaltUnit(saltUnit);
            productInfo.setSaltLevel(saltLevel);

            //get categories
            int catLength = offJsonObject.getJSONArray("categories_hierarchy").length();
            String[] categories = new String[catLength];
            for (int i = 0; i < catLength; i++){
                categories[i] = offJsonObject.getJSONArray("categories_hierarchy").getString(i);
                Log.d("test",categories[i]);
            }

            //set categories
            productInfo.setCategoryHierarchy(categories);




        } catch (JSONException e) {
            e.printStackTrace();
        }



        return productInfo;

    }

    public static NutritionInfo[] generateNewNutritionInfoNoProductFromAltResults(String altResults){
        NutritionInfo[] newNutInfo = null;
        try {
            JSONObject resultsObject = new JSONObject(altResults);
            int numProducts = resultsObject.getInt("count");
            JSONArray productArray = resultsObject.getJSONArray("products");

            newNutInfo = new NutritionInfo[numProducts];
            for (int i = 0; i < numProducts; i++ ){
                newNutInfo[i] = generateNewNutritionInfoNoProduct(productArray.get(0).toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return newNutInfo;
    }
    // takes in a string from json results and returns an int key  ALSO converts grade to a key in same way
    public static int nutrientLevelToInt(String level){
        int levelNumber = 0;

        if (level == null) {
            return  levelNumber;
        }

        switch (level) {
            case "low":case "a":case "b":
                levelNumber = 1;
                break;
            case "moderate":case "c":
                levelNumber = 2;
                break;
            case "high":case "d":case "e":
                levelNumber = 3;
                break;
        }
        Log.d("tag", "nutrientLevelToInt: " + levelNumber);
        return levelNumber;
    }

    // takes in a int key and sets the textview to that colour  (not a json util not work making seperate class for )
    public static void setColour(TextView text, int key){

        switch (key) {
            case 1:
                text.setTextColor(Color.GREEN);
                break;
            case 2:
                text.setTextColor(Color.rgb(255,165,0));
                break;
            case 3:
                text.setTextColor(Color.RED);
                break;
        }
    }


}
