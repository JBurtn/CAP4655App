package com.example.covidapp.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.annotation.NonNull;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.SortedList;

import com.example.covidapp.Bargraph;
import com.example.covidapp.Linegraph;
import com.example.covidapp.R;
import com.example.covidapp.StatModel.Region;
import com.example.covidapp.VolleyBE;

import java.util.ArrayList;
import java.util.SortedMap;
import java.util.Timer;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private View constraintLayout;
    private Button History;
    private Button setCountry;
    private TextView Name;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        VolleyBE BE = VolleyBE.getInstance(getContext());
        Spinner spinner = root.findViewById(R.id.StatList);
        Button compare = root.findViewById(R.id.compare);
        LinearLayout layout = root.findViewById(R.id.Countries);
        String[] names = getResources().getStringArray(R.array.Region_Code);
        Intent BarGraph = new Intent(getContext(), Bargraph.class);//set Intent
        Intent LineGraph = new Intent(getContext(), Linegraph.class);
        Bundle GetHistory = new Bundle();// set Intent
        Bundle Comp = new Bundle();

        dashboardViewModel.QueryStats(BE, "global", true);

        for (String name : names) {
            constraintLayout = LayoutInflater.from(root.getContext()).inflate(R.layout.countrylayout, null);
            History = constraintLayout.findViewById(R.id.History);
            setCountry = constraintLayout.findViewById(R.id.GetCountry);
            Name = constraintLayout.findViewById(R.id.CountryName);
            TextView Value = constraintLayout.findViewById(R.id.Value);

            Name.setText(name);
            setCountry.setTag(name);
            Value.setTag(name);
            History.setTag(name);

            setCountry.setOnClickListener(v -> {
                CheckBox c = (CheckBox) v;
                String tag = (String) c.getTag();
                if (c.isChecked()) {
                    Log.d("Checked", tag);
                    dashboardViewModel.addCountries(tag);
                    Comp.putSerializable(tag, dashboardViewModel.getCountries().get(tag));
                    Comp.putStringArrayList("TagList", dashboardViewModel.getSelector());
                    if(dashboardViewModel.getSelector().size() > 1)
                        compare.setEnabled(true);
                } else {
                    Log.d("Unchecked", tag);
                    if(Comp.containsKey(tag))
                        Comp.remove(tag);
                        dashboardViewModel.removeCountries(tag);
                        Comp.putStringArrayList("TagList", dashboardViewModel.getSelector());
                    if(dashboardViewModel.getSelector().size() <= 1)
                        compare.setEnabled(false);
                }
            });
            History.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button b = (Button) v;
                    String tag = (String) b.getTag();
                    Region r = dashboardViewModel.getCountries().get(tag);
                    if(r.hasHistory()){

                        GetHistory.putSerializable("History", r);
                        GetHistory.putInt("Selection", spinner.getSelectedItemPosition());
                        LineGraph.putExtras(GetHistory);
                        startActivity(LineGraph);
                    }
                    else{
                        dashboardViewModel.QueryStats(BE, tag, false);
                        dashboardViewModel.setFlag(true);
                    }
                }

            });
            layout.addView(constraintLayout);
        }


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GetHistory.putInt("Selection", position);
                Comp.putInt("Selection", position);
                if(position != 0){
                    TextView v = (TextView) view;
                    for(int text = 0; text < layout.getChildCount(); text++){
                        ConstraintLayout c = (ConstraintLayout) layout.getChildAt(text);
                        TextView value = c.findViewById(R.id.Value);
                        Region r = dashboardViewModel.getCountries().get((String) value.getTag());

                        switch (v.getText().toString()) {
                            case "Confirmed Cumul.":
                                value.setText(r.getTotalConfirmedCases());
                                break;
                            case "Deaths Cumul.":
                                value.setText(r.getTotalDeaths());
                                break;
                            case "Recoveries Cumul.":
                                value.setText(r.getTotalRecoveredCases());
                                break;
                            case "Confirmed (By day)":
                                value.setText(r.getConfirmedCases());
                                break;
                            case "Deaths (By day)":
                                value.setText(r.getDeaths());
                                break;
                            case "Recoveries (By day)":
                                value.setText(r.getRecoveredCases());
                                break;
                            default:
                                break;
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        compare.setOnClickListener(v -> {
            final ArrayList<String> selector = dashboardViewModel.getSelector();
            final SortedMap<String, Region> list = dashboardViewModel.getCountries();
            if(list.keySet().containsAll(selector)) {
                BarGraph.putExtras(Comp);
                startActivity(BarGraph);
            }
        });
        dashboardViewModel.getStatus().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String aBoolean) {
                if(dashboardViewModel.getFlag()){
                    GetHistory.putSerializable("History", dashboardViewModel.getCountries().get(aBoolean));
                    LineGraph.putExtras(GetHistory);
                    startActivity(LineGraph);
                    dashboardViewModel.setFlag(false);
                }
            }
        });
        return root;
    }
}
