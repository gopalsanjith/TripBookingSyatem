package com.example.inclass14;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Add_Places extends AppCompatActivity implements AdapterOfPlaces.InteractWithAddPlaces{

    private RecyclerView rv_listOfPlaces;
    private RecyclerView.Adapter rv_adapter;
    private RecyclerView.LayoutManager rv_layoutManager;
    private FirebaseFirestore db;
    private String tripName;
    ArrayList<Place> places = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__places);
        db = FirebaseFirestore.getInstance();
        rv_listOfPlaces= findViewById(R.id.rv_listOfPlaces);

        tripName = (String) getIntent().getExtras().get("SELECTEDTRIP");
        String url = (String) getIntent().getExtras().get("URL");
        new loadPlaces().execute(url);
    }

    @Override
    public void selecteditem(int position) {
        addPlaceToDB(places.get(position));
    }

    public void addPlaceToDB(Place place){
        db.collection("Trips")
                .document(tripName)
                .update("places", FieldValue.arrayUnion(place))
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent i = new Intent(Add_Places.this,MainActivity.class);
                startActivity(i);
            }
        });
    }


    public class loadPlaces extends AsyncTask<String,Void,Void> {

        @Override
        protected Void doInBackground(String... strings) {
            HttpURLConnection connection = null;
            try {
                URL urlcon = new URL(strings[0]);
                connection = (HttpURLConnection) urlcon.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");

                    JSONObject root = new JSONObject(json);
                    JSONArray results = root.getJSONArray("results");
                    for (int i=0;i<results.length();i++) {
                        Place loc = new Place();
                        JSONObject place = results.getJSONObject(i);
                        loc.setLatitide(place.getJSONObject("geometry").getJSONObject("location").getString("lat"));
                        loc.setLongitude(place.getJSONObject("geometry").getJSONObject("location").getString("lng"));
                        loc.setIcon_id(place.getString("icon"));
                        loc.setPlace_id(place.getString("place_id"));
                        loc.setDescription(place.getString("name"));
                        places.add(loc);
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            rv_listOfPlaces.setHasFixedSize(true);
            rv_layoutManager = new LinearLayoutManager(Add_Places.this);
            rv_listOfPlaces.setLayoutManager(rv_layoutManager);
            rv_adapter = new AdapterOfPlaces(places, Add_Places.this);
            rv_listOfPlaces.setAdapter(rv_adapter);
        }
    }
}
