package com.example.android.nfoodapp;

import android.content.Context;
import android.content.Intent;
import android.net.Network;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;


import com.example.android.nfoodapp.utilities.JsonUtilities;
import com.example.android.nfoodapp.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DetailActivity extends AppCompatActivity implements RecyclerAdapter.AltAdapterOnClickHandler {

    private String mOffJson;
    private TextView mDataDisplay;
    private TextView mProductNameDisplay;
    private String productName;
    private char nutritionGrade;
    private static final int SEARCH_LOADER = 12;
    private RecyclerView mAltResultsRecyclerView;
    private LinearLayoutManager mAltResultsLinearLayoutManager;
    private RecyclerAdapter mAdapter;
    private ProgressBar mProgressBarLoading;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mProductNameDisplay = (TextView) findViewById(R.id.tv_product_name_display);
        mAltResultsRecyclerView = (RecyclerView) findViewById(R.id.rv_results_recycler_view);
        mProgressBarLoading = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        mAltResultsLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAltResultsRecyclerView.setLayoutManager(mAltResultsLinearLayoutManager);
        mAltResultsRecyclerView.setHasFixedSize(true);
        mAdapter = new RecyclerAdapter(this);
        mAltResultsRecyclerView.setAdapter(mAdapter);

        HashMap<String,Double> nutData = new HashMap();


        //retrieve intent + data from intent
        Intent intentThatStartedThisActivity = getIntent();

        if(intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)){

                mOffJson = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
                // try to parse product name from json
                try{
                    productName = JsonUtilities.getProductNameFromJson(mOffJson);
                    nutritionGrade = JsonUtilities.getNutritionGrade(mOffJson);
                    JsonUtilities.getAndSetNutInfo(mOffJson,nutData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String title = productName + " Grade: " + Character.toString(nutritionGrade);

                mProductNameDisplay.setText(title);
                //mDataDisplay.setText("Alternatives: \n" );
                makeAltQuery(mOffJson);
                // this probably wont work
            }
        }
    }

    // takes in a json string (for a product) and looks up alternatives in same category
    private void makeAltQuery(String jsonString){
        // retrieve the category of a product
        String altCat = null;
        char altChar = 'n';
        URL altSearchUrl;
        try {
            altCat = JsonUtilities.getCategory(jsonString);
            altChar = JsonUtilities.getNutritionGrade(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(altCat == null){
            return;
        }
        //search for higher rated foods in the same category
        for (int i = 97; i < altChar; i++){
            String altGrade = Character.toString((char) i);

            altSearchUrl = NetworkUtils.buildAltUrl(altCat,altGrade);
            new AltQueryTask().execute(altSearchUrl);
        }

    }
    // Async task for alternate foods lookup
    public class AltQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //loadAltDataView();
        }

        @Override
        protected String doInBackground(URL... urls) {
            // use built url to query open food facts and return json
            URL searchUrl = urls[0];
            String offJsonAltResults = null;
            try{
                offJsonAltResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e){
                e.printStackTrace();
            }
            return offJsonAltResults;
        }

        @Override
        protected void onPostExecute(String offJsonAltResults) {
            // get array of products (results) and append product names of each to data Textview
            if (offJsonAltResults != null && !offJsonAltResults.equals("")){

                try {
                    JSONObject results = new JSONObject(offJsonAltResults);
                    JSONArray products = results.getJSONArray("products");
                    String productsString[] = new String[products.length()];

                    for (int i = 0; i < products.length(); i++){
                        productsString[i] = products.getString(i);
                    }
                    mAdapter.setmAltData(productsString);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //mDataDisplay.setText(offJsonAltResults);
            }
        }
    }

    @Override
    public void onClick(String altData) {
        Context context = this;
        String category = null;
        try {
           category = JsonUtilities.getCategoryNoProduct(altData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Toast.makeText(context,category, Toast.LENGTH_SHORT)
                .show();
    }
    // hide loading animation and show data
    private void showAltDataView(){
        mProgressBarLoading.setVisibility(View.INVISIBLE);
        mAltResultsRecyclerView.setVisibility(View.VISIBLE);
    }
    // hide data and show loading animation
    private void loadAltDataView(){
        mAltResultsRecyclerView.setVisibility(View.INVISIBLE);
        mProgressBarLoading.setVisibility(View.VISIBLE);
    }



}
