package com.bielanski.bakingapp.ui;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;

import com.bielanski.bakingapp.R;


import static com.bielanski.bakingapp.ui.MainActivity.*;

public class StepsActivity extends AppCompatActivity {

    private int mIdExtra;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        Intent intent = getIntent();
        if(intent != null){
            mIdExtra = intent.getIntExtra(RECIPE_ID, 0);
            Log.v(TAG, "id " + mIdExtra);
            StepsFragment fragment = (StepsFragment)getSupportFragmentManager().findFragmentById(R.id.steps_fragment);
            fragment.setIdExtra(mIdExtra);

        }
    }
}
