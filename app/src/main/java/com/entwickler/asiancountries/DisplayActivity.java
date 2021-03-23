package com.entwickler.asiancountries;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayActivity extends AppCompatActivity {

    private TextView display_name, display_capital, display_region, display_sub_region, display_population, display_borders, display_languages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);


        display_borders = findViewById(R.id.display_border);
        display_name = findViewById(R.id.display_name);
        display_capital = findViewById(R.id.display_capital);
        display_region = findViewById(R.id.display_region);
        display_sub_region = findViewById(R.id.display_sub_region);
        display_population = findViewById(R.id.display_population);
        display_languages = findViewById(R.id.display_language);

        Intent intent = getIntent();

        String name = intent.getStringExtra("Name");
        String capital = intent.getStringExtra("Capital");
        String region = intent.getStringExtra("Region");
        String sub_region = intent.getStringExtra("Subregion");
        long population = intent.getLongExtra("Population",0);
        String borders = intent.getStringExtra("Borders");
        String languages = intent.getStringExtra("Languages");

        if (borders.equals("")){
            borders="No Neighbours";
        }

        display_borders.setText("Borders : "+borders);
        display_name.setText(name);
        display_languages.setText(languages);
        display_sub_region.setText("Sub Region : "+sub_region);
        display_region.setText("Region : "+region);
        display_capital.setText("Capital : "+capital);
        display_population.setText("Population : "+population);


    }

}