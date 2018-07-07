package com.bielanski.bakingapp.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bielanski.bakingapp.R;
import com.bielanski.bakingapp.StepsAdapter;
import com.bielanski.bakingapp.data.Recipe;
import com.bielanski.bakingapp.data.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class StepsFragment extends Fragment {
    private static final String TAG = "StepsFragment";
    private List<Recipe> mRecipes;
    private int mRecipeNumber;
    private Unbinder unbinder;
    @BindView(R.id.steps_recycler_view) RecyclerView recyclerView;

    public StepsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_steps, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);


        ArrayList<Step> steps = new ArrayList<>();
        if(mRecipes != null) {
            for (Recipe r : mRecipes) {
                if (r.getId() == mRecipeNumber) {
                    steps = r.getSteps();
                }
            }
        }
        StepsAdapter mStepsAdapter = new StepsAdapter(steps, (StepsAdapter.OnClickStepHandler) getActivity());
        recyclerView.setAdapter(mStepsAdapter);

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setRecipes(List<Recipe> recipes) {
        this.mRecipes = recipes;
    }

    public void setRecipeNumber(int recipeNumber) {
        this.mRecipeNumber = recipeNumber;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
