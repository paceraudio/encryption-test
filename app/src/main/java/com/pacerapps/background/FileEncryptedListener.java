package com.pacerapps.background;

/**
 * Created by jeffwconaway on 7/21/16.
 */
public interface FileEncryptedListener {

    void onFileEncrypted();

    void onFileDecrypted();

    void onSongEncryptedToDb();

    void onFileDecryptedFromDb(String decryptedPath);
}
