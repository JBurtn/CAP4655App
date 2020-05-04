package com.example.covidapp.Firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseBE {
    private static MutableLiveData<LoginState> state = new MutableLiveData<>();
    private static FirebaseUser currentUser;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public FirebaseBE(){
        if(mAuth.getCurrentUser() != null)
            currentUser = mAuth.getCurrentUser();
    }

    public static FirebaseUser getCurrentUser() {
        return currentUser;
    }

    public void login(String username, String password){
        mAuth.signInWithEmailAndPassword(username, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
               FirebaseUser currentUser = authResult.getUser();
               state.setValue(LoginState.LOGIN_SUCCESS);
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(e instanceof FirebaseAuthInvalidCredentialsException){
                    state.setValue( LoginState.LOGIN_FAILURE);
                }
                else
                    state.setValue(LoginState.LOGIN_ERROR);
                Log.d("Failure", e.getMessage());
            }
        });
    }
    public void logout(){
        state.setValue(LoginState.LOGIN_PENDING);
        mAuth.signOut();
    }
    public MutableLiveData<LoginState> getState(){
        if(state.getValue() == null)
            state.setValue(LoginState.LOGIN_PENDING);
        return state;
    }
    public enum LoginState{
        LOGIN_ERROR (-1),
        LOGIN_FAILURE (0),
        LOGIN_SUCCESS (1),
        LOGIN_PENDING (2);

        private final int LevelCode;
        LoginState(int LevelCode){
            this.LevelCode = LevelCode;
        }
    }
}
