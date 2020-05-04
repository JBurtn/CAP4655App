package com.example.covidapp.ui.profile;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.covidapp.Firebase.Data.UserModel.UserModel;
import com.example.covidapp.Firebase.FirebaseBE;
import com.example.covidapp.R;
import com.example.covidapp.editProfile;


public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;
    private Intent editScreen;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //Const
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        LinearLayout Use = (LinearLayout) root.findViewById(R.id.profileData);
        Button Logout = root.findViewById(R.id.Logout);
        Button editProfile = root.findViewById(R.id.EditProfile);
         editScreen = new Intent(this.getContext().getApplicationContext(), editProfile.class);

        //Modify Objects
        Use.setGravity(Gravity.LEFT);
        Logout.setOnClickListener(this::OnLogout);
        editProfile.setOnClickListener(this::editProfile);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        //used for mediator in Profile ViewModel
        mViewModel.getStateMediator().observe(getViewLifecycleOwner(), new Observer<FirebaseBE.LoginState>() {
            @Override
            public void onChanged(FirebaseBE.LoginState loginState) {
                Log.d("State", String.valueOf(loginState));
            }
        });
        mViewModel.getUserData().observe(getViewLifecycleOwner(), new Observer<UserModel>() {
            @Override
            public void onChanged(UserModel x) {
                if(x == null)
                    return;
                Use.removeAllViews();
                TextView List = new TextView(root.getContext());

                List.setText(x.toString());
                List.setTypeface(Typeface.DEFAULT_BOLD);
                Use.addView(List);
                editProfile.setEnabled(true);
            }
        });
        return root;
    }
    public void editProfile(View view){
        //create and set bundle
        Bundle bundle = new Bundle();
        UserModel temp = mViewModel.getUserData().getValue();
        bundle.putString("First Name", temp.getFirst_name());
        bundle.putString("Last Name", temp.getLast_name());
        bundle.putString("Address", temp.getAddress());
        bundle.putString("DOB", temp.getDate_of_birth());
        editScreen.putExtras(bundle);
        startActivityForResult(editScreen, 52);

    }
    public void OnLogout(View view){
        mViewModel.logout();
    }

    public void onActivityResult(int request, int Result, Intent data){
        if(request == 52) {
           Bundle bundle = data.getExtras();
            Log.d("Called", "c");
           // get Current Package
           UserModel model = mViewModel.getUserData().getValue();
           //Redeclare with updated values
           model = new UserModel(bundle.getString("First Name"), bundle.getString("Last Name"),
                   bundle.getString("Address"), bundle.getString("DOB"), model.getSurvey());
           mViewModel.setUserDatabase(model);
        }
    }
}
