package com.example.covidapp.ui.login;

import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.covidapp.Firebase.FirebaseBE;

public class LoginViewModel extends ViewModel {
    final FirebaseBE be =  new FirebaseBE();
    private MutableLiveData<FirebaseBE.LoginState> stateMutableLiveData = new MutableLiveData<>();
    public void login(String user, String pass){
        be.login(user, pass);
    }
    public MutableLiveData<FirebaseBE.LoginState> getStateMutableLiveData() {
        return be.getState();
    }
}

