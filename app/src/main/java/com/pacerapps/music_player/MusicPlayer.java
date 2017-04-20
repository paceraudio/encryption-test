package com.pacerapps.music_player;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.pacerapps.testencryption.EncryptionModel;

import java.io.IOException;

/**
 * Created by jeffwconaway on 7/21/16.
 */
public class MusicPlayer {

    MediaPlayer mediaPlayer;
    EncryptionModel model;
    Context context;

    SimpleExoPlayer exoPlayer;

    public static final String TAG = "jwc";

    public MusicPlayer(Context context, EncryptionModel model) {
        this.context = context;
        this.model = model;
        initExoPlayer();
    }

    private void initExoPlayer() {
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        LoadControl loadControl = new DefaultLoadControl();

        exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);
    }

    public void playOriginal() {
        mediaPlayer = new MediaPlayer();
        try {
            String songPath = model.getOriginalSongPath();
            if (songPath != null) {
                mediaPlayer.setDataSource(model.getOriginalSongPath());
                mediaPlayer.prepare();
                setUpErrorListener(mediaPlayer);
                mediaPlayer.start();
            }

        } catch (IOException e) {
            Log.e(TAG, "playOriginal: ", e);
        }
    }

    public void playDecrypted() {
        mediaPlayer = new MediaPlayer();
        try {
            String songPath = model.getDecryptedSongPath();
            if (songPath != null) {
                mediaPlayer.setDataSource(model.getDecryptedSongPath());
                mediaPlayer.prepare();
                setUpErrorListener(mediaPlayer);
                mediaPlayer.start();
            }

        } catch (IOException e) {
            Log.e(TAG, "playDecrypted: ", e);
        }
    }

    public void stop() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }

    public void setUpErrorListener(MediaPlayer mediaPlayer) {
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.d(TAG, "onError: " + what);
                return false;
            }
        });
    }

    public void playEncrypted() {
        mediaPlayer = new MediaPlayer();
        try {
            String songPath = model.getEncryptedSongPath();
            if (songPath != null) {
                mediaPlayer.setDataSource(model.getEncryptedSongPath());
                mediaPlayer.prepare();
                setUpErrorListener(mediaPlayer);
                mediaPlayer.start();
            }

        } catch (IOException e) {
            Log.e(TAG, "playEncrypted: ", e);
        }
    }

    public void playDecryptedFromDb() {
        mediaPlayer = new MediaPlayer();
        try {
            String songPath = model.getDecryptedFromDbSongPath();
            if (songPath != null) {
                mediaPlayer.setDataSource(model.getDecryptedFromDbSongPath());
                mediaPlayer.prepare();
                setUpErrorListener(mediaPlayer);
                mediaPlayer.start();
            }

        } catch (IOException e) {
            Log.e(TAG, "playDecryptedFromDb: ", e);
        }
    }
}
