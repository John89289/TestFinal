package com.example.android.nfoodapp.utilities;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by johnkay on 31/07/2017.
 * Utility functions for interacting with Open Food Facts.
 */

public class NetworkUtils {

    private static final String BASE_URL = "https://uk.openfoodfacts.org/api/v0/product/";
    private static final String FORMAT = ".json";
    private static final String BASE_ALT_URL = "https://uk.openfoodfacts.org/";

    // Method to return the entire result from the HTTP response
    public static String getResponseFromHttpUrl(URL url) throws IOException{
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try{
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput){
                return scanner.next();
            } else {
                return null;
            }

        } finally {
            urlConnection.disconnect();
        }
    }
    // build a url for barcode lookup
    public static URL buildUrl(String barcodeQuery){
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(barcodeQuery)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e){
            e.printStackTrace();
        }

        Log.v(NetworkUtils.class.getSimpleName(),"Built URI " + url);

        return url;
    }

    // build a url for finding alternatives lookup
    // finds products in same category with higher nutrition grade
    public static URL buildAltUrl(String categoryQuery, String nutritionGrade){
        Uri builtUri = Uri.parse(BASE_ALT_URL).buildUpon()
                .appendPath("category")
                .appendPath(categoryQuery)
                .appendPath("nutrition-grade")
                .appendPath(nutritionGrade)
                .appendEncodedPath(FORMAT).build();
        URL url = null;
        try{
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(NetworkUtils.class.getSimpleName(),"Built URI " + url);

        return url;
    }

}
