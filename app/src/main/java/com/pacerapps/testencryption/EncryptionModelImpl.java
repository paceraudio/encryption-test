package com.pacerapps.testencryption;

import android.content.Context;
import android.util.Log;

import com.pacerapps.background.DecryptFromDbRunnable;
import com.pacerapps.repository.EncryptionRepository;
import com.pacerapps.repository.filesystem.FileSystemUtil;
import com.pacerapps.repository.filesystem.Md5Util;

import java.io.File;

import javax.inject.Inject;

/**
 * Created by jeffwconaway on 7/21/16.
 */


public class EncryptionModelImpl implements com.pacerapps.background.EncryptionModel {

    public static final String TAG = "jwc";
    private String songDirectory = "";
    private String encryptedDirectory = "";
    private String decryptedDirectory = "";
    private String decryptedFromDbDirectory = "";

    //String originalPath;
    File originalFile;
    String originalName;
    private String originalSongPath;
    private String encryptedSongPath;
    private String decryptedSongPath;

    private String decryptedFromDbSongPath;

    private EncryptionActivityPresenter presenter;

    @Inject
    EncryptionRepository repository;

    @Inject
    Context context;

    private String originalSongMd5;
    private String decryptedSongMd5;
    private String decryptedFromDbMd5;

    public EncryptionModelImpl(EncryptionRepository repository) {
        this.repository = repository;
        this.repository.setModel(this);
    }

    public void setPresenter(EncryptionActivityPresenter presenter) {
        this.presenter = presenter;
    }

    public String getOriginalSongPath() {
        Log.d(TAG, "getOriginalSongPath() returned: " + originalSongPath);
        return originalSongPath;
    }

    public String getEncryptedSongPath() {
        return encryptedSongPath;
    }

    public String getDecryptedSongPath() {
        Log.d(TAG, "getDecryptedSongPath() returned: " + decryptedSongPath);
        return decryptedSongPath;
    }

    @Override
    public void onSongDirCreated(String songDirectory) {
        this.songDirectory = songDirectory;
    }

    @Override
    public void onEncryptedDirCreated(String encryptedDirectory) {
        this.encryptedDirectory = encryptedDirectory;
    }

    @Override
    public void onDecryptedDirCreated(String decryptedDirectory) {
        this.decryptedDirectory = decryptedDirectory;
    }

    @Override
    public void onDecryptedFromDbDirCreated(String decryptedFromDbDirectory) {
        this.decryptedFromDbDirectory = decryptedFromDbDirectory;
    }

    @Override
    public void onOriginalFileCreated(File originalFile) {
        this.originalFile = originalFile;
        originalName = originalFile.getName();
        originalSongPath = originalFile.getAbsolutePath();
        originalSongMd5 = new Md5Util().calcMd5(originalFile);

    }

    @Override
    public void onExoEnd() {
        presenter.onExoEnd();
    }

    @Override
    public void onFileWrittenFromRaw(File file) {
        Log.d(TAG, "onFileWrittenFromRaw() called with: file = [" + file.getAbsolutePath() + "]");
        this.originalFile = file;
        originalName = originalFile.getName();
        originalSongPath = originalFile.getAbsolutePath();
        originalSongMd5 = new Md5Util().calcMd5(originalFile);
        presenter.onFileWrittenFromRaw();
    }

    @Override
    public void onEncryptedFileCreated(File file) {
        encryptedSongPath = file.getAbsolutePath();
        presenter.onFileEncrypted();
    }

    @Override
    public void onDecryptedFileCreated(File file) {
        decryptedSongPath = file.getAbsolutePath();
    }

    @Override
    public void onDecryptedFromDbFileCreated(File file) {
        decryptedFromDbSongPath = file.getAbsolutePath();
    }

    @Override
    public void onOriginalMd5Calculated(String s) {
        originalSongMd5 = s;
    }

    @Override
    public void onFileEncrypted() {
        //new FileSystemUtil().listFiles(this);
        //presenter.onFileEncrypted();
    }

    @Override
    public void onFileDecrypted(File decryptedFile) {
        FileSystemUtil fileSystemUtil = new FileSystemUtil();
        if (fileSystemUtil.checkFileExistsAndIsFile(decryptedFile)) {
            decryptedSongPath = decryptedFile.getAbsolutePath();
            Md5Util md5Util = new Md5Util();
            decryptedSongMd5 = md5Util.calcMd5(decryptedFile);

            if (md5Util.compareMd5s(originalSongMd5, decryptedSongMd5)) {
                presenter.onFileDecrypted();
            }
        }
    }

    @Override
    public void onSongEncryptedToDb() {
        presenter.onSongEncryptedToDb();
    }

    @Override
    public void onFileDecryptedFromDb(String decryptedDbPath) {
        File file = new File(decryptedDbPath);
        if (file.exists()) {
            Md5Util md5Util = new Md5Util();
            decryptedFromDbMd5 = md5Util.calcMd5(file);

            if (md5Util.compareMd5s(originalSongMd5, decryptedFromDbMd5)) {
                decryptedFromDbSongPath = decryptedDbPath;
                presenter.onSongDecryptedFromDb();
            }
        }
    }

    @Override
    public void onError(Exception e) {
        presenter.onError(e);
    }

    public String getSongDirectory() {
        Log.d(TAG, "getSongDirectory: " + songDirectory);
        //listFiles();
        return songDirectory;
    }



    String getEncryptedDirectory() {
        Log.d(TAG, "getEncryptedDirectory: " + encryptedDirectory);
        return encryptedDirectory;
    }

    String getDecryptedDirectory() {
        Log.d(TAG, "getDecryptedDirectory: " + decryptedDirectory);
        return decryptedDirectory;
    }

    void encryptFile() {
        repository.encryptFile(originalSongPath, originalName, encryptedDirectory, this);
    }

    void decryptFile() {
        Log.d(TAG, "decryptFile: running!");
        repository.decryptFile(encryptedDirectory, decryptedDirectory, originalName, this);
    }

    void encryptToDb() {
        repository.encryptToDb(originalSongMd5, originalSongPath, originalName, this);
    }


    void decryptFromDb() {
        String decryptedFromDbPath = decryptedFromDbDirectory + "/" + originalName;
        repository.decryptFromDb(originalSongMd5, decryptedFromDbPath, originalName, this);

        Thread thread = new Thread(new DecryptFromDbRunnable(originalSongMd5, decryptedFromDbPath, originalName, this));
        thread.setName("Decrypt From Db Thread");
        thread.start();
    }

    public String getDecryptedFromDbSongPath() {
        return decryptedFromDbSongPath;
    }

    void makeDirectories() {
        repository.initRepository();
    }
}
