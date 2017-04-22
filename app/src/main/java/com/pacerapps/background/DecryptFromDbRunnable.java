package com.pacerapps.background;

import com.pacerapps.EncApp;
import com.pacerapps.repository.EncryptionRepository;

import javax.inject.Inject;

/**
 * Created by jeffwconaway on 7/25/16.
 */

public class DecryptFromDbRunnable implements Runnable {


    private String md5;
    private String decryptedFromDbPath;
    private String fileName;
    EncryptionModel model;

    @Inject
    EncryptionRepository repository;

    private static final String TAG = "jwc";

    public DecryptFromDbRunnable(String md5, String pathToWrite, String fileName, EncryptionModel model) {
        this.md5 = md5;
        this.decryptedFromDbPath = pathToWrite;
        this.fileName = fileName;
        this.model = model;
        EncApp.getInstance().getAppComponent().inject(this);
    }


    @Override
    public void run() {
        repository.retrieveSongAttachment(md5, decryptedFromDbPath, fileName);
        model.onFileDecryptedFromDb(decryptedFromDbPath);
    }
}
