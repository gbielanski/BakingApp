package com.bielanski.bakingapp.ui;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bielanski.bakingapp.R;

public class StepDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_details);
        InstructionsFragment instructionsFragment = new InstructionsFragment();
        VideoFragment videoFragment = new VideoFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.instructions_container, instructionsFragment)
                .add(R.id.video_container, videoFragment)
                .commit();
    }
}
