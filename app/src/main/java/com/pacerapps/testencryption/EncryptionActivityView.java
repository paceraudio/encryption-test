package com.pacerapps.testencryption;

/**
 * Created by jeffwconaway on 7/21/16.
 */

interface EncryptionActivityView {

    void onEncrypt();

    void onDecrypt();

    void onPlayOriginal();

    void onPlayDecrypted();

    void onPlayEncrypted();

    void onSongEncryptedToDb();

    void onSongDecryptedFromDb();

    void onPlayDecryptedFromDb();

    void onMusicPlaying(String songPath);

    void onMusicStopped();

    void onError(Exception e);

    void onFileWrittenFromRaw();


}
