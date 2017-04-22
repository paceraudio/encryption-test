package com.pacerapps.testencryption;

import android.content.Context;

import com.pacerapps.music_player.MusicPlayer;

import javax.inject.Inject;

/**
 * Created by jeffwconaway on 7/21/16.
 */

public class EncryptionActivityPresenter {

    EncryptionActivityView view;
    @Inject
    EncryptionModelImpl model;
    @Inject
    Context context;

    MusicPlayer musicPlayer;


    public EncryptionActivityPresenter(Context context, EncryptionModelImpl model) {
        this.context = context;
        this.model = model;
        this.model.setPresenter(this);
        musicPlayer = new MusicPlayer(context, model);
    }

    public void setView(EncryptionActivityView view) {
        this.view = view;
    }

    void makeDirectory() {
        model.makeDirectories();
        //view.onDirCreated(exists);
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


    public void onError(Exception e) {
        view.onError(e);
    }

    public void onFileWritingFromRaw() {
        view.onFileWritingFromRaw();
    }
}
