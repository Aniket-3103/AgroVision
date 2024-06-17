package com.mishraaniket.agrovision;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class regionWise extends AppCompatActivity {

    Spinner spinner;
    TextView area_soil;
    TextView nextButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region_wise);
        spinner=findViewById(R.id.spinner);
        area_soil=findViewById(R.id.area_soil);
        nextButton=findViewById(R.id.next);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(regionWise.this, news_article.class));
            }
        });

        // Define strings for states and soil types
        String[] states = {
                "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh",
                "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jharkhand",
                "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur",
                "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab",
                "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura",
                "Uttarakhand", "Uttar Pradesh", "West Bengal",

                // Union Territories
                "Ladakh", "Jammu and Kashmir", "Puducherry", "Lakshadweep",
                "Andaman and Nicobar Islands", "Dadra and Nagar Haveli", "Daman and Diu",
                "Chandigarh", "National Capital Territory of Delhi"
        };

        String[] soilTypes = {
                "Black soil, Red soil",  // Andhra Pradesh
                "Alluvial soil, Red and yellow soil",  // Arunachal Pradesh
                "Alluvial soil, Black soil",  // Assam
                "Alluvial soil",  // Bihar
                "Black soil, Red soil",  // Chhattisgarh
                "Laterite soil",  // Goa
                "Black soil, Alluvial soil",  // Gujarat
                "Alluvial soil",  // Haryana
                "Forest & Mountainous soil",  // Himachal Pradesh
                "Red and yellow soil, Laterite soil",  // Jharkhand
                "Black soil, Red soil, Laterite soil",  // Karnataka
                "Laterite soil, Red soil",  // Kerala
                "Black soil",  // Madhya Pradesh
                "Black soil, Red and yellow soil",  // Maharashtra
                "Red and yellow soil",  // Manipur
                "Laterite soil, Red and yellow soil",  // Meghalaya
                "Red and yellow soil",  // Mizoram
                "Alluvial soil, Red and yellow soil",  // Nagaland
                "Red and yellow soil, Laterite soil",  // Odisha
                "Alluvial soil",  // Punjab
                "Arid soil",  // Rajasthan
                "Mountain soil",  // Sikkim
                "Black soil, Red soil",  // Tamil Nadu
                "Black soil, Red soil",  // Telangana
                "Laterite soil",  // Tripura
                "Mountain soil",  // Uttarakhand
                "Alluvial soil",  // Uttar Pradesh
                "Alluvial soil, Laterite soil",  // West Bengal

                // Union Territories
                "Mountain soil",  // Ladakh
                "Alluvial soil, Mountain soil",  // Jammu and Kashmir
                "Sandy soil",  // Puducherry
                "Sandy soil",  // Lakshadweep
                "Laterite soil",  // Andaman and Nicobar Islands
                "Laterite soil",  // Dadra and Nagar Haveli
                "Alluvial soil",  // Daman and Diu
                "Alluvial soil",  // Chandigarh
                "Alluvial soil",  // National Capital Territory of Delhi
        };

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(regionWise.this, android.R.layout.simple_spinner_item,states);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String state=adapterView.getItemAtPosition(i).toString();

                area_soil.setText(state + ": " + soilTypes[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}