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
        view.onEncrypt();
    }

    public void decryptFile() {
        model.decryptFile();
        view.onDecrypt();
    }

    public void playOriginal() {
        musicPlayer.playOriginal();
        view.onPlayOriginal();
    }

    public void playDecrypted() {
        musicPlayer.playDecrypted();
        view.onPlayDecrypted();
    }

    public void stop() {
        musicPlayer.stop();
        view.onStopClicked();
    }

    public void playEncrypted() {
        musicPlayer.playEncrypted();
        view.onPlayEncrypted();
    }

    public void onFileEncrypted() {
        view.onEncrypt();
    }

    public void onFileDecrypted() {
        view.onDecrypt();
    }
}
