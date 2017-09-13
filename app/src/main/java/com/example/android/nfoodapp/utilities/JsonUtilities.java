package com.example.android.nfoodapp.utilities;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.android.nfoodapp.CircularTextView;
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
    public static double compareSugar(NutritionInfo origProduct, NutritionInfo altProduct){
        return  altProduct.getSugar() - origProduct.getSugar();
    }
    // take in a a json string and create a nutritionInfo object
    // fill in all the fields with names and nutrition info
    public static NutritionInfo generateNewNutritionInfo(String offJson){

        NutritionInfo productInfo = new NutritionInfo();

        try {
            JSONObject offJsonObject = new JSONObject(offJson);
            JSONObject product = offJsonObject.getJSONObject("product");
            JSONArray completionStates = product.getJSONArray("states_hierarchy");
            Log.d("tag", "generateNewNutritionInfo: states 0" + completionStates.getString(0));
            Log.d("tag", "generateNewNutritionInfo: states 0" + completionStates.getString(1));
            if (!completionStates.toString().contains("en:nutrition-facts-completed")){
                return null;
            }

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
            double energy = nutriments.getDouble("energy");
            String energyUnit = nutriments.getString("energy_unit");

            productInfo.setEnergy(energy);
            productInfo.setEnergyUnit(energyUnit);

            //get+ set protein /w units
            double protein = nutriments.getDouble("proteins");
            String proteinUnit = nutriments.getString("proteins_unit");

            productInfo.setProtein(protein);
            productInfo.setProteinUnit(proteinUnit);

            ///get+ set fat /w units
            double fat = nutriments.getDouble("fat");
            String fatUnit = nutriments.getString("fat_unit");
            int fatLevel = nutrientLevelToInt(nutrientLevels.getString("fat"));

            productInfo.setFat(fat);
            productInfo.setFatUnit(fatUnit);
            productInfo.setFatLevel(fatLevel);


            //get+ set carb /w units
            double carbohydrates = nutriments.getDouble("carbohydrates");
            String carbohydratesUnit = nutriments.getString("carbohydrates_unit");

            productInfo.setCarbohydrates(carbohydrates);
            productInfo.setCarbohydratesUnit(carbohydratesUnit);

            //get+ set sugar /w units
            double sugar = nutriments.getDouble("sugars");
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
            Log.d("salt tag", "generateNewNutritionInfo: " + salt);
            String saltUnit = "g";
            int saltLevel = nutrientLevelToInt(nutrientLevels.getString("salt"));

            productInfo.setSalt(salt);
            productInfo.setSaltUnit(saltUnit);
            productInfo.setSaltLevel(saltLevel);

            //get and set image Url



        } catch (JSONException e) {
            e.printStackTrace();
        }

        return productInfo;

    }

    public static NutritionInfo generateNewNutritionInfoNoProduct(String offJson){

        NutritionInfo productInfo = new NutritionInfo();

        try {
            JSONObject offJsonObject = new JSONObject(offJson);
            // if nutrition facts are completed
           /* JSONArray completionStates = offJsonObject.getJSONArray("states_hierarchy");
            if (!Objects.equals(completionStates.getString(1), "en:nutrition-facts-completed")){
                return null;
            }*/
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
            double energy = nutriments.getDouble("energy");
            String energyUnit = nutriments.getString("energy_unit");

            productInfo.setEnergy(energy);
            productInfo.setEnergyUnit(energyUnit);

            //get+ set protein /w units
            double protein = nutriments.getDouble("proteins");
            String proteinUnit = nutriments.getString("proteins_unit");

            productInfo.setProtein(protein);
            productInfo.setProteinUnit(proteinUnit);

            //get+ set fat /w units
            double fat = nutriments.getDouble("fat");
            String fatUnit = nutriments.getString("fat_unit");
            int fatLevel = nutrientLevelToInt(nutrientLevels.getString("fat"));

            productInfo.setFat(fat);
            productInfo.setFatUnit(fatUnit);
            productInfo.setFatLevel(fatLevel);


            //get+ set carb /w units
            double carbohydrates = nutriments.getDouble("carbohydrates");
            String carbohydratesUnit = nutriments.getString("carbohydrates_unit");

            productInfo.setCarbohydrates(carbohydrates);
            productInfo.setCarbohydratesUnit(carbohydratesUnit);

            //get+ set sugar /w units
            double sugar = nutriments.getDouble("sugars");
            String sugarUnit = nutriments.getString("sugars_unit");
            int sugarLevel = nutrientLevelToInt(nutrientLevels.getString("sugars"));

            productInfo.setSugar(sugar);
            productInfo.setSugarUnit(sugarUnit);
            productInfo.setSugarLevel(sugarLevel);

            //get categories
            int catLength = offJsonObject.getJSONArray("categories_hierarchy").length();
            String[] categories = new String[catLength];
            for (int i = 0; i < catLength; i++){
                categories[i] = offJsonObject.getJSONArray("categories_hierarchy").getString(i);
                Log.d("test",categories[i]);
            }

            //set categories
            productInfo.setCategoryHierarchy(categories);


            //get+ set salt /w units
            double salt = nutriments.getDouble("salt");
            String saltUnit = "g";
            int saltLevel = nutrientLevelToInt(nutrientLevels.getString("salt"));

            productInfo.setSalt(salt);
            productInfo.setSaltUnit(saltUnit);
            productInfo.setSaltLevel(saltLevel);





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
    public static void setColour(View view, int key){

        switch (key) {
            case 1:
                view.setBackgroundColor(Color.rgb(151, 215, 0));
                break;
            case 2:
                view.setBackgroundColor(Color.rgb(240,179,54));
                break;
            case 3:
                view.setBackgroundColor(Color.rgb(239,51,64));
                break;
        }
    }

    public static void setColourCircle(CircularTextView text, int key){
        switch (key) {
            case 1:
                text.setSolidColor(Color.rgb(151, 215, 0));
                break;
            case 2:
                text.setSolidColor(Color.rgb(240,179,54));
                break;
            case 3:
                text.setSolidColor(Color.rgb(239,51,64));
                break;
        }
    }



}
