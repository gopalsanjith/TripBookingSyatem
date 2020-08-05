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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Add_trip extends AppCompatActivity implements AdapterOfCities.InteractWithAddTripActivity {

    private EditText et_tripName,et_city;
    private FirebaseFirestore db;
    private Button btn_searchCity,btn_addTrip;
    private RecyclerView rv_listOfCities;
    private RecyclerView.Adapter rv_adapter;
    private RecyclerView.LayoutManager rv_layoutManager;
    String searchCityUrl;
    Trip selectedCity = null;

    ArrayList<Trip> searchCityResult = new ArrayList<Trip>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        et_tripName=findViewById(R.id.et_tripName);
        et_city=findViewById(R.id.et_city);
        btn_searchCity=findViewById(R.id.btn_searchCity);
        btn_addTrip=findViewById(R.id.btn_addTrip);
        rv_listOfCities=findViewById(R.id.rv_listOfCities);
        db = FirebaseFirestore.getInstance();

        btn_addTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!et_tripName.getText().toString().equals("") && !et_city.getText().toString().equals("") && selectedCity != null){
                    selectedCity.setTripName(et_tripName.getText().toString());
                    db.collection("Trips")
                            .document(selectedCity.getTripName())
                            .set(selectedCity).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Add_trip.this, "Trip added Successfully", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Add_trip.this,MainActivity.class);
                            startActivity(i);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Add_trip.this, "Trip Adding failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(Add_trip.this, "Please enter both tripname and CIty to search", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btn_searchCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!et_city.getText().toString().equals("")){
                    searchCityResult.clear();
                    try {
                        searchCityUrl= "https://maps.googleapis.com/maps/api/place/autocomplete/json"+"?key="+URLEncoder.encode( getResources().getString(R.string.api_key), "UTF-8")+"&types=(cities)&input="+URLEncoder.encode(et_city.getText().toString(),"UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    new SearchCity().execute(searchCityUrl);
                }else{
                    Toast.makeText(Add_trip.this, "Please enter City to search", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void selecteditem(int position) {
        selectedCity = new Trip();
        selectedCity.setDescription(searchCityResult.get(position).getDescription());
        selectedCity.setPlace_id(searchCityResult.get(position).getPlace_id());
        new getLatLong().execute(searchCityResult.get(position).getPlace_id());
    }

    public class SearchCity extends AsyncTask<String,Void,Void> {

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
                    JSONArray predictions = root.getJSONArray("predictions");
                    for (int i=0;i<predictions.length();i++) {
                        Trip city = new Trip();
                        JSONObject prediction = predictions.getJSONObject(i);
                        city.setDescription(prediction.getString("description"));
                        city.setPlace_id(prediction.getString("place_id"));
                        searchCityResult.add(city);
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
            rv_listOfCities.setHasFixedSize(true);
            rv_layoutManager = new LinearLayoutManager(Add_trip.this);
            rv_listOfCities.setLayoutManager(rv_layoutManager);
            rv_adapter = new AdapterOfCities(searchCityResult, Add_trip.this);
            rv_listOfCities.setAdapter(rv_adapter);
        }
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
            }return new String[]{Latitude,Longitude};
        }

        @Override
        protected void onPostExecute(String[] aVoid) {
            super.onPostExecute(aVoid);
            selectedCity.setLatitide(aVoid[0]);
            selectedCity.setLongitude(aVoid[1]);
            et_city.setText(selectedCity.getDescription());
        }
    }
}
