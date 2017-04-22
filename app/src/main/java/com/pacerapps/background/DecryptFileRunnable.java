package com.pacerapps.background;

import com.pacerapps.repository.filesystem.FileSystemUtil;

/**
 * Created by jeffwconaway on 7/21/16.
 */
public class DecryptFileRunnable implements Runnable {

    String encryptedDir;
    String decryptedDir;
    String originalName;
    EncryptionModel model;

    public static final String TAG = "jwc";

    public DecryptFileRunnable(String encryptedDir, String decryptedDir, String originalName, EncryptionModel model) {
        this.encryptedDir = encryptedDir;
        this.decryptedDir = decryptedDir;
        this.originalName = originalName;
        this.model = model;
    }

    @Override
    public void run() {
        new FileSystemUtil().decryptToFileSystem(encryptedDir, decryptedDir, originalName, model);
    }

}
