package com.example.covidapp.ui.home;

import android.content.Context;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.covidapp.Firebase.FirebaseBE;
import com.example.covidapp.GPStracking;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class HomeViewModel extends ViewModel {

    private GPStracking gpStracking;
    private FirebaseBE BE = new FirebaseBE();
    private MutableLiveData<String> count = new MutableLiveData<>();

    private static FirebaseDatabase baseDB = FirebaseDatabase.getInstance();
    private static DatabaseReference UserDB;
    public HomeViewModel() {
        UserDB = baseDB.getReference();

        Query totalUsers = UserDB.child("User").child("__count");

        totalUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count.setValue((String) dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public MutableLiveData<String> getCount() {
        return count;
    }
    public void getLocation(Context context){
        if(gpStracking == null)
            gpStracking = new GPStracking(context);
        if(!gpStracking.cangetLocation() && gpStracking.getHasLoc().getValue() != null)
            gpStracking.getLocation();
    }
    public LiveData<Location> hasLocation() {
        return gpStracking.getHasLoc();
    }
}