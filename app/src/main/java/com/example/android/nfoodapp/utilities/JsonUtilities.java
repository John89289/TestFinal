package com.example.android.nfoodapp.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by johnkay on 01/08/2017.
 */
// utility functions to help with JSON parsing
public class JsonUtilities {

    // retrieve the product name + brand name from a JSON String which has no product object
    public static String getProductNameFromJsonNoProduct(String offJson) throws JSONException {
        String productName;

        JSONObject offJsonObject = new JSONObject(offJson);
        productName = offJsonObject.getString("product_name");

        String brandName = offJsonObject.getString("brands");
        String fullName = brandName + " - " + productName;

        return fullName;
    }
    // retrieve the product name + brand name from a JSON String which has a product object
    public static String getProductNameFromJson(String offJson) throws JSONException {
        String productName;

        JSONObject offJsonObject = new JSONObject(offJson);
        JSONObject product = offJsonObject.getJSONObject("product");

        productName = product.getString("product_name");
        String brandName = product.getString("brands");
        String fullName = brandName + " - " + productName;

        return fullName;
    }

    // retrieve the product name + brand name from a JSON String which has a product object
    // use when looking up alts
    public static String getProductNameFromAltJson(JSONObject product) throws JSONException {
        String productName;
        productName = product.getString("product_name");
        String brandName = product.getString("brands");
        String fullName = brandName + " - " + productName;

        return fullName;
    }
    // retrieve the NutritionGrade from a JSON String which has a product object
    public static char getNutritionGrade(String offJSon) throws JSONException {

        JSONObject offJsonObject = new JSONObject(offJSon);
        JSONObject product = offJsonObject.getJSONObject("product");
        String nutritionGrade = product.getString("nutrition_grades");

        return nutritionGrade.charAt(0);
    }

    // retrieve the NutritionGrade from a JSON String which has a product object
    public static char getNutritionGradeNoProduct(String offJSon) throws JSONException {

        JSONObject offJsonObject = new JSONObject(offJSon);
        String nutritionGrade = offJsonObject.getString("nutrition_grades");

        return nutritionGrade.charAt(0);
    }

    // retrieve the category of a product from a json string which has a product object
    public static String getCategory(String offJSon) throws JSONException {

        JSONObject offJsonObject = new JSONObject(offJSon);
        JSONObject product = offJsonObject.getJSONObject("product");
        String category = product.getJSONArray("categories_hierarchy").getString(0);

        return category;
    }
    // retrieve the category of a product from a json string which has no product object
    public static String getCategoryNoProduct(String offJSon) throws JSONException {

        JSONObject offJsonObject = new JSONObject(offJSon);
        String category = offJsonObject.getJSONArray("categories_hierarchy").getString(1);

        return category;
    }

    //retrieve nutritional information
    public static void getAndSetNutInfo(String offJSon, Map dataMap) throws JSONException {

        double energy, protein, carb, sugar, fat, salt;

        JSONObject offJsonObject = new JSONObject(offJSon);
        JSONObject product = offJsonObject.getJSONObject("product");
        JSONObject nutrients = product.getJSONObject("nutriments");

        energy = nutrients.getDouble("energy");
        if (nutrients.getString("energy_unit") == "kJ" ){
            energy = kJtoCal(energy);
        }

        protein = nutrients.getDouble("proteins");
        carb = nutrients.getDouble("carbohydrates");
        sugar = nutrients.getDouble("sugars");
        fat = nutrients.getDouble("fat");
        salt = nutrients.getDouble("salt");

        dataMap.put("energy",energy);
        dataMap.put("protein",protein);
        dataMap.put("carbohydrates",carb);
        dataMap.put("sugars",sugar);
        dataMap.put("fat",fat);
        dataMap.put("salt",salt);

    }

    public static void getAndSetNutInfoNoProduct(String offJSon, Map dataMap) throws JSONException {

        double energy, protein, carb, sugar, fat, salt;

        JSONObject offJsonObject = new JSONObject(offJSon);
        JSONObject nutrients = offJsonObject.getJSONObject("nutriments");

        energy = nutrients.getDouble("energy");
        if (nutrients.getString("energy_unit").equals("kJ")){
            energy = kJtoCal(energy);
        }

        protein = nutrients.getDouble("proteins");
        carb = nutrients.getDouble("carbohydrates");
        sugar = nutrients.getDouble("sugars");
        fat = nutrients.getDouble("fat");
        salt = nutrients.getDouble("salt");

        dataMap.put("energy",energy);
        dataMap.put("protein",protein);
        dataMap.put("carbohydrates",carb);
        dataMap.put("sugars",sugar);
        dataMap.put("fat",fat);
        dataMap.put("salt",salt);

    }

    public static double kJtoCal(double kj){
        double calories;
        calories  =  kj / 4.2;
        return calories;
    }
}
