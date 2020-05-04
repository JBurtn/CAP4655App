package com.example.covidapp.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.covidapp.Firebase.FirebaseBE;
import com.example.covidapp.R;
import com.example.covidapp.RegistrationActivity;

public class LoginActivity extends AppCompatActivity {
    private LoginViewModel mViewModel;
    private EditText Username;
    private EditText password;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        mViewModel.getStateMutableLiveData().observe(this, loginState -> {
            switch (loginState) {
                case LOGIN_ERROR:
                    Toast.makeText(getBaseContext(), "Login Error", Toast.LENGTH_SHORT).show();
                    break;
                case LOGIN_FAILURE:
                    Toast.makeText(getBaseContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                    break;
                case LOGIN_SUCCESS:
                    Toast.makeText(getBaseContext(), "Login Success", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                default:
                    break;

            }
        });
    }
    public void onLogin(View view){
        if(checkUsername(Username.getText().toString()) && checkPassword(password.getText().toString()) )
            mViewModel.login(Username.getText().toString(), password.getText().toString());
        else{
            Toast.makeText(getApplicationContext(), "Invalid Attempt", Toast.LENGTH_SHORT).show();
        }
    }
    public void onRegisterClick(View view){

        Intent register = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivityForResult(register, Activity.RESULT_OK);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == resultCode){
           String username = data.getStringExtra("username");
           String password = data.getStringExtra("password");
           mViewModel.login(username, password);

        }
    }
    private boolean checkUsername(String user){
        //perform Regex
        if (user == null) {
            return false;
        }
        if (user.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(user).matches();
        } else {
            return !user.trim().isEmpty();
        }
    }
    private boolean checkPassword(String pass){
        //perform check
        if(pass == null)
            return false;
        String isUser = pass.trim();
        return isUser.matches("^([0-9A-Za-z@.]{6,255})$");
    }
}