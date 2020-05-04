package com.example.covidapp.ui.notifications;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.covidapp.VolleyBE;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationsViewModel extends ViewModel {

    private MutableLiveData<ArrayList<news>> mText;
    private String gdata;
    public NotificationsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue(new ArrayList<news>());
    }

    void getNews(VolleyBE queue){
        String url = "https://api.smartable.ai/coronavirus/news/US-FL";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    gdata = response.getString("updatedDateTime");
                    ArrayList<news> piece = new ArrayList<news>();
                    for (int i = 0; i < response.getJSONArray("news").length(); i++ ) {
                            JSONObject obj = (JSONObject) response.getJSONArray("news").get(i);
                            piece.add(new news(obj.getString("title"), obj.getString("webUrl"), obj.getString("excerpt")));
                    }

                    ArrayList<news> comp = mText.getValue();
                    if(!piece.equals(comp))
                        mText.postValue(piece);
                } catch (JSONException e) {
                    Log.d("News Request", e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Notification Fragment", error.getMessage());
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
        queue.addToRequestQueue(request);
    }
    LiveData<ArrayList<news>> getText() {
        return mText;
    }
    public String getGdata(){
        return gdata;
    }

    public static class news{
        private String title;
        private String URL;
        private String excerpt;
        public news(){}
        news(String title, String URL, String excerpt){
            this.title = title;
            this.URL = URL;
            this.excerpt = excerpt;
        }
        @Override
        public boolean equals(Object o){
            if(o instanceof news){
                news O = (news) o;
                return O.getTitle().equals(this.getTitle()) &&
                        O.getURL().equals(this.getURL()) &&
                        O.getExcerpt().equals(this.getExcerpt());
            }
            return false;
        }
        @Override
        public int hashCode(){
            int result = 17;
            result = 31 * result + title.hashCode();
            result = 31 * result + URL.hashCode();
            result = 31 * result + excerpt.hashCode();

            return result;

        }
        String getTitle() {
            return title;
        }

        String getURL() {
            return URL;
        }

        String getExcerpt() {
            return excerpt;
        }
    }
}