package com.example.covidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.covidapp.StatModel.Region;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;

public class Bargraph extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bargraph);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        BarChart chart = (BarChart) findViewById(R.id.BarGraph);
        Spinner spinner = findViewById(R.id.StatsSelection);
        spinner.setSelection(bundle.getInt("Selection"));

        ArrayList<String> TagList = bundle.getStringArrayList("TagList");
        ArrayList<BarEntry> Tconfirmed = new ArrayList<BarEntry>();
        ArrayList<BarEntry> TDead = new ArrayList<BarEntry>();
        ArrayList<BarEntry> TRecovered = new ArrayList<BarEntry>();
        ArrayList<BarEntry> confirmed = new ArrayList<BarEntry>();
        ArrayList<BarEntry> Dead = new ArrayList<BarEntry>();
        ArrayList<BarEntry> Recovered = new ArrayList<BarEntry>();
        XAxis xAxis = chart.getXAxis();

        for(int count = 0; count < TagList.size(); count++){
            Region region = (Region) bundle.get(TagList.get(count));
            Tconfirmed.add(new BarEntry(count, Float.parseFloat(region.getTotalConfirmedCases()) ));
            TDead.add(new BarEntry(count, Float.parseFloat(region.getTotalDeaths()) ));
            TRecovered.add(new BarEntry(count, Float.parseFloat(region.getTotalRecoveredCases()) ));
            confirmed.add(new BarEntry(count, Float.parseFloat(region.getConfirmedCases()) ));
            Dead.add(new BarEntry(count, Float.parseFloat(region.getDeaths()) ));
            Recovered.add(new BarEntry(count, Float.parseFloat(region.getRecoveredCases()) ));
        }
        xAxis.setValueFormatter(new IndexAxisValueFormatter(TagList));
        xAxis.setGranularity(1f);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView v = (TextView) view;
                BarDataSet dataSet = new BarDataSet(Tconfirmed, "Total Confirmed");
                switch (v.getText().toString()) {
                    case "Confirmed Cumul.":
                        dataSet = new BarDataSet(Tconfirmed, "Total Confirmed");
                        break;
                    case "Deaths Cumul.":
                        dataSet = new BarDataSet(TDead, "Total Dead");
                        break;
                    case "Recoveries Cumul.":
                        dataSet = new BarDataSet(TRecovered, "Total Recovered");
                        break;
                    case "Confirmed (By day)":
                        dataSet = new BarDataSet(confirmed, "Confirmed");
                        break;
                    case "Deaths (By day)":
                        dataSet = new BarDataSet(Dead, "Dead");
                        break;
                    case "Recoveries (By day)":
                        dataSet = new BarDataSet(Recovered, "Recovered");;
                        break;
                    default:
                        break;
                }
                BarData data = new BarData(dataSet);
                chart.setData(data);
                chart.invalidate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}
