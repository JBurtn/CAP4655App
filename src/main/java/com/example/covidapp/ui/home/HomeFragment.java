package com.example.covidapp.ui.home;

import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.example.covidapp.Firebase.FirebaseBE;
import com.example.covidapp.R;
import com.example.covidapp.VolleyBE;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private VolleyBE Vbe;
    private static final String URL = "https://maps.googleapis.com/maps/api/geocode/json?";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        LinearLayout Location = root.findViewById(R.id.LocationStats);
        LinearLayout Area = root.findViewById(R.id.AreaStats);

        Vbe = VolleyBE.getInstance(getContext());
        homeViewModel.getLocation(getContext());
        homeViewModel.getCount().observe(getViewLifecycleOwner(), s -> {
            TextView cnt = new TextView(getContext());//Count
            TextView timeStamp = new TextView(getContext());

            //edit View
            cnt.setTypeface(Typeface.DEFAULT_BOLD);// default Text too light
            cnt.setText(s);

            timeStamp.setTypeface(Typeface.DEFAULT_BOLD);
            timeStamp.setText(Calendar.getInstance().getTime().toString());
            //add View
            Area.addView(cnt);
            Area.addView(timeStamp);
        });
        homeViewModel.hasLocation().observe(getViewLifecycleOwner(), new Observer<android.location.Location>() {
                    @Override
                    public void onChanged(Location aBoolean) {
                        if(aBoolean != null){
                            String addititve = "latlng=" + aBoolean.getLatitude() + "," + aBoolean.getLongitude();
                            String key = "&key=AIzaSyCdO439bqpmSU0MnI1gVAtkR-JxUOV0c4g";
                            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL + addititve + key, null, new Response.Listener<JSONObject>(){

                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        //iterate for formatted Address over compontents
                                        JSONArray arr = response.getJSONArray("results");
                                        for(int i = 0; i < arr.length(); i++){
                                            //Declarations
                                            JSONObject obj = (JSONObject) arr.get(i);//Reassignment needed

                                            TextView addr = new TextView(getContext());
                                            TextView TypeSet = new TextView(getContext());

                                            //Use
                                            addr.setText(obj.getString("formatted_address"));
                                            //removing natural braces
                                            String types = obj.getJSONArray("Types").toString(1).replace("}", "\n");
                                            TypeSet.setText(types.replace("{", ""));

                                            addr.setTypeface(Typeface.DEFAULT_BOLD);
                                            TypeSet.setTypeface(Typeface.DEFAULT_BOLD);

                                            //Add
                                            Location.addView(addr);
                                            Location.addView(TypeSet);
                                        }
                                        TextView Loc = new TextView(getContext());
                                        Loc.setText("Location: " + Location.toString());
                                        Location.addView(Loc);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener(){

                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });
                            Vbe.addToRequestQueue(request);
                        }
                    }
                });

        return root;
    }
}
