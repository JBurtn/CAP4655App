package com.example.covidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.covidapp.StatModel.Region;
import com.example.covidapp.StatModel.RegionHistory;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.Collections;

public class Linegraph extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linegraph);

        //declarations
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        LineChart chart = (LineChart) findViewById(R.id.LineGraph);
        Spinner spinner = findViewById(R.id.LineSelection);
        spinner.setSelection(bundle.getInt("Selection"));

        Region region = (Region) bundle.getSerializable("History");
        ArrayList<RegionHistory> histories = region.getHistory();
        ArrayList<String> days = new ArrayList<>();
        ArrayList<Entry> Tconfirmed = new ArrayList<Entry>();
        ArrayList<Entry> TDead = new ArrayList<Entry>();
        ArrayList<Entry> TRecovered = new ArrayList<Entry>();
        ArrayList<Entry> confirmed = new ArrayList<Entry>();
        ArrayList<Entry> Dead = new ArrayList<Entry>();
        ArrayList<Entry> Recovered = new ArrayList<Entry>();
        XAxis xAxis = chart.getXAxis();

        //LineGraph requires consistent and continuous points
        histories.sort((final RegionHistory a, final RegionHistory b) -> a.getDate().compareTo(b.getDate()));

        float lastTConfirmed = 0f;
        float lastTDead = 0f;
        float lastTRecovered = 0f;

        for(int count = 0; count < histories.size(); count++){
            RegionHistory item = histories.get(count);
            float tConfirmed = Float.parseFloat(item.getConfirmed());
            float tDead = Float.parseFloat(item.getDeaths());
            float tRecovered = Float.parseFloat(item.getRecovered());

            Tconfirmed.add(new Entry(count, tConfirmed ));
            TDead.add(new Entry(count, tDead ));
            TRecovered.add(new Entry(count, tRecovered ));
            //Descretize the data
            confirmed.add(new  Entry(count, tConfirmed - lastTConfirmed));
            Dead.add(new  Entry(count, tDead - lastTDead));
            Recovered.add(new  Entry(count,  tRecovered - lastTRecovered));
            days.add(item.getDate().replace("T00:00:00", ""));

            lastTConfirmed = tConfirmed;
            lastTDead = tDead;
            lastTRecovered = tRecovered;
        }
        Collections.sort(days);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(days));
        xAxis.setGranularity(1f);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView v = (TextView) view;
                LineDataSet dataSet = new LineDataSet(Tconfirmed, "Total Confirmed");
                switch (v.getText().toString()) {
                    case "Confirmed Cumul.":
                        dataSet = new LineDataSet(Tconfirmed, "Total Confirmed");
                        break;
                    case "Deaths Cumul.":
                        dataSet = new LineDataSet(TDead, "Total Dead");
                        break;
                    case "Recoveries Cumul.":
                        dataSet = new LineDataSet(TRecovered, "Total Recovered");
                        break;
                    case "Confirmed (By day)":
                        dataSet = new LineDataSet(confirmed, "Confirmed");
                        break;
                    case "Deaths (By day)":
                        dataSet = new LineDataSet(Dead, "Dead");
                        break;
                    case "Recoveries (By day)":
                        dataSet = new LineDataSet(Recovered, "Recovered");
                        break;
                    default:
                        break;
                }
                LineData data = new LineData(dataSet);
                chart.setData(data);
                chart.invalidate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}
