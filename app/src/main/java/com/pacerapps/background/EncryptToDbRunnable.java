package com.pacerapps.background;

import com.pacerapps.EncApp;
import com.pacerapps.repository.EncryptionRepository;

import javax.inject.Inject;

/**
 * Created by jeffwconaway on 7/25/16.
 */
public class EncryptToDbRunnable implements Runnable {

    private String md5;
    private String path;
    private String fileName;
    private EncryptionModel model;

    @Inject
    EncryptionRepository repository;

    private static final String TAG = "jwc";

    public EncryptToDbRunnable(String md5, String path, String fileName, EncryptionModel model) {
        this.md5 = md5;
        this.path = path;
        this.fileName = fileName;
        this.model = model;
        EncApp.getInstance().getAppComponent().inject(this);
    }

    @Override
    public void run() {
        repository.createSongDocument(md5, path, fileName);
        model.onSongEncryptedToDb();
    }
}
