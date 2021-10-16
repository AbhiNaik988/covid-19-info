package com.example.covid_19_info;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covid_19_info.model.Root;
import com.example.covid_19_info.retrofit.RetrofitClient;
import com.hbb20.CountryCodePicker;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "TAG";
    private CountryCodePicker countryCodePickerp;
    private TextView casesTv,dailyCasesTv,recoveredTv,dailyRecoveredTv,deathsTv,dailyDeathsTv,activeTv,dailyActiveTv,criticalTv,populationTv,casePerMillTv;
    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).hide();

        casesTv = findViewById(R.id.cases_tv);
        dailyCasesTv = findViewById(R.id.daily_cases_tv);
        deathsTv = findViewById(R.id.deaths_tv);
        dailyDeathsTv = findViewById(R.id.daily_deaths_tv);
        recoveredTv = findViewById(R.id.recovered_tv);
        dailyRecoveredTv = findViewById(R.id.daily_recovered_tv);
        activeTv = findViewById(R.id.active_tv);
        dailyActiveTv = findViewById(R.id.daily_active_tv);
        criticalTv = findViewById(R.id.critical_tv);
        populationTv = findViewById(R.id.population_tv);
        pieChart = findViewById(R.id.pie_chart);
        casePerMillTv = findViewById(R.id.cases_per_million_tv);

        countryCodePickerp = findViewById(R.id.ccp);

        countryCodePickerp.setCountryForNameCode("IN");
        getData("India");
        countryCodePickerp.setOnCountryChangeListener(() -> {
            if(countryCodePickerp.getSelectedCountryName().equals("United States")){
                getData("USA");
            }
            else{
                getData(countryCodePickerp.getSelectedCountryName());
            }
        });
    }

    private void getData(String selectedCountryName) {
        RetrofitClient.getInstance().getApi().getCountries().enqueue(new Callback<List<Root>>() {
            @Override
            public void onResponse(@NonNull Call<List<Root>> call, @NonNull Response<List<Root>> response) {
                List<Root> list = response.body();

                for(int i = 0; i< Objects.requireNonNull(list).size(); i++){
                    if(list.get(i).getCountry().equals(selectedCountryName)){
                        Log.d(TAG, "onResponse: "+list.get(i));
                        if(list.get(i) != null){
                            casesTv.setText(String.valueOf(list.get(i).getCases()));
                            recoveredTv.setText(String.valueOf(list.get(i).getRecovered()));
                            deathsTv.setText(String.valueOf(list.get(i).getDeaths()));
                            activeTv.setText(String.valueOf(list.get(i).getActive()));
                            dailyCasesTv.setText(String.valueOf(list.get(i).getTodayCases()));
                            dailyRecoveredTv.setText(String.valueOf(list.get(i).getTodayRecovered()));
                            dailyDeathsTv.setText(String.valueOf(list.get(i).getTodayDeaths()));
                            dailyActiveTv.setText(String.valueOf(0));
                            criticalTv.setText(String.valueOf(list.get(i).getCritical()));
                            populationTv.setText(String.valueOf(list.get(i).getPopulation()));
                            casePerMillTv.setText(String.valueOf(list.get(i).getCasesPerOneMillion()));

                            setPieChart(list.get(i).getCases(),
                                    list.get(i).getRecovered(),
                                    list.get(i).getActive(),
                                    list.get(i).getDeaths(),
                                    list.get(i).getCritical());
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Root>> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "Failed!!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: "+call.request()+"; "+t.getMessage()+"; "+t.getCause());
            }
        });
    }

    private void setPieChart(int cases, int recovered, int active, int deaths, int critical) {
        pieChart.clearChart();
        pieChart.addPieSlice(new PieModel("Confirmed",cases, Color.parseColor("#8B4513")));
        pieChart.addPieSlice(new PieModel("Recovered",recovered, Color.GREEN));
        pieChart.addPieSlice(new PieModel("Active",active, Color.BLUE));
        pieChart.addPieSlice(new PieModel("Deaths",deaths, Color.RED));
        pieChart.addPieSlice(new PieModel("Critical",critical, Color.YELLOW));
        pieChart.startAnimation();
    }
}