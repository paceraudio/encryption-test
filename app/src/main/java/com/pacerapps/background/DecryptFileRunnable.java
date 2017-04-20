package com.pacerapps.background;

import com.pacerapps.EncApp;
import com.pacerapps.repository.EncryptionRepository;

import javax.inject.Inject;

/**
 * Created by jeffwconaway on 7/21/16.
 */
public class DecryptFileRunnable implements Runnable {

    String encryptedDir;
    String decryptedDir;
    String originalName;
    FileEncryptedListener listener;
    @Inject
    EncryptionRepository repository;
    public static final String TAG = "jwc";

    public DecryptFileRunnable(String encryptedDir, String decryptedDir, String originalName, FileEncryptedListener listener) {
        this.encryptedDir = encryptedDir;
        this.decryptedDir = decryptedDir;
        this.originalName = originalName;
        this.listener = listener;
        EncApp.getInstance().getAppComponent().inject(this);
    }

    @Override
    public void run() {
        repository.decryptFile(encryptedDir, decryptedDir, originalName, listener);
    }

}
