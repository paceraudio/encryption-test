package com.pacerapps.testencryption;

/**
 * Created by jeffwconaway on 7/21/16.
 */
public interface EncryptionActivityView {

    void onEncrypt();
    void onDecrypt();
    void onPlayOriginal();
    void onPlayDecrypted();
    void onStopClicked();

    void onDirCreated(boolean exists);

    void onPlayEncrypted();

    void onSongEncryptedToDb();

    void onSongDecryptedFromDb();

    void onPlayDecryptedFromDb();

    void onMusicPlaying(String songPath);

    void onMusicStopped();
}
