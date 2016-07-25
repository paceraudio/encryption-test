package com.pacerapps.background;

import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.pacerapps.database.EncDbUtil;

import java.io.IOException;

/**
 * Created by jeffwconaway on 7/25/16.
 */
public class EncryptToDbRunnable implements Runnable {

    private String md5;
    private String path;
    private String fileName;
    FileEncryptedListener listener;

    private static final String TAG = "jwc";

    public EncryptToDbRunnable(String md5, String path, String fileName, FileEncryptedListener listener) {
        this.md5 = md5;
        this.path = path;
        this.fileName = fileName;
        this.listener = listener;
    }

    @Override
    public void run() {

        try {
            EncDbUtil.createSongDocument(md5, path, fileName);
            listener.onSongEncryptedToDb();
        } catch (IOException | CouchbaseLiteException e) {
            Log.e(TAG, "run: ", e);
        }
    }
}
