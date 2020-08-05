package com.example.inclass14;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterOfTrips.InteractWithMainActivity,AdapterOfSubPlaces.InteractClass{

    private RecyclerView rv_MainTrips;
    private ImageView iv_addTrip;
    private FirebaseFirestore db;
    private TextView tv_notrips;
    private RecyclerView.Adapter rv_adapter1;
    private RecyclerView.LayoutManager rv_layoutManager1;
    ArrayList<Trip> trips = new ArrayList<>();
    private String selectedtrip="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Trips");
        db = FirebaseFirestore.getInstance();

        rv_MainTrips=findViewById(R.id.rv_MainTrips);
        iv_addTrip=findViewById(R.id.iv_addTrip);
        tv_notrips =findViewById(R.id.tv_notrips);


        rv_MainTrips.setVisibility(RecyclerView.INVISIBLE);
        tv_notrips.setVisibility(TextView.INVISIBLE);

        getAllTrips();

        iv_addTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,Add_trip.class);
                startActivity(i);
            }
        });
    }

    public void getAllTrips(){
        db.collection("Trips")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        trips.clear();
                        for (DocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                            ArrayList<Place> placesUnderTrip = new ArrayList<>();
                            Trip trip = new Trip();
                            trip.setDescription(documentSnapshot.getString("description"));
                            trip.setLongitude(documentSnapshot.getString("longitude"));
                            trip.setPlace_id(documentSnapshot.getString("place_id"));
                            trip.setTripName(documentSnapshot.getString("tripName"));
                            trip.setLatitide(documentSnapshot.getString("latitide"));
                            ArrayList<Map<String,String>> placesForthisTrip = null;
                            placesForthisTrip = (ArrayList<Map<String,String>>) documentSnapshot.get("places");
                            for(Map<String,String> m:placesForthisTrip){
                                Place p = new Place();
                                p.setPlace_id(m.get("place_id"));
                                p.setDescription(m.get("description"));
                                p.setLatitide(m.get("latitide"));
                                p.setLongitude(m.get("longitude"));
                                p.setIcon_id(m.get("icon_id"));
                                placesUnderTrip.add(p);
                            }
                            trip.setPlaces(placesUnderTrip);
                            trips.add(trip);
                        }
                        if(trips.size()>0){
                            tv_notrips.setVisibility(TextView.INVISIBLE);
                            rv_MainTrips.setVisibility(RecyclerView.VISIBLE);

                            rv_MainTrips.setHasFixedSize(true);
                            rv_layoutManager1 = new LinearLayoutManager(MainActivity.this);
                            rv_MainTrips.setLayoutManager(rv_layoutManager1);
                            rv_adapter1 = new AdapterOfTrips(trips, MainActivity.this);
                            rv_MainTrips.setAdapter(rv_adapter1);
                        }else{
                            tv_notrips.setVisibility(TextView.VISIBLE);
                            rv_MainTrips.setVisibility(RecyclerView.INVISIBLE);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("DEMO", "Failed to the trips: "+e.getMessage() );
                    }
                });
    }

    @Override
    public void selecteditem(int position) {
        selectedtrip=trips.get(position).getTripName();
        new getLatLong().execute(trips.get(position).getPlace_id());
    }

    @Override
    public void showOnMap(int position) {
        Intent i = new Intent(MainActivity.this,TripMapScreen.class);
        i.putExtra("TRIP",trips.get(position));
        startActivity(i);
    }


    public void deletePlaceFromTrip(Place place, String tripName){
        db.collection("Trips")
                .document(tripName)
                .update("places", FieldValue.arrayRemove(place))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        getAllTrips();
                    }
                });
    }

    @Override
    public void delete(int position, int tripPosition) {
        deletePlaceFromTrip(trips.get(tripPosition).getPlaces().get(position),trips.get(tripPosition).getTripName());
    }


    public class getLatLong extends AsyncTask<String,Void,String[]> {

        @Override
        protected String[] doInBackground(String... strings) {
            HttpURLConnection connection = null;
            String searchCityUrl = "";
            String Latitude="",Longitude="";
            try {
                searchCityUrl = "https://maps.googleapis.com/maps/api/place/details/json" + "?key=" + URLEncoder.encode(getResources().getString(R.string.api_key), "UTF-8") + "&placeid=" + URLEncoder.encode(strings[0], "UTF-8");

                URL urlcon = new URL(searchCityUrl);
                connection = (HttpURLConnection) urlcon.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                    JSONObject root = new JSONObject(json);
                    Latitude = root.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").getString("lat");
                    Longitude = root.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").getString("lng");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }  finally {
                //Close the connections
            }   return new String[]{Latitude,Longitude};
        }

        @Override
        protected void onPostExecute(String[] aVoid) {
            super.onPostExecute(aVoid);
            String url="";
            try {
                url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" + "?key=" + URLEncoder.encode(getResources().getString(R.string.api_key), "UTF-8") + "&location=" + aVoid[0]+"," +aVoid[1] +"&radius=1000" ;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Intent gotoAddPlaces = new Intent(MainActivity.this,Add_Places.class);
            gotoAddPlaces.putExtra("URL",url);
            gotoAddPlaces.putExtra("SELECTEDTRIP",selectedtrip);
            startActivity(gotoAddPlaces);
        }
    }
}
