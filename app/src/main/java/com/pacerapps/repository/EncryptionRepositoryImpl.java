package com.pacerapps.repository;

import android.content.Context;
import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.pacerapps.background.DecryptFileRunnable;
import com.pacerapps.background.DecryptFromDbRunnable;
import com.pacerapps.background.EncryptFileRunnable;
import com.pacerapps.background.EncryptToDbRunnable;
import com.pacerapps.background.EncryptionModel;
import com.pacerapps.repository.database.CouchBaseLiteDbHelper;
import com.pacerapps.repository.filesystem.FileSystemUtil;
import com.pacerapps.repository.filesystem.Md5Util;
import com.pacerapps.testencryption.EncryptionModelImpl;
import com.pacerapps.testencryption.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

/**
 * Created by jeffwconaway on 7/22/16.
 */

public class EncryptionRepositoryImpl implements EncryptionRepository {

    private static final String TAG = "jwc";

    @Inject
    Context context;

    @Inject
    CouchBaseLiteDbHelper couchBaseLiteDbHelper;

    //@Inject
    EncryptionModelImpl model;

    public EncryptionRepositoryImpl(Context context, CouchBaseLiteDbHelper couchBaseLiteDbHelper) {
        this.context = context;
        this.couchBaseLiteDbHelper = couchBaseLiteDbHelper;
    }

    @Override
    public void setModel(EncryptionModelImpl model) {
        this.model = model;
    }

    @Override
    public void initRepository() {
        Log.d(TAG, "initRepository() called");

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                boolean dirsCreated = false;
                try {
                    dirsCreated = new FileSystemUtil().makeDirectories(model);
                    couchBaseLiteDbHelper.initDatabase();
                    if (dirsCreated) {
                        writeRawMp4ToFileSystem();
                    }

                } catch (IOException | CouchbaseLiteException e) {
                    Log.e(TAG, "initRepository: ", e);
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();

    }

    private void writeRawMp4ToFileSystem() {
        Log.d(TAG, "writeRawMp4ToFileSystem() called");
        InputStream in = context.getResources().openRawResource(R.raw.sound);
        String path = model.getSongDirectory() + "/sound.mp3";
        File toWrite = new File(path);
        try {
            toWrite.createNewFile();

            FileOutputStream out = new FileOutputStream(toWrite);
            byte[] buffer = new byte[1024];
            int len;

            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
                Log.d(TAG, "writeRawMp4ToFileSystem: " + len);
            }
            out.close();
            in.close();
            model.onFileWritingFromRaw(toWrite);
            //model.onSongResourceToFileSystem();
            //model.setOriginalSongPath(path);


        } catch (IOException e) {
            Log.e(TAG, "writeRawMp4ToFileSystem: ", e);
        }

    }

    @Override
    public void retrieveSongAttachment(String md5, String decryptedFromDbPath, String fileName) {
        try {
            couchBaseLiteDbHelper.retrieveSongAttachmentFromDb(md5, decryptedFromDbPath, fileName);
        } catch (IOException | CouchbaseLiteException e) {
            Log.e(TAG, "retrieveSongAttachment: ", e);
        }
    }

    @Override
    public void createSongDocument(String md5, String path, String fileName) {
        try {
            couchBaseLiteDbHelper.createSongDocumentInDb(md5, path, fileName);
        } catch (IOException | CouchbaseLiteException e) {
            Log.e(TAG, "createSongDocument: ", e);
        }
    }

    @Override
    public void encryptFile(String originalPath, String originalName, String encryptedDirectory, EncryptionModel listener) {
        launchEncryptFileRunnable(originalPath, originalName, encryptedDirectory, listener);
    }

    private void launchEncryptFileRunnable(String originalPath, String originalName, String encryptedDirectory, EncryptionModel listener) {
        Log.d(TAG, "encryptFile: in model running!");
        FileSystemUtil fileSystemUtil = new FileSystemUtil();
        File file = fileSystemUtil.checkFileExists(originalPath);
        if (file != null) {
            listener.onOriginalMd5Calculated(new Md5Util().calcMd5(file));
            EncryptFileRunnable encryptFileRunnable = new EncryptFileRunnable(originalPath, originalName, encryptedDirectory, listener);
            Thread thread = new Thread(encryptFileRunnable);
            thread.setName("File Encryption Thread");
            thread.start();
        }
    }

    @Override
    public void decryptFile(String encryptedDir, String decryptedDir, String originalName, EncryptionModel model) {
        launchDecryptFileRunnable(encryptedDir, decryptedDir, originalName, model);
    }

    private void launchDecryptFileRunnable(String encryptedDir, String decryptedDir, String originalName, EncryptionModel model) {
        Log.d(TAG, "launchDecryptFileRunnable: running!");
        DecryptFileRunnable encryptFileRunnable = new DecryptFileRunnable(encryptedDir, decryptedDir, originalName, model);
        Thread thread = new Thread(encryptFileRunnable);
        thread.setName("File Decryption Thread");
        thread.start();

    }

    @Override
    public void encryptToDb(String originalSongMd5, String originalSongPath, String originalName, EncryptionModelImpl model) {
        Thread thread = new Thread(new EncryptToDbRunnable(originalSongMd5, originalSongPath, originalName, model));
        thread.setName("Encrypt to DB Thread");
        thread.start();
    }

    @Override
    public void decryptFromDb(String originalSongMd5, String decryptedFromDbPath, String originalName, EncryptionModelImpl model) {
        Thread thread = new Thread(new DecryptFromDbRunnable(originalSongMd5, decryptedFromDbPath, originalName, model));
        thread.setName("Decrypt From Db Thread");
        thread.start();
    }
}
