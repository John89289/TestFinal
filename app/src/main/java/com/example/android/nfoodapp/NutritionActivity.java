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


import java.util.HashMap;

public class NutritionActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView mNutritionDataTextView;
    private TextView mNutritionProductNameTextView;
    private NutritionInfo productNutInfo;
    //ActivityNutritionBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition);


        //mNutritionDataTextView = (TextView) findViewById(R.id.tv_results_display);
        mNutritionProductNameTextView = (TextView) findViewById(R.id.textViewProductName);
        findViewById(R.id.buttonFindAlt).setOnClickListener(this);



        Intent intentThatStartedThisActivity = getIntent();

        if(intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)){

                productNutInfo = intentThatStartedThisActivity.getParcelableExtra(Intent.EXTRA_TEXT);

                // if product not found refer user to OFF contribute page
                if (productNutInfo.getProductName() == null) {


                }
                else{
                    String title = productNutInfo.getProductName() + " Grade: " + productNutInfo.getNutritionGrade().toUpperCase();
                    mNutritionProductNameTextView.setText(title);
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
