package com.pacerapps.background;

import com.pacerapps.repository.filesystem.FileSystemUtil;

/**
 * Created by jeffwconaway on 7/21/16.
 */
public class EncryptFileRunnable implements Runnable {
    private String originalPath;
    private String encryptedDirectory;
    private String originalName;
    private EncryptionModel listener;
    public static final String TAG = "jwc";

    public EncryptFileRunnable(String originalPath, String originalName, String encryptedDirectory, EncryptionModel listener) {
        this.originalPath = originalPath;
        this.originalName = originalName;
        this.encryptedDirectory = encryptedDirectory;
        this.listener = listener;
    }

    @Override
    public void run() {
        new FileSystemUtil().encryptToFileSystem(originalPath, originalName, encryptedDirectory, listener);
    }

}
