package com.example.edf;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences=getSharedPreferences("details",MODE_PRIVATE);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        TextView name=findViewById(R.id.name);
        TextView clg=findViewById(R.id.clg_name);
        name.setText(preferences.getString("name",""));
        clg.setText(preferences.getString("clg",""));

    }
}
