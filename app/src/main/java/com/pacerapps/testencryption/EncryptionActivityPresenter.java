package com.pacerapps.testencryption;

import android.content.Context;

import com.pacerapps.music_player.MusicPlayer;

/**
 * Created by jeffwconaway on 7/21/16.
 */
public class EncryptionActivityPresenter {

    EncryptionActivityView view;
    EncryptionModel model;
    Context context;
    MusicPlayer musicPlayer;

    public EncryptionActivityPresenter(Context context, EncryptionActivityView view) {
        this.view = view;
        this.context = context;
        model = new EncryptionModel(context, this);
        musicPlayer = new MusicPlayer(context, model);
    }



    public void makeDirectory() {
        boolean exists = model.makeDirectory();
        view.onDirCreated(exists);
    }

    public String getSongDirectory() {
        return model.getSongDirectory();
    }

    public String getEncryptDirectory() {
        return model.getEncryptedDirectory();
    }

    public String getDecryptDirectory() {
        return model.getDecryptedDirectory();
    }

    public void encryptFile() {
        model.encryptFile();
        //view.onEncrypt();
    }

    public void decryptFile() {
        model.decryptFile();
        //view.onDecrypt();
    }

    public void playOriginal() {
        musicPlayer.playOriginal();
        view.onPlayOriginal();
        onMusicPlaying(model.getOriginalSongPath());
    }

    public void playDecrypted() {
        musicPlayer.playDecrypted();
        view.onPlayDecrypted();
        onMusicPlaying(model.getDecryptedSongPath());
    }

    public void stop() {
        musicPlayer.stop();
        view.onStopClicked();
        view.onMusicStopped();
    }

    public void playEncrypted() {
        musicPlayer.playEncrypted();
        view.onPlayEncrypted();
        onMusicPlaying(model.getEncryptedSongPath());
    }

    public void playDecryptedFromDb() {
        musicPlayer.playDecryptedFromDb();
        view.onPlayDecryptedFromDb();
        onMusicPlaying(model.getDecryptedFromDbSongPath());
    }

    public void onMusicPlaying(String songPath) {
        view.onMusicPlaying(songPath);
    }

    public void onFileEncrypted() {
        view.onEncrypt();
    }

    public void onFileDecrypted() {
        view.onDecrypt();
    }

    public void decryptFromDb() {
        model.decryptFromDb();
    }

    public void encryptToDb() {
        model.encryptToDb();
    }

    public void onSongEncryptedToDb() {
        view.onSongEncryptedToDb();
    }

    public void onSongDecryptedFromDb() {
        view.onSongDecryptedFromDb();
    }


}
