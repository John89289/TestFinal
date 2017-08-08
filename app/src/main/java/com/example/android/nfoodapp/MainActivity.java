package com.example.android.nfoodapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.nfoodapp.utilities.NetworkUtils;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>, View.OnClickListener {

    private static final int SEARCH_LOADER = 12;

    private EditText mSearchBoxEditText;
    private TextView mSearchResultsTextView;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    private static final int RC_BARCODE_CAPTURE = 9001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // store references to views
        mSearchBoxEditText = (EditText) findViewById(R.id.et_search_box);
        mSearchResultsTextView = (TextView) findViewById(R.id.tv_results_display);

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        // initialise loader
        getSupportLoaderManager().initLoader(SEARCH_LOADER, null, this);

        // initialise button
        findViewById(R.id.read_barcode).setOnClickListener(this);
    }

    private void makeSearchQuery(String barcodeQuery){
       // String barcodeQuery = mSearchBoxEditText.getText().toString();
       // String barcodeQuery = barcode;

        if (TextUtils.isEmpty(barcodeQuery)){
            return;
        }

        URL offSearchUrl = NetworkUtils.buildUrl(barcodeQuery);
        // initialise bundle
        Bundle queryBundle = new Bundle();
        queryBundle.putString("query", offSearchUrl.toString());

        //initialise loader or restart loader

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> offSearchLoader = loaderManager.getLoader(SEARCH_LOADER);
        if (offSearchLoader == null) {
            loaderManager.initLoader(SEARCH_LOADER, queryBundle, this);
        } else {
            loaderManager.restartLoader(SEARCH_LOADER, queryBundle, this);
        }
    }


    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {

            //raw JSON results
            String mOffJson;

            @Override
            protected void onStartLoading() {

                if (args == null){
                    return;
                }

               // mLoadingIndicator.setVisibility(View.VISIBLE);

                if (mOffJson != null) {
                    deliverResult(mOffJson);
                } else {
                    forceLoad();
                }
            }

            @Override
            public String loadInBackground() {
                //extract search query
                String searchQueryUrlString = args.getString("query");

                if (searchQueryUrlString == null || TextUtils.isEmpty(searchQueryUrlString)){
                    return null;
                }

                // parse the url from string and perfom api search
                try{
                    URL offUrl = new URL(searchQueryUrlString);
                    return NetworkUtils.getResponseFromHttpUrl(offUrl);
                } catch (IOException e){
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void deliverResult(String offJson) {
                mOffJson = offJson;
                super.deliverResult(offJson);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        // hide loading indicator
        mLoadingIndicator.setVisibility(View.INVISIBLE);

        // if null assume error and show error message
        if (null == data) {
            showErrorMessage();
        } else {
            // go to detailed activity
            Context context = this;
            Class destinationClass = DetailActivity.class;
            Intent intentToStartDetailActivity = new Intent(context,destinationClass);
            intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, data);
            startActivity(intentToStartDetailActivity);

           // mSearchResultsTextView.setText(data);
           // showJsonDataView();
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    // make Result text Visible and hide error message
    private void showJsonDataView(){
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mSearchResultsTextView.setVisibility(View.VISIBLE);
    }
    // make Result text Invisible and Error Message Visible
    private void showErrorMessage(){
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        mSearchResultsTextView.setVisibility(View.INVISIBLE);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_search){
            String barcodeQuery = mSearchBoxEditText.getText().toString();
            makeSearchQuery(barcodeQuery);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.read_barcode) {
            // launch barcode activity
            Intent intent = new Intent(this, BarcodeActivity.class);

            startActivityForResult(intent,RC_BARCODE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE){
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null){
                    String barcode = data.getStringExtra("barcode");
                    Log.d("barcode", barcode);
                    makeSearchQuery(barcode);
                }
            }

        }
        else{
        super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
