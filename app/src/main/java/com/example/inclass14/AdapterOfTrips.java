package com.example.inclass14;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AdapterOfTrips extends RecyclerView.Adapter<AdapterOfTrips.ViewHolder>{

    public static InteractWithMainActivity interact;
    Context ctx;
    ArrayList<Trip> tripsList = new ArrayList<Trip>();
    RecyclerView.Adapter rv_adapter2;
    RecyclerView.LayoutManager rv_layoutManager2;

    public AdapterOfTrips(ArrayList<Trip> triplist, Context ctx) {
        this.tripsList = triplist;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout rv_layout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.tripitem, parent, false);
        ViewHolder viewHolder = new ViewHolder(rv_layout);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.selectedposition = position;
        holder.tv_itemTripName.setText(tripsList.get(position).getTripName());
        holder.tv_itemCityName.setText(tripsList.get(position).getDescription());

        holder.rv_subPlacesUnderCities.setHasFixedSize(true);
        rv_layoutManager2 = new LinearLayoutManager(ctx);
        holder.rv_subPlacesUnderCities.setLayoutManager(rv_layoutManager2);
        rv_adapter2 = new AdapterOfSubPlaces(tripsList.get(position).getPlaces(), ctx, position);
        holder.rv_subPlacesUnderCities.setAdapter(rv_adapter2);
    }

    @Override
    public int getItemCount() {
        return tripsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        int selectedposition;
        TextView tv_itemTripName,tv_itemCityName;
        ImageView iv_itemShowOnMap,iv_ItemaddPlaces;
        RecyclerView rv_subPlacesUnderCities;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_itemTripName = itemView.findViewById(R.id.tv_itemTripName);
            tv_itemCityName = itemView.findViewById(R.id.tv_itemCityName);
            iv_itemShowOnMap = itemView.findViewById(R.id.iv_itemShowOnMap);
            iv_ItemaddPlaces = itemView.findViewById(R.id.iv_ItemaddPlaces);
            rv_subPlacesUnderCities = itemView.findViewById(R.id.rv_subPlacesUnderCities);

            iv_ItemaddPlaces.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    interact = (InteractWithMainActivity) ctx;
                    interact.selecteditem(selectedposition);
                }
            });

            iv_itemShowOnMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    interact = (InteractWithMainActivity) ctx;
                    interact.showOnMap(selectedposition);
                }
            });
        }
    }

    public interface InteractWithMainActivity{
        void selecteditem(int position);
        void showOnMap(int position);
    }


}
