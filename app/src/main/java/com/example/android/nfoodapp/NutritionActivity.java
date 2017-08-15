package com.example.android.nfoodapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import android.databinding.DataBindingUtil;
import com.example.android.nfoodapp.utilities.JsonUtilities;

import org.json.JSONException;
import org.w3c.dom.Text;


import java.util.HashMap;

public class NutritionActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView mNutritionNutGradeTextView;
    private TextView mNutritionProductNameTextView;
    private TextView mCaloriesTextView;
    private TextView mFatValueTextView;
    private TextView mSaltValueTextView;
    private TextView mSugarValueTextView;
    private NutritionInfo productNutInfo;
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


        findViewById(R.id.buttonFindAlt).setOnClickListener(this);



        Intent intentThatStartedThisActivity = getIntent();

        if(intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("nutInfo")){

                productNutInfo = intentThatStartedThisActivity.getParcelableExtra("nutInfo");

                // if product not found refer user to OFF contribute page
                if (productNutInfo.getProductName() == null) {


                }
                else{
                    String title = productNutInfo.getProductName();
                    mNutritionProductNameTextView.setText(title);
                    mNutritionNutGradeTextView.setText(productNutInfo.getNutritionGrade());
                    String Calories = Long.toString(productNutInfo.getEnergy()) + productNutInfo.getEnergyUnit();
                    mCaloriesTextView.setText(Calories);
                    String Fat = Long.toString(productNutInfo.getFat()) + productNutInfo.getFatUnit();
                    mFatValueTextView.setText(Fat);
                    String Salt = Long.toString(productNutInfo.getSalt()) + productNutInfo.getSaltUnit();
                    mSaltValueTextView.setText(Salt);
                    String Sugar = Long.toString(productNutInfo.getSugar()) + productNutInfo.getSugarUnit();
                    mSugarValueTextView.setText(Sugar);
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
}
