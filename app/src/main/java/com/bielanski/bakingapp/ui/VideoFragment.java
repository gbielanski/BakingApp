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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class VideoFragment extends Fragment {
    private static final String TAG = "VideoFragment";

    public static final String POSSITION = "POSSITION";
    public static final String PLAYSTATE = "PLAYSTATE";
    public static final String STEP = "STEP";
    public static final String RECIPE = "RECIPE";
    public static final String VIDEOURL = "VIDEOURL";
    private SimpleExoPlayer mExoPlayer;
    @BindView(R.id.player_view)
    SimpleExoPlayerView mPlayerView;
    private List<Recipe> mRecipes;
    private int mRecipeNumber;
    private int mStepNumber;
    private Unbinder unbinder;
    private long mCurrentPosition;
    private boolean mPlayVideoWhenForegrounded;
    private String videoURL;

    public VideoFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_video, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.gotowanie));

        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getLong(POSSITION, 0);
            mPlayVideoWhenForegrounded = savedInstanceState.getBoolean(PLAYSTATE, false);
            mStepNumber = savedInstanceState.getInt(STEP, 0);
            mRecipeNumber = savedInstanceState.getInt(RECIPE, 0);
            videoURL = savedInstanceState.getString(VIDEOURL, null);
            Log.v(TAG, "onCreateView " + POSSITION + " " + mCurrentPosition);
            Log.v(TAG, "onCreateView " + PLAYSTATE + " " + mPlayVideoWhenForegrounded);
            Log.v(TAG, "onCreateView " + STEP + " " + mStepNumber);
            Log.v(TAG, "onCreateView " + RECIPE + " " + mRecipeNumber);
        }
        return rootView;
    }

    private void initializePlayer() {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            //videoURL = null;

            if (mRecipes != null && mStepNumber > 0) {
                for (Recipe r : mRecipes) {
                    if (r.getId() == mRecipeNumber) {
                        Step step = r.getSteps().get(mStepNumber - 1);
                        videoURL = step.getVideoURL();
                    }
                }
            }

            Log.v(TAG, "onCreateView videoURL " + videoURL);
            Log.v(TAG, "onCreateView stepNumber " + mStepNumber);

            if (videoURL != null) {
                MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(videoURL),
                        new DefaultDataSourceFactory(getActivity(), Util.getUserAgent(getActivity(), "BakingApp")),
                        new DefaultExtractorsFactory(),
                        null,
                        null);

                mExoPlayer.prepare(mediaSource);
            }
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onDestroyView() {
        Log.v(TAG, "onDestroyView");

        releasePlayer();
        unbinder.unbind();
        super.onDestroyView();
    }

    public void setRecipes(List<Recipe> recipes) {
        Log.v(TAG, "setRecipes");
        this.mRecipes = recipes;
    }

    public void setRecipeNumber(int recipeNumber) {
        Log.v(TAG, "setRecipeNumber");

        this.mRecipeNumber= recipeNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.mStepNumber = stepNumber;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.v(TAG, "onSaveInstanceState " + POSSITION + " " + mCurrentPosition);
        Log.v(TAG, "onSaveInstanceState " + PLAYSTATE + " " + mPlayVideoWhenForegrounded);
        Log.v(TAG, "onSaveInstanceState " + STEP + " " + mStepNumber);
        Log.v(TAG, "onSaveInstanceState " + RECIPE + " " + mRecipeNumber);
        outState.putLong(POSSITION, mCurrentPosition);
        outState.putBoolean(PLAYSTATE, mPlayVideoWhenForegrounded);
        outState.putInt(STEP, mStepNumber);
        outState.putInt(RECIPE, mRecipeNumber);
        outState.putString(VIDEOURL, videoURL);
        //super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {

        super.onPause();
        Log.v(TAG, "onPause");
        mCurrentPosition = mExoPlayer.getCurrentPosition();
        mPlayVideoWhenForegrounded = mExoPlayer.getPlayWhenReady();
        mExoPlayer.setPlayWhenReady(false);
        releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v(TAG, "onStop");
        releasePlayer();
    }

    @Override
    public void onStart() {
        super.onStart();
        initializePlayer();
        mExoPlayer.seekTo(mCurrentPosition);
        mExoPlayer.setPlayWhenReady(mPlayVideoWhenForegrounded);
        Log.v(TAG, "onStart " + POSSITION + " " + mCurrentPosition);
        Log.v(TAG, "onStart " + PLAYSTATE + " " + mPlayVideoWhenForegrounded);
    }
}
