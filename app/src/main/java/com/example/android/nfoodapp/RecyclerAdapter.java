package com.example.android.nfoodapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.nfoodapp.utilities.JsonUtilities;
import com.example.android.nfoodapp.utilities.NetworkUtils;
import com.example.android.nfoodapp.DetailActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Objects;

import static android.provider.Settings.Global.getString;

/**
 * Created by johnkay on 01/08/2017.
 */
// Adapter for the Alternate food list RecyclerView
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.AltViewHolder> {

   // private String[] mAltData;
    private ArrayList<NutritionInfo> mAltData = new ArrayList<NutritionInfo>();
    private final AltAdapterOnClickHandler mClickHandler;



    // Creates a Forecast adapter
    public RecyclerAdapter(AltAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    // create viewholders to fill the screen
    @Override
    public RecyclerAdapter.AltViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.recyclerview_item_row;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return  new AltViewHolder(view);
    }
    // interface to receive onClick Messages
    public interface AltAdapterOnClickHandler {
        void onClick(NutritionInfo altJson);
    }


    // retrieve product name from JSON string and bind to view holder
    @Override
    public void onBindViewHolder(RecyclerAdapter.AltViewHolder holder, int position) {
        String productName = mAltData.get(position).getProductName();
        long productSugar = mAltData.get(position).getSugar();
        String productGrade = mAltData.get(position).getNutritionGrade();


        String description = "Nutrition Grade: " + productGrade.toUpperCase();
        holder.mItemTitle.setText(productName);
        holder.mItemDescription.setText(description);

    }



    @Override
    public int getItemCount() {
        if(mAltData == null) return 0;
        return mAltData.size();
    }

    public class AltViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mItemTitle;
        public TextView mItemDescription;

        public AltViewHolder(View itemView) {
            super(itemView);
            mItemTitle = (TextView) itemView.findViewById(R.id.list_item_title);
            mItemDescription = (TextView) itemView.findViewById(R.id.list_item_description);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            NutritionInfo altData = mAltData.get(adapterPosition);
            mClickHandler.onClick(altData);
        }

    }
    // append new data onto the end of the data ArrayList
    public void setmAltData(NutritionInfo[] data){
        boolean foundDuplicate;
        for (NutritionInfo aData : data) {
            foundDuplicate = false;
            // check for duplicates between the string and all other strings in the Array list
            for (NutritionInfo product: mAltData) {
                if (Objects.equals(aData.getProductName(), product.getProductName())) {
                    foundDuplicate = true;
                    break;
                }
            }
            if(!foundDuplicate){
                mAltData.add(aData);
            }
        }
        notifyDataSetChanged();
    }

}
