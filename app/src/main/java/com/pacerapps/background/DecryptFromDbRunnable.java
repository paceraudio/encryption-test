package com.pacerapps.background;

import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.pacerapps.database.EncDbUtil;

import java.io.IOException;

/**
 * Created by jeffwconaway on 7/25/16.
 */
public class DecryptFromDbRunnable implements Runnable {


    private String md5;
    private String decryptedFromDbPath;
    private String fileName;
    FileEncryptedListener listener;

    private static final String TAG = "jwc";

    public DecryptFromDbRunnable(String md5, String pathToWrite, String fileName, FileEncryptedListener listener) {
        this.md5 = md5;
        this.decryptedFromDbPath = pathToWrite;
        this.fileName = fileName;
        this.listener = listener;
    }


    @Override
    public void run() {
        try {
            EncDbUtil.retrieveSongAttachment(md5, decryptedFromDbPath, fileName);
            listener.onFileDecryptedFromDb(decryptedFromDbPath);
        } catch (IOException | CouchbaseLiteException e) {
            Log.e(TAG, "run: ", e);
        }
    }
}
