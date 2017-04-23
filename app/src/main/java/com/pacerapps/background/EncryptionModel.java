package com.pacerapps.background;

import java.io.File;

/**
 * Created by jeffwconaway on 7/21/16.
 */
public interface EncryptionModel {

    void onFileEncrypted();

    void onFileDecrypted(File decryptedFile);

    void onSongEncryptedToDb();

    void onFileDecryptedFromDb(String decryptedPath);

    void onError(Exception e);

    void onSongDirCreated(String songDirectory);

    void onEncryptedDirCreated(String encryptedDirectory);

    void onDecryptedDirCreated(String decryptedDirectory);

    void onDecryptedFromDbDirCreated(String decryptedFromDbDirectory);

    void onOriginalFileCreated(File originalFile);

    void onEncryptedFileCreated(File file);

    void onDecryptedFileCreated(File file);

    void onDecryptedFromDbFileCreated(File file);

    void onOriginalMd5Calculated(String s);

    void onFileWritingFromRaw(File file);

    void onExoEnd();

    //void onSongResourceToFileSystem();
}
