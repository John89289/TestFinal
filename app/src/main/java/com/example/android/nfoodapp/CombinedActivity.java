package com.example.android.nfoodapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.nfoodapp.utilities.JsonUtilities;
import com.example.android.nfoodapp.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;

public class CombinedActivity extends AppCompatActivity implements RecyclerAdapter.AltAdapterOnClickHandler, View.OnClickListener{



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
    //private NutritionInfo productNutInfo;
    private TextView mNutritionNutGradeTextView;
    private TextView mNutritionProductNameTextView;
    private TextView mCaloriesTextView;
    private TextView mFatValueTextView;
    private TextView mSaltValueTextView;
    private TextView mSugarValueTextView;
    private NutritionInfo productNutInfo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combined);

        //detailactivity variables
        mProductNameDisplay = (TextView) findViewById(R.id.tv_product_name_display);
        mDataDisplay = (TextView) findViewById(R.id.tv_contribute_display);
        mAltResultsRecyclerView = (RecyclerView) findViewById(R.id.rv_results_recycler_view);
        mProgressBarLoading = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        mAltResultsLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAltResultsRecyclerView.setLayoutManager(mAltResultsLinearLayoutManager);
        mAltResultsRecyclerView.setHasFixedSize(true);
        mAdapter = new RecyclerAdapter(this);
        mAltResultsRecyclerView.setAdapter(mAdapter);

        /// nutritionactivity variables
        //mNutritionDataTextView = (TextView) findViewById(R.id.tv_results_display);
        mNutritionProductNameTextView = (TextView) findViewById(R.id.textViewProductName);
        mNutritionNutGradeTextView = (TextView) findViewById(R.id.textViewNutGrade);
        mCaloriesTextView = (TextView) findViewById(R.id.textViewCalories);
        mFatValueTextView = (TextView) findViewById(R.id.textViewFatValue);
        mSaltValueTextView = (TextView) findViewById(R.id.textViewSaltValue);
        mSugarValueTextView = (TextView) findViewById(R.id.textViewSugarValue);

        //retrieve intent + data from intent
        Intent intentThatStartedThisActivity = getIntent();

        if(intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("nutInfo")){

                productNutInfo = intentThatStartedThisActivity.getParcelableExtra("nutInfo");

                // if product not found refer user to OFF contribute page
                if (productNutInfo.getProductName() == null) {

                    /// set text to /uk.openfoodfacts.org/contribute
                }
                else{



                    String title = productNutInfo.getProductName();
                    mNutritionProductNameTextView.setText(title);
                    mNutritionNutGradeTextView.setText(productNutInfo.getNutritionGrade().toUpperCase());

                    int gradeLevel = JsonUtilities.nutrientLevelToInt(productNutInfo.getNutritionGrade());
                    JsonUtilities.setColour(mNutritionNutGradeTextView,gradeLevel);

                    String Calories = Long.toString(productNutInfo.getEnergy()) + " " + productNutInfo.getEnergyUnit();
                    mCaloriesTextView.setText(Calories);
                    Log.d("value of salt",String.valueOf(productNutInfo.getSalt()));

                    String Fat = String.valueOf(productNutInfo.getFat()) + productNutInfo.getFatUnit();
                    mFatValueTextView.setText(Fat);
                    JsonUtilities.setColour(mFatValueTextView,productNutInfo.getFatLevel());

                    double saltValue = productNutInfo.getSalt();
                    String roundedSaltValue = new DecimalFormat("#.##").format(saltValue);
                    String Salt = roundedSaltValue+ productNutInfo.getSaltUnit();
                    mSaltValueTextView.setText(Salt);
                    JsonUtilities.setColour(mSaltValueTextView,productNutInfo.getSaltLevel());

                    String Sugar = String.valueOf(productNutInfo.getSugar()) + productNutInfo.getSugarUnit();
                    mSugarValueTextView.setText(Sugar);
                    JsonUtilities.setColour(mSugarValueTextView,productNutInfo.getSugarLevel());



                }
            }
        }



        // mOffJson = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
        //productNutInfo = intentThatStartedThisActivity.getParcelableExtra(Intent.EXTRA_TEXT);

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




        findViewById(R.id.buttonFindAlt).setOnClickListener(this);
/*
        // my_child_toolbar is defined in the layout file
        Toolbar detailToolbar =
                (Toolbar) findViewById(R.id.nutrition_toolbar);
        setSupportActionBar(detailToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
*/




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
                new CombinedActivity.AltQueryTask().execute(altSearchUrl);
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
        Class destinationClass = CombinedActivity.class;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_home){
            Intent newIntent = new Intent(this, MainActivity.class);
            newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(newIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        Log.d("'test","here");
        int itemThatWasClickedId = view.getId();
        if (itemThatWasClickedId == R.id.buttonFindAlt){
            Context context = this;
            Class destinationClass = DetailActivity.class;
            Intent intentToStartDetailActivity = new Intent(context,destinationClass);
            intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT,productNutInfo);
            startActivity(intentToStartDetailActivity);
        }

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_home){
            Intent newIntent = new Intent(this, MainActivity.class);
            newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(newIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/






}

