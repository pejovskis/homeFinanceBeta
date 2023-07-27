package com.example.homefinancebeta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        if (savedInstanceState == null) {
            // Load MenuFragment only if it's not already loaded (to avoid overlapping on configuration changes)
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.menuFragment, new MenuFragment())
                    .commit();
        }
    }

}