package com.bielanski.bakingapp.ui;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bielanski.bakingapp.R;
import com.bielanski.bakingapp.data.Recipe;
import com.bielanski.bakingapp.data.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

public class VideoFragment extends Fragment {
    private static final String TAG = "VideoFragment";
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private List<Recipe> recipes;
    private int recipeNumber;
    private int stepNumber;

    public VideoFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_video, container, false);
        mPlayerView = rootView.findViewById(R.id.player_view);
        mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.gotowanie));
        initializePlayer();
        return rootView;
    }

    private void initializePlayer() {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            String videoURL = null;

            if(recipes != null && stepNumber > 0) {
                for (Recipe r : recipes) {
                    if (r.getId() == recipeNumber) {
                        Step step = r.getSteps().get(stepNumber-1);
                        videoURL = step.getVideoURL();
                    }
                }
            }

            Log.v(TAG, "videoURL " + videoURL);
            Log.v(TAG, "stepNumber " + stepNumber);

            if (videoURL != null) {
                MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(videoURL),
                        new DefaultDataSourceFactory(getActivity(), Util.getUserAgent(getActivity(), "BakingApp")),
                        new DefaultExtractorsFactory(),
                        null,
                        null);

                mExoPlayer.prepare(mediaSource);
                mExoPlayer.setPlayWhenReady(true);
            }
        }
    }

    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Override
    public void onDestroyView() {
        releasePlayer();
        super.onDestroyView();
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public void setRecipeNumber(int recipeNumber) {
        this.recipeNumber = recipeNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
