package com.pacerapps.repository;

import com.pacerapps.background.FileEncryptedListener;

/**
 * Created by jeffwconaway on 4/20/17.
 */

public interface EncryptionRepository {

    void initRepository();

    void retrieveSongAttachment(String md5, String decryptedFromDbPath, String fileName);

    void decryptFile(String encryptedDir, String decryptedDir, String originalName, FileEncryptedListener listener);

    void encryptFile(String originalPath, String originalName, String encryptedDirectory, FileEncryptedListener listener);

    void createSongDocument(String md5, String path, String fileName);
}
