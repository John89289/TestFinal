package com.example.android.nfoodapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.nfoodapp.utilities.JsonUtilities;

import org.json.JSONException;

import java.util.HashMap;

public class NutritionActivity extends AppCompatActivity {


    private TextView mNutritionDataTextView;
    private TextView mNutritionTitleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition);


        HashMap<String,Double> nutData = new HashMap();
        mNutritionDataTextView = (TextView) findViewById(R.id.tv_results_display);
        //mNutritionTitleTextView = (TextView) findViewById(R.id.tv_results_display);



        //retrieve intent + data from intent
        Intent intentThatStartedThisActivity = getIntent();

        if(intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("hashmap")){

                nutData = (HashMap<String, Double>) intentThatStartedThisActivity.getSerializableExtra("hashmap");
                mNutritionDataTextView.setText(nutData.toString());


            }
        }
    }
}
