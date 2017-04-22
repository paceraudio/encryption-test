package com.pacerapps.repository;

import com.pacerapps.background.EncryptionModel;
import com.pacerapps.testencryption.EncryptionModelImpl;

/**
 * Created by jeffwconaway on 4/20/17.
 */

public interface EncryptionRepository {

    void initRepository();

    void setModel(EncryptionModelImpl model);

    void retrieveSongAttachment(String md5, String decryptedFromDbPath, String fileName);

    void decryptFile(String encryptedDir, String decryptedDir, String originalName, EncryptionModel listener);

    void encryptFile(String originalPath, String originalName, String encryptedDirectory, EncryptionModel listener);

    void createSongDocument(String md5, String path, String fileName);

    void encryptToDb(String originalSongMd5, String originalSongPath, String originalName, EncryptionModelImpl encryptionModel);

    void decryptFromDb(String originalSongMd5, String decryptedFromDbPath, String originalName, EncryptionModelImpl encryptionModel);
}
