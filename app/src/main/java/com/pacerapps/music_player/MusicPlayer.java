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
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import com.pacerapps.repository.filesystem.FileSystemUtil;
import com.pacerapps.testencryption.EncryptionModelImpl;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by jeffwconaway on 7/21/16.
 */
public class MusicPlayer implements ExoPlayer.EventListener, TransferListener<DataSource> {

    EncryptionModelImpl model;
    Context context;

    SimpleExoPlayer exoPlayer;

    private static final String TAG = "jwc";

    public MusicPlayer(Context context, EncryptionModelImpl model) {
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
                model.onExoEnd();
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

    // TransferListener methods


    @Override
    public void onTransferStart(DataSource source, DataSpec dataSpec) {
        Log.d(TAG, "onTransferStart() called with: source = [" + source.getUri() + "], dataSpec length = [" + dataSpec.length + "]");
    }

    @Override
    public void onBytesTransferred(DataSource source, int bytesTransferred) {
        //Log.d(TAG, "onBytesTransferred() called with: source = [" + source.getUri() + "], bytesTransferred = [" + bytesTransferred + "]");
    }

    @Override
    public void onTransferEnd(DataSource source) {
        Log.d(TAG, "onTransferEnd: ");
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

    private void prepareExoDecryptWhilePlaying(String path) {
        try {
            initExoPlayer();
            Cipher cipher = new FileSystemUtil().initCipher();
            EncryptedDataSourceFactory dataSourceFactory = new EncryptedDataSourceFactory(cipher, this);
            Uri songUri = new Uri.Builder().path(path).build();
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(songUri, dataSourceFactory, extractorsFactory, null, null);
            exoPlayer.setPlayWhenReady(true);
            exoPlayer.addListener(this);
            exoPlayer.prepare(mediaSource);
        } catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException e) {
            e.printStackTrace();
        }

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
        //prepareExo(model.getEncryptedSongPath());
        prepareExoDecryptWhilePlaying(model.getEncryptedSongPath());
    }

    public void playDecryptedFromDbExo() {
        prepareExo(model.getDecryptedFromDbSongPath());
    }

}
