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
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterOfSubPlaces extends RecyclerView.Adapter<AdapterOfSubPlaces.ViewHolder>{

    public static InteractClass interact;
    Context ctx;
    int tripPosition;
    ArrayList<Place> placesList = new ArrayList<Place>();

    public AdapterOfSubPlaces(ArrayList<Place> placesList, Context ctx, int  tripPosition) {
        this.placesList = placesList;
        this.ctx = ctx;
        this.tripPosition = tripPosition;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout rv_layout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.placeundertripitem, parent, false);
        ViewHolder viewHolder = new ViewHolder(rv_layout);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.selectedposition = position;
        holder.tv_subitemPlaceName.setText(placesList.get(position).getDescription());
        Log.d("Demo", "onBindViewHolder: "+placesList.get(position).getDescription());
        Picasso.get().load(placesList.get(position).getIcon_id()).into(holder.iv_subitemPlaceIcon);
    }

    @Override
    public int getItemCount() {
        return placesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        int selectedposition;
        TextView tv_subitemPlaceName;
        ImageView iv_subitemPlaceIcon,iv_delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_subitemPlaceName = itemView.findViewById(R.id.tv_subitemPlaceName);
            iv_subitemPlaceIcon = itemView.findViewById(R.id.iv_subitemPlaceIcon);
            iv_delete = itemView.findViewById(R.id.iv_delete);

            iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    interact = (InteractClass) ctx;
                    interact.delete(selectedposition, tripPosition);
                }
            });
        }
    }

    public interface InteractClass{
        void delete(int position, int tripPosition);
    }
}
