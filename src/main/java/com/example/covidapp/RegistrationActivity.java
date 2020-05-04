package com.example.covidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.covidapp.Firebase.Data.UserModel.Survey;
import com.example.covidapp.Firebase.Data.UserModel.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class RegistrationActivity extends AppCompatActivity {

    private static int MESSAGE_CODE = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        //Const
        final Intent intent = getIntent();
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseDatabase DataBase = FirebaseDatabase.getInstance();
        final DatabaseReference fireDB = DataBase.getReference().child("User");

        final Button submit = findViewById(R.id.RegSubmit);
        final EditText username = findViewById(R.id.Username);
        final EditText password = findViewById(R.id.Password);
        final EditText DateofBirth = findViewById(R.id.DateofBirth);
        final EditText FName = findViewById(R.id.FName);
        final EditText LName = findViewById(R.id.LName);
        final EditText Address = findViewById(R.id.Address);
        final RadioGroup q1 = findViewById(R.id.Question1);
        final RadioGroup q2 = findViewById(R.id.Question2);
        final RadioGroup q3 = findViewById(R.id.Question3);
        final Spinner sp1 = findViewById(R.id.ReoccurRating);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Const
                final String username = username.getText().toString();
                final String password = password.getText().toString();
                final RadioButton q1a = findViewById(q1.getCheckedRadioButtonId());
                final RadioButton q2a = findViewById(q2.getCheckedRadioButtonId());
                final RadioButton q3a = findViewById(q3.getCheckedRadioButtonId());

                Survey survey = new Survey();
                survey.addQuestion(R.string.question1, q1a.getText().toString());
                survey.addQuestion(R.string.question2, q2a.getText().toString());
                survey.addQuestion(R.string.question3, q3a.getText().toString());
                survey.addQuestion(R.string.question4, sp1.getSelectedItem().toString());
                final UserModel UserData =
                        new UserModel(FName.getText().toString(), LName.getText().toString(), DateofBirth.getText().toString(), Address.getText().toString(), survey);
                //logic
                if(checkUsername(username) && checkPassword(password)){
                    //send results to firebase adn finish on success toast
                    firebaseAuth.createUserWithEmailAndPassword(username, password)
                            .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>(){
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        FirebaseUser user = firebaseAuth.getCurrentUser();
                                        fireDB.child(user.getUid()).setValue(UserData);
                                        Toast.makeText(RegistrationActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                        //CB to Login to update UI
                                        intent.putExtra("username", username);
                                        intent.putExtra("password", password);
                                        setResult(100, intent);
                                        finish();
                                    }
                                    else{
                                        try {
                                            throw task.getException();
                                        }
                                        catch (FirebaseAuthUserCollisionException e){
                                            Toast.makeText(RegistrationActivity.this, "Email Already Exists", Toast.LENGTH_SHORT).show();
                                            setResult(-1);
                                            Log.d("On Complete:", e.getMessage());
                                         }
                                        catch (Exception e){
                                            Log.d("On Complete:", e.getMessage());
                                        }
                                    }
                                }
                            }
                    );

                }
                else{
                    Toast.makeText(RegistrationActivity.this, "Authentication Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private boolean checkUsername(String user){//Username is email
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
        return isUser.matches("^([0-9A-Za-z@.]{6,255})$");//6 or more no special chars
    }

}
