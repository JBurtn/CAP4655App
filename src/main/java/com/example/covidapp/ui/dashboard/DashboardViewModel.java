package com.example.covidapp.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.covidapp.StatModel.Region;
import com.example.covidapp.VolleyBE;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class DashboardViewModel extends ViewModel {

    private MutableLiveData<String> status;
    private SortedMap<String, Region> countries = new TreeMap<>();
    private ArrayList<String> selector = new ArrayList<String>();
    private static final String API_URL = "https://api.smartable.ai/coronavirus/stats/";
    private Boolean flag = false;


    public DashboardViewModel() {
        status = new MutableLiveData<>();
        status.setValue(" No Last Tag");
    }
    public void QueryStats(VolleyBE BE, String RegionCode, Boolean breakdown){

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, API_URL + RegionCode, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String totalConfirmedCases = response.getJSONObject("stats").getString("totalConfirmedCases");
                    String newlyConfirmedCases = response.getJSONObject("stats").getString("newlyConfirmedCases");
                    String totalDeaths = response.getJSONObject("stats").getString("totalDeaths");
                    String newDeaths = response.getJSONObject("stats").getString("newDeaths");
                    String totalRecoveredCases = response.getJSONObject("stats").getString("totalRecoveredCases");
                    String newlyRecoveredCases = response.getJSONObject("stats").getString("newlyRecoveredCases");
                    String name = response.getJSONObject("location").getString("isoCode");

                    if(name.equals("null"))
                        name = "global";

                    Region region = new Region(totalConfirmedCases, newlyConfirmedCases, totalDeaths, newDeaths, totalRecoveredCases, newlyRecoveredCases);

                    JSONArray history = response.getJSONObject("stats").getJSONArray("history");
                    for(int i = 0; i < history.length(); i++){
                        JSONObject o = (JSONObject) history.get(i);
                        region.addHistory(o.getString("date"), o.getString("confirmed"), o.getString("deaths"), o.getString("recovered"));
                    }
                    getBreakdown(response, breakdown);
                    countries.put(name, region);
                    status.setValue(name);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Cache-Control", "no-cache");
                params.put("Subscription-Key", "8f46ff8c6bce435faa40a689ae33a4c6");

                return params;
            }
        };
        BE.addToRequestQueue(request);
    }
    private void getBreakdown(JSONObject response, boolean breakdown) throws JSONException {
        if(response.getJSONObject("stats").has("breakdowns") && breakdown){
            JSONArray breakdowns = response.getJSONObject("stats").getJSONArray("breakdowns");
            for(int i = 0; i < breakdowns.length(); i++){
                JSONObject b = (JSONObject) breakdowns.get(i);
                String totalConfirmedCases = b.getString("totalConfirmedCases");
                String newlyConfirmedCases = b.getString("newlyConfirmedCases");
                String totalDeaths = b.getString("totalDeaths");
                String newDeaths = b.getString("newDeaths");
                String totalRecoveredCases = b.getString("totalRecoveredCases");
                String newlyRecoveredCases = b.getString("newlyRecoveredCases");
                String name = b.getJSONObject("location").getString("isoCode");

                Region region = new Region(totalConfirmedCases, newlyConfirmedCases, totalDeaths, newDeaths, totalRecoveredCases, newlyRecoveredCases);
                countries.putIfAbsent(name, region);
            }
        }
    }
    void addCountries(String tag){
        selector.add(tag);
    }
    void removeCountries(String tag){
        selector.remove(tag);
    }
    SortedMap<String, Region> getCountries() {
        return countries;
    }
    ArrayList<String> getSelector(){
        return selector;
    }
    MutableLiveData<String> getStatus(){
        return status;
    }
    void setFlag(boolean f){
        flag = f;
    }
    boolean getFlag(){
        return flag;
    }
}