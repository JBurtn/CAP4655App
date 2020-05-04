package com.example.covidapp.ui.profile;

import androidx.annotation.NonNull;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.covidapp.Firebase.Data.UserModel.UserModel;
import com.example.covidapp.Firebase.FirebaseBE;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProfileViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private FirebaseBE BE = new FirebaseBE();
    private MutableLiveData<UserModel> userData;
    private MediatorLiveData<FirebaseBE.LoginState> stateMediator = new MediatorLiveData<>();
    private static FirebaseDatabase baseDB = FirebaseDatabase.getInstance();
    private static DatabaseReference UserDB;
    private static volatile FirebaseUser currentUser;

    public ProfileViewModel(){
          stateMediator.addSource(BE.getState(), new Observer<FirebaseBE.LoginState>() {
              @Override
              public void onChanged(FirebaseBE.LoginState loginState) {
                      currentUser = FirebaseBE.getCurrentUser();
                  if(currentUser != null) {
                      UserDB = baseDB.getReference().child("User");
                      Query user = UserDB.child(currentUser.getUid());

                      user.addValueEventListener(new ValueEventListener() {
                          @Override
                          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                              userData.setValue(dataSnapshot.getValue(UserModel.class));
                          }

                          @Override
                          public void onCancelled(@NonNull DatabaseError databaseError) {

                          }
                      });
                  }
              }
          });
    }

    public MediatorLiveData<FirebaseBE.LoginState> getStateMediator() {
        return stateMediator;
    }

    void setUserDatabase(UserModel model){
        UserDB.child(currentUser.getUid()).setValue(model);
    }
    void logout(){
        if(BE.getState().getValue() == FirebaseBE.LoginState.LOGIN_SUCCESS)
            BE.logout();
    }
    MutableLiveData<UserModel> getUserData() {
        if(userData == null){
            userData = new MutableLiveData<>();
        }
        return userData;
    }
}
