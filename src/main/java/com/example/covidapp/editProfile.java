package com.example.covidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.covidapp.R;


public class editProfile extends AppCompatActivity {
    private String uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Intent caller = getIntent();
        Bundle bundle = caller.getExtras();

        EditText Fname = findViewById(R.id.Fname);
        EditText lname = findViewById(R.id.Lname);
        EditText address = findViewById(R.id.Address);
        EditText DOB = findViewById(R.id.DOB);
        Button submit = findViewById(R.id.button2);

       Fname.setText(bundle.getString("First Name"));
       lname.setText(bundle.getString("Last Name"));
       address.setText(bundle.getString("Address"));
       DOB.setText(bundle.getString("DOB"));

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent response = new Intent();
                //repackage and send back
                bundle.putString("First Name", Fname.getText().toString());
                bundle.putString("Last Name", lname.getText().toString());
                bundle.putString("Address", address.getText().toString());
                bundle.putString("DOB", DOB.getText().toString());
                bundle.putString("URI", uri);

                response.putExtras(bundle);
                setResult(52, response);
                finish();
            }
        });
    }
    // added but never implemented
    public void forImage(View view){
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, Activity.RESULT_OK);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== resultCode && resultCode==RESULT_OK){

            uri = data.getData().getLastPathSegment();

        }
    }
}
