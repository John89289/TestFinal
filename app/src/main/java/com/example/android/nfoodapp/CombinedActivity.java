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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.nfoodapp.utilities.JsonUtilities;
import com.example.android.nfoodapp.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;

public class CombinedActivity extends AppCompatActivity implements RecyclerAdapter.AltAdapterOnClickHandler{



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
    private CircularTextView mNutritionNutGradeTextView;
    private TextView mNutritionProductNameTextView;
    private TextView mCaloriesTextView;
    private TextView mFatValueTextView;
    private TextView mSaltValueTextView;
    private TextView mSugarValueTextView;
    private TextView mProteinValueTextView;
    private TextView mCarbohydrateValueTextView;
    private ImageView mOffLogoImageView;
    private NutritionInfo productNutInfo;
    private View mFatColorView;
    private View mSugarColorView;
    private View mSaltColorView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combined);

        // detailactivity variables
        mProductNameDisplay = (TextView) findViewById(R.id.tv_product_name_display);
        mDataDisplay = (TextView) findViewById(R.id.tv_contribute_display);
        mAltResultsRecyclerView = (RecyclerView) findViewById(R.id.rv_results_recycler_view);
        mProgressBarLoading = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mOffLogoImageView = (ImageView) findViewById(R.id.iv_off_logo_contribute);

        mAltResultsLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAltResultsRecyclerView.setLayoutManager(mAltResultsLinearLayoutManager);
        mAltResultsRecyclerView.setHasFixedSize(true);
        mAdapter = new RecyclerAdapter(this);
        mAltResultsRecyclerView.setAdapter(mAdapter);

        /// nutritionactivity variables
        //mNutritionDataTextView = (TextView) findViewById(R.id.tv_results_display);
        mNutritionProductNameTextView = (TextView) findViewById(R.id.textViewProductName);
        mNutritionNutGradeTextView = (CircularTextView) findViewById(R.id.textViewNutGrade);
        mCaloriesTextView = (TextView) findViewById(R.id.textViewCalories);
        mFatValueTextView = (TextView) findViewById(R.id.textViewFat);
        mSaltValueTextView = (TextView) findViewById(R.id.textViewSalt);
        mSugarValueTextView = (TextView) findViewById(R.id.textViewSugar);
        mProteinValueTextView = (TextView) findViewById(R.id.textViewProtein);
        mCarbohydrateValueTextView = (TextView) findViewById(R.id.textViewCarbohydrate);
        mFatColorView = findViewById(R.id.textViewFatColor);
        mSugarColorView = findViewById(R.id.textViewSugarColor);
        mSaltColorView = findViewById(R.id.textViewSaltColor);

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
                    int key = JsonUtilities.nutrientLevelToInt(productNutInfo.getNutritionGrade());
                    JsonUtilities.setColourCircle(mNutritionNutGradeTextView,key);

                    // set energy value + units
                    double EnergyValue = productNutInfo.getEnergy();
                    String roundedEnergyValue = new DecimalFormat("#.##").format(EnergyValue);
                    String Energy = roundedEnergyValue + " " + productNutInfo.getEnergyUnit();
                    mCaloriesTextView.setText(Energy);

                    // set Fat value, unit and colour
                    double fatValue = productNutInfo.getFat();
                    String roundedFatValue = new DecimalFormat("#.##").format(fatValue);
                    String Fat = roundedFatValue + productNutInfo.getFatUnit();
                    mFatValueTextView.setText("Fat: " + Fat);
                    JsonUtilities.setColour( mFatColorView, productNutInfo.getFatLevel());

                    // set Salt value, unit and colour
                    double saltValue = productNutInfo.getSalt();
                    String roundedSaltValue = new DecimalFormat("#.##").format(saltValue);
                    String Salt = roundedSaltValue + productNutInfo.getSaltUnit();
                    mSaltValueTextView.setText("Salt: " + Salt);
                    JsonUtilities.setColour(mSaltColorView,productNutInfo.getSaltLevel());

                    // set Sugar value, unit and colour
                    double sugarValue = productNutInfo.getSugar();
                    String roundedSugarValue = new DecimalFormat("#.##").format(sugarValue);
                    String Sugar = roundedSugarValue + productNutInfo.getSugarUnit();
                    mSugarValueTextView.setText("Sugar: " + Sugar);
                    JsonUtilities.setColour(mSugarColorView,productNutInfo.getSugarLevel());

                    // set protein value and unit
                    double proteinValue = productNutInfo.getProtein();
                    String roundedProteinValue = new DecimalFormat("#.##").format(proteinValue);
                    String Protein = roundedProteinValue + productNutInfo.getProteinUnit();
                    mProteinValueTextView.setText("Protein: " + Protein);

                    // set Carbohydrate value and unit
                    double carbValue = productNutInfo.getCarbohydrates();
                    String roundedCarbValue = new DecimalFormat("#.##").format(carbValue);
                    String Carb = roundedCarbValue + productNutInfo.getCarbohydratesUnit();
                    mCarbohydrateValueTextView.setText("Carbohydrate: " + Carb);





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
            mOffLogoImageView.setVisibility(View.VISIBLE);


        }
        else{
            String title = productNutInfo.getProductName() + " Grade: " + productNutInfo.getNutritionGrade().toUpperCase();

            //mProductNameDisplay.setText(title);
            //mDataDisplay.setText("Alternatives: \n" );
            makeAltQuery(productNutInfo);

        }




       // findViewById(R.id.buttonFindAlt).setOnClickListener(this);
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
        if(categories == null){
            return;
        }
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

            /// ALWAYS INCLUDE A - needs check for same product as main
            /*

            do {

                String altGrade = Character.toString((char) i);
                altCat = offNutInfo.getCategoryHierarchy()[j];
                Log.d("tag",altGrade + " - " + altCat);

                altSearchUrl = NetworkUtils.buildAltUrl(altCat,altGrade);
                new CombinedActivity.AltQueryTask().execute(altSearchUrl);
                i++;
            } while (i < altChar);
             */
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

    /*@Override
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

    }*/

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

