package com.pacerapps.background;

import com.pacerapps.EncApp;
import com.pacerapps.repository.EncryptionRepository;

import javax.inject.Inject;

/**
 * Created by jeffwconaway on 7/21/16.
 */
public class EncryptFileRunnable implements Runnable {
    String originalPath;
    String encryptedDirectory;
    String originalName;
    FileEncryptedListener listener;

    @Inject
    EncryptionRepository repository;

    public static final String TAG = "jwc";

    public EncryptFileRunnable(String originalPath, String originalName, String encryptedDirectory, FileEncryptedListener listener) {
        this.originalPath = originalPath;
        this.originalName = originalName;
        this.encryptedDirectory = encryptedDirectory;
        this.listener = listener;
        EncApp.getInstance().getAppComponent().inject(this);
    }

    @Override
    public void run() {
        repository.encryptFile(originalPath, originalName, encryptedDirectory, listener);
    }

}
