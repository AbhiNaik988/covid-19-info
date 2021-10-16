package com.example.covid_19_info;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "TAG";
    private CountryCodePicker countryCodePickerp;
    private TextView casesTv,dailyCasesTv,recoveredTv,dailyRecoveredTv,deathsTv,dailyDeathsTv,activeTv,dailyActiveTv,criticalTv,populationTv,casePerMillTv;
    private PieChart pieChart;
    private TextView lastUpdatedTv;

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
        lastUpdatedTv = findViewById(R.id.last_updated_tv);

        countryCodePickerp = findViewById(R.id.ccp);

        countryCodePickerp.setCountryForNameCode("IN");
        getData("IN");
        countryCodePickerp.setOnCountryChangeListener(() -> {
            getData(countryCodePickerp.getSelectedCountryNameCode());
        });
    }

    private void getData(String selectedCountryNameCode) {
        RetrofitClient.getInstance().getApi().getCountries().enqueue(new Callback<List<Root>>() {
            @Override
            public void onResponse(@NonNull Call<List<Root>> call, @NonNull Response<List<Root>> response) {
                List<Root> list = response.body();

                for(int i = 0; i< Objects.requireNonNull(list).size(); i++){
                    if(list.get(i).getCountryInfo().iso2 != null){
                        if(list.get(i).getCountryInfo().iso2.equals(selectedCountryNameCode)){
//                            Log.d(TAG, "onResponse: "+selectedCountryNameCode+" : "+list.get(i).getCountryInfo().iso2);
                            if(list.get(i) != null){
                                casesTv.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(list.get(i).getCases()));
                                recoveredTv.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(list.get(i).getRecovered()));
                                deathsTv.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(list.get(i).getDeaths()));
                                activeTv.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(list.get(i).getActive()));
                                dailyCasesTv.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(list.get(i).getTodayCases()));
                                dailyRecoveredTv.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(list.get(i).getTodayRecovered()));
                                dailyDeathsTv.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(list.get(i).getTodayDeaths()));
                                dailyActiveTv.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(0));
                                criticalTv.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(list.get(i).getCritical()));
                                populationTv.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(list.get(i).getPopulation()));
                                casePerMillTv.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(list.get(i).getCasesPerOneMillion()));
                                lastUpdatedTv.setText(milliToDate(list.get(i).getUpdated()));

                                setPieChart(list.get(i).getCases(),
                                        list.get(i).getRecovered(),
                                        list.get(i).getActive(),
                                        list.get(i).getDeaths(),
                                        list.get(i).getCritical());
                            }
                        }    
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Root>> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "Failed!!", Toast.LENGTH_SHORT).show();
//                Log.d(TAG, "onFailure: "+call.request()+"; "+t.getMessage()+"; "+t.getCause());
            }
        });
    }

    private String milliToDate(long updated) {
        String dateFormat = "dd/MM/yyyy hh:mm:ss";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(updated);
        String time = formatter.format(calendar.getTime());
//        Log.d(TAG, "milliToDate: "+time);
        return time;
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