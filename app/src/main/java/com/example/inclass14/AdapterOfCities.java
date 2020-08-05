package com.example.inclass14;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterOfCities extends RecyclerView.Adapter<AdapterOfCities.ViewHolder>{

    public static InteractWithAddTripActivity interact;
    Context ctx;
    ArrayList<Trip> citiesList = new ArrayList<Trip>();

    public AdapterOfCities(ArrayList<Trip> citiesList, Context ctx) {
        this.citiesList = citiesList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout rv_layout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.citiesresultitem, parent, false);
        ViewHolder viewHolder = new ViewHolder(rv_layout);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.selectedposition = position;
        holder.tv_cityresultItem.setText(citiesList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return citiesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        int selectedposition;
        TextView tv_cityresultItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_cityresultItem = itemView.findViewById(R.id.tv_cityresultItem);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    interact = (InteractWithAddTripActivity) ctx;
                    interact.selecteditem(selectedposition);
                }
            });
        }
    }

    public interface InteractWithAddTripActivity{
        void selecteditem(int position);
    }
}
