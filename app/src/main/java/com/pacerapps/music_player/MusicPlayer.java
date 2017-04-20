package com.pacerapps.music_player;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.pacerapps.testencryption.EncryptionModel;

/**
 * Created by jeffwconaway on 7/21/16.
 */
public class MusicPlayer implements ExoPlayer.EventListener {

    EncryptionModel model;
    Context context;

    SimpleExoPlayer exoPlayer;

    private static final String TAG = "jwc";

    public MusicPlayer(Context context, EncryptionModel model) {
        this.context = context.getApplicationContext();
        this.model = model;
    }

    // ExoPlayer listener methods
    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
        Log.d(TAG, "onTimelineChanged() called with: timeline = [" + timeline + "], manifest = [" + manifest + "]");
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        Log.d(TAG, "onTracksChanged() called with: trackGroups = [" + trackGroups + "], trackSelections = [" + trackSelections + "]");
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        Log.d(TAG, "onLoadingChanged() called with: isLoading = [" + isLoading + "]");
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        String state = "";
        switch (playbackState) {
            case ExoPlayer.STATE_BUFFERING:
                state = "STATE_BUFFERING";
                break;
            case ExoPlayer.STATE_READY:
                state = "STATE_READY";
                break;
            case ExoPlayer.STATE_IDLE:
                state = "STATE_IDLE";
                break;
            case ExoPlayer.STATE_ENDED:
                state = "STATE_ENDED";
                break;
        }
        Log.d(TAG, "onPlayerStateChanged: playWhenReady: " + playWhenReady + " state: " + playbackState + " " + state);
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        Log.d(TAG, "onPlayerError: " + error);
    }

    @Override
    public void onPositionDiscontinuity() {
        Log.d(TAG, "onPositionDiscontinuity() called");
    }

    private void initExoPlayer() {
        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();

        exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);
    }

    private void prepareExo(String path) {
        initExoPlayer();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, "TestEncryption"));
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        Uri songUri = new Uri.Builder().path(path).build();
        MediaSource mediaSource = new ExtractorMediaSource(songUri, dataSourceFactory, extractorsFactory, null, null);
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.addListener(this);
        exoPlayer.prepare(mediaSource);
    }

    public void playOriginalExo() {
        prepareExo(model.getOriginalSongPath());
    }


    public void playDecryptedExo() {
        prepareExo(model.getDecryptedSongPath());
    }


    public void stopExo() {
        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(false);
            exoPlayer.release();
        }

    }

    public void playEncryptedExo() {
        prepareExo(model.getEncryptedSongPath());
    }

    public void playDecryptedFromDbExo() {
        prepareExo(model.getDecryptedFromDbSongPath());
    }

}
