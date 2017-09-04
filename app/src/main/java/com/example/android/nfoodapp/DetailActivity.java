package com.example.android.nfoodapp;

import android.content.Context;
import android.content.Intent;
import android.net.Network;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
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
    private NutritionInfo productNutInfo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mProductNameDisplay = (TextView) findViewById(R.id.tv_product_name_display);
        mDataDisplay = (TextView) findViewById(R.id.tv_contribute_display);
        mAltResultsRecyclerView = (RecyclerView) findViewById(R.id.rv_results_recycler_view);
        mProgressBarLoading = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        mAltResultsLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAltResultsRecyclerView.setLayoutManager(mAltResultsLinearLayoutManager);
        mAltResultsRecyclerView.setHasFixedSize(true);
        mAdapter = new RecyclerAdapter(this);
        mAltResultsRecyclerView.setAdapter(mAdapter);

       /* // my_child_toolbar is defined in the layout file
        Toolbar detailToolbar =
                (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(detailToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);*/




        //retrieve intent + data from intent
        Intent intentThatStartedThisActivity = getIntent();

        if(intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)){

               // mOffJson = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
                productNutInfo = intentThatStartedThisActivity.getParcelableExtra(Intent.EXTRA_TEXT);

                // if product not found refer user to OFF contribute page
                if (productNutInfo.getProductName() == null) {

                    mProductNameDisplay.setText("Product not found");
                    mDataDisplay.setText(R.string.contribute_url);
                    mDataDisplay.setVisibility(View.VISIBLE);

                }
                else{
                    String title = productNutInfo.getProductName() + " Grade: " + productNutInfo.getNutritionGrade().toUpperCase();

                    mProductNameDisplay.setText(title);
                    //mDataDisplay.setText("Alternatives: \n" );
                    makeAltQuery(productNutInfo);
                }
            }
        }
    }

    // takes in a json string (for a product) and looks up alternatives in same category
    private void makeAltQuery(NutritionInfo offNutInfo){
        // retrieve the category of a product
        String altCat = null;
        char altChar = offNutInfo.getNutritionGrade().charAt(0);
        String[] categories = offNutInfo.getCategoryHierarchy();
        URL altSearchUrl;

       /* try {
            categoryHierarchy = JsonUtilities.getCategoryHierarchy(jsonString);
            //altCat = JsonUtilities.getCategory(jsonString);
            altChar = JsonUtilities.getNutritionGrade(jsonString);

        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        //search for higher rated foods in the same category
        Log.d("tag", "makeAltQuery: " + categories.length);
        for (int j = categories.length - 1; j >= 0; j--){
            Log.d("tag",Character.toString(altChar));
            for (int i = 97; i < altChar; i++) {
                String altGrade = Character.toString((char) i);
                altCat = offNutInfo.getCategoryHierarchy()[j];
                Log.d("tag",altGrade + " - " + altCat);

                altSearchUrl = NetworkUtils.buildAltUrl(altCat,altGrade);
                new AltQueryTask().execute(altSearchUrl);
            }
            //altSearchUrl = NetworkUtils.buildAltUrl(altCat,altGrade);
           // new AltQueryTask().execute(altSearchUrl);
        }
        if(altCat == null){
            return;
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
            if (offJsonAltResults != null && !offJsonAltResults.equals("") && mAdapter.getItemCount() < 20){

                NutritionInfo[] NutInfoResults = null;
                NutInfoResults = JsonUtilities.generateNewNutritionInfoNoProductFromAltResults(offJsonAltResults);
                mAdapter.setmAltData(NutInfoResults);

            }
        }
    }

    @Override
    public void onClick(NutritionInfo altData) {
        Context context = this;
        Class destinationClass = NutritionActivity.class;

        // go to detailed activity
        Intent intentToStartNutActivity = new Intent(context,destinationClass);
        intentToStartNutActivity.putExtra("nutInfo",altData);
        startActivity(intentToStartNutActivity);

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

    public String getOffJson(){
        return mOffJson;
    }




}
