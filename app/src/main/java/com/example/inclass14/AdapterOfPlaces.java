package com.example.inclass14;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterOfPlaces extends RecyclerView.Adapter<AdapterOfPlaces.ViewHolder>{

    public static InteractWithAddPlaces interact;
    Context ctx;
    ArrayList<Place> placesList = new ArrayList<Place>();

    public AdapterOfPlaces(ArrayList<Place> citiesList, Context ctx) {
        this.placesList = citiesList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout rv_layout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlistofplaces, parent, false);
        ViewHolder viewHolder = new ViewHolder(rv_layout);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.selectedposition = position;
        holder.tv_itemPlaceName.setText(placesList.get(position).getDescription());
        Picasso.get().load(placesList.get(position).getIcon_id()).into(holder.iv_itemplaceImageID);
    }

    @Override
    public int getItemCount() {
        return placesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        int selectedposition;
        TextView tv_itemPlaceName;
        ImageView iv_itemplaceImageID,iv_addPlaceToCity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_itemPlaceName = itemView.findViewById(R.id.tv_itemPlaceName);
            iv_itemplaceImageID = itemView.findViewById(R.id.iv_itemplaceImageID);
            iv_addPlaceToCity = itemView.findViewById(R.id.iv_addPlaceToCity);

            iv_addPlaceToCity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    interact = (InteractWithAddPlaces) ctx;
                    interact.selecteditem(selectedposition);
                }
            });
        }
    }

    public interface InteractWithAddPlaces{
        void selecteditem(int position);
    }
}
