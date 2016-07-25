package com.pacerapps.music_player;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import com.pacerapps.testencryption.EncryptionModel;

import java.io.IOException;

/**
 * Created by jeffwconaway on 7/21/16.
 */
public class MusicPlayer {

    MediaPlayer mediaPlayer;
    EncryptionModel model;
    Context context;

    public static final String TAG = "jwc";

    public MusicPlayer(Context context, EncryptionModel model) {
        this.context = context;
        this.model = model;
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
