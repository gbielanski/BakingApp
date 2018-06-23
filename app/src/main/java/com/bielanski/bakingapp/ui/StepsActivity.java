package com.bielanski.bakingapp.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.bielanski.bakingapp.R;

public class StepsActivity extends AppCompatActivity {

    private static final String TAG = "StepsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        Intent intent = getIntent();
        if(intent != null){
            int idExtra = intent.getIntExtra(MainActivity.RECIPE_ID, 0);
            Log.v(TAG, "id " + idExtra);
        }
    }
}
