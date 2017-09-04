package com.example.android.nfoodapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import android.databinding.DataBindingUtil;
import com.example.android.nfoodapp.utilities.JsonUtilities;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;

public class NutritionActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView mNutritionNutGradeTextView;
    private TextView mNutritionProductNameTextView;
    private TextView mCaloriesTextView;
    private TextView mFatValueTextView;
    private TextView mSaltValueTextView;
    private TextView mSugarValueTextView;
    private ImageView mTestImageView;
    private NutritionInfo productNutInfo;
    URL imageUrl = null;
    Bitmap ImageBitmap = null;
    //ActivityNutritionBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition);


        //mNutritionDataTextView = (TextView) findViewById(R.id.tv_results_display);
        mNutritionProductNameTextView = (TextView) findViewById(R.id.textViewProductName);
        mNutritionNutGradeTextView = (TextView) findViewById(R.id.textViewNutGrade);
        mCaloriesTextView = (TextView) findViewById(R.id.textViewCalories);
        mFatValueTextView = (TextView) findViewById(R.id.textViewFatValue);
        mSaltValueTextView = (TextView) findViewById(R.id.textViewSaltValue);
        mSugarValueTextView = (TextView) findViewById(R.id.textViewSugarValue);
        mTestImageView = (ImageView) findViewById(R.id.imageViewTestImage);


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


        Intent intentThatStartedThisActivity = getIntent();

        if(intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("nutInfo")){

                productNutInfo = intentThatStartedThisActivity.getParcelableExtra("nutInfo");

                // if product not found refer user to OFF contribute page
                if (productNutInfo.getProductName() == null) {

                    /// set text to /uk.openfoodfacts.org/contribute
                }
                else{



                    try {
                         imageUrl = new URL(productNutInfo.getImageUrl());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    new ImageQueryTask().execute(imageUrl);

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

    public class ImageQueryTask extends AsyncTask<URL, Integer, Bitmap>{

        @Override
        protected Bitmap doInBackground(URL... urls) {
            Bitmap bitmap = null;
            try {
                 bitmap = BitmapFactory.decodeStream((InputStream)new URL(productNutInfo.getImageUrl()).getContent());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            ImageBitmap = bitmap;
        }
    }
}
