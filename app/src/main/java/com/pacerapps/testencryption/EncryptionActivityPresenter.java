package com.pacerapps.testencryption;

import android.content.Context;

import com.pacerapps.music_player.MusicPlayer;

/**
 * Created by jeffwconaway on 7/21/16.
 */

class EncryptionActivityPresenter {

    EncryptionActivityView view;
    EncryptionModel model;
    Context context;
    MusicPlayer musicPlayer;

    EncryptionActivityPresenter(Context context, EncryptionActivityView view) {
        this.view = view;
        this.context = context;
        model = new EncryptionModel(context, this);
        musicPlayer = new MusicPlayer(context, model);
    }



    void makeDirectory() {
        boolean exists = model.makeDirectory();
        view.onDirCreated(exists);
    }

    String getSongDirectory() {
        return model.getSongDirectory();
    }

    String getEncryptDirectory() {
        return model.getEncryptedDirectory();
    }

    String getDecryptDirectory() {
        return model.getDecryptedDirectory();
    }

    void encryptFile() {
        model.encryptFile();
        //view.onEncrypt();
    }

    void decryptFile() {
        model.decryptFile();
        //view.onDecrypt();
    }

    void playOriginal() {
        musicPlayer.playOriginalExo();
        view.onPlayOriginal();
        onMusicPlaying(model.getOriginalSongPath());
    }

    void playDecrypted() {
        musicPlayer.playDecryptedExo();
        view.onPlayDecrypted();
        onMusicPlaying(model.getDecryptedSongPath());
    }

    void stop() {
        musicPlayer.stopExo();
        view.onStopClicked();
        view.onMusicStopped();
    }

    void playEncrypted() {
        musicPlayer.playEncryptedExo();
        view.onPlayEncrypted();
        onMusicPlaying(model.getEncryptedSongPath());
    }

    void playDecryptedFromDb() {
        musicPlayer.playDecryptedFromDbExo();
        view.onPlayDecryptedFromDb();
        onMusicPlaying(model.getDecryptedFromDbSongPath());
    }

    private void onMusicPlaying(String songPath) {
        view.onMusicPlaying(songPath);
    }

    void onFileEncrypted() {
        view.onEncrypt();
    }

    void onFileDecrypted() {
        view.onDecrypt();
    }

    void decryptFromDb() {
        model.decryptFromDb();
    }

    void encryptToDb() {
        model.encryptToDb();
    }

    void onSongEncryptedToDb() {
        view.onSongEncryptedToDb();
    }

    void onSongDecryptedFromDb() {
        view.onSongDecryptedFromDb();
    }


}
