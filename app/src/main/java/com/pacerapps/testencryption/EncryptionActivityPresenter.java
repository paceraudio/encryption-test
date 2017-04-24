package com.pacerapps.testencryption;

import android.content.Context;

import com.pacerapps.music_player.MusicPlayer;

import javax.inject.Inject;

/**
 * Created by jeffwconaway on 7/21/16.
 */

public class EncryptionActivityPresenter {

    private EncryptionActivityView view;
    @Inject
    EncryptionModelImpl model;
    @Inject
    Context context;

    private MusicPlayer musicPlayer;


    public EncryptionActivityPresenter(Context context, EncryptionModelImpl model) {
        this.context = context;
        this.model = model;
        this.model.setPresenter(this);
        musicPlayer = new MusicPlayer(context, model);
    }

    void setView(EncryptionActivityView view) {
        this.view = view;
    }

    void makeDirectory() {
        model.makeDirectories();
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
    }

    void decryptFile() {
        model.decryptFile();
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


    void onError(Exception e) {
        view.onError(e);
    }

    void onFileWrittenFromRaw() {
        view.onFileWrittenFromRaw();
    }

    void onExoEnd() {
        view.onMusicStopped();
    }
}
