package com.pacerapps.testencryption;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;

import com.pacerapps.background.DecryptFileRunnable;
import com.pacerapps.background.EncryptFileRunnable;
import com.pacerapps.background.FileEncryptedListener;

/**
 * Created by jeffwconaway on 7/21/16.
 */


public class EncryptionModel implements FileEncryptedListener {

    public static final String TAG = "jwc";
    private String songDirectory = "";
    private String encryptedDirectory = "";
    private String decryptedDirectory = "";
    Context context;

    //String originalPath;
    File originalFile;
    String originalName;
    private String originalSongPath;
    private String encryptedSongPath;
    private String decryptedSongPath;

    private EncryptionActivityPresenter presenter;

    private String originalSongMd5;

    public EncryptionModel(Context context, EncryptionActivityPresenter presenter) {
        this.context = context;
        this.presenter = presenter;
    }


    public String getOriginalSongPath() {
        Log.d(TAG, "getOriginalSongPath() returned: " + originalSongPath);
        return originalSongPath;
    }

    public void setOriginalSongPath(String path) {

        originalSongPath = path;
    }

    public String getEncryptedSongPath() {
        return encryptedSongPath;
    }

    public void setEncryptedSongPath(String path) {
        encryptedSongPath = path;
    }

    public String getDecryptedSongPath() {
        Log.d(TAG, "getDecryptedSongPath() returned: " + decryptedSongPath);
        return decryptedSongPath;
    }

    public void setDecryptedSongPath(String path) {
        decryptedSongPath = path;
    }

    public String getOriginalSongMd5() {
        return originalSongMd5;
    }

    public void encryptFile() {
        Log.d(TAG, "encryptFile: in model running!");
        EncryptFileRunnable encryptFileRunnable = new EncryptFileRunnable(originalSongPath, originalName, encryptedDirectory, this);
        Thread thread = new Thread(encryptFileRunnable);
        thread.setName("File Encryption Thread");
        thread.start();
    }

    @Override
    public void onFileEncrypted() {
        listFiles();
        presenter.onFileEncrypted();
    }

    @Override
    public void onFileDecrypted() {
        listFiles();
        presenter.onFileDecrypted();
    }

    public void decryptFile() {
        Log.d(TAG, "decryptFile: running!");
        DecryptFileRunnable decryptFileRunnable = new DecryptFileRunnable(encryptedDirectory, decryptedDirectory, originalName, this);
        Thread thread = new Thread(decryptFileRunnable);
        thread.setName("File Decryption Thread");
        thread.start();
    }

    public boolean makeDirectory() {

        String path = context.getExternalFilesDir(null) + "/song";
        String encryptedDir = context.getExternalFilesDir(null) + "/encrypted";
        String decryptedDir = context.getExternalFilesDir(null) + "/decrypted";
        File file = new File(path);
        boolean exists = (file.mkdirs() || file.isDirectory());
        if (exists) {
            songDirectory = file.getAbsolutePath();
        }
        File encrypt = new File(encryptedDir);
        boolean encryptExists = (encrypt.mkdirs() || encrypt.isDirectory());
        if (encryptExists) {
            encryptedDirectory = encrypt.getAbsolutePath();
        }
        File decrypted = new File(decryptedDir);
        boolean decryptedExists = (decrypted.mkdirs() || decrypted.isDirectory());
        if (decryptedExists) {
            decryptedDirectory = decrypted.getAbsolutePath();
        }
        listFiles();
        return exists && encryptExists && decryptedExists;
    }

    public String getSongDirectory() {
        Log.d(TAG, "getSongDirectory: " + songDirectory);
        //listFiles();
        return songDirectory;
    }

    public String getEncryptedDirectory() {
        Log.d(TAG, "getEncryptedDirectory: " + encryptedDirectory);
        return encryptedDirectory;
    }

    public String getDecryptedDirectory() {
        Log.d(TAG, "getDecryptedDirectory: " + decryptedDirectory);
        return decryptedDirectory;
    }

    public void listFiles() {
        File songDir = new File(songDirectory);
        File[] files = songDir.listFiles();
        for (File file : files) {
            Log.d(TAG, "listFiles: " + file.getAbsolutePath());
            originalFile = file;
            originalName = file.getName();
            setOriginalSongPath(file.getAbsolutePath());
        }
        File encryptFile = new File(encryptedDirectory);
        File[] encrptFiles = encryptFile.listFiles();
        for (File file : encrptFiles) {
            Log.d(TAG, "listFiles: encrypted: " + file.getAbsolutePath());
            setEncryptedSongPath(file.getAbsolutePath());
        }

        File decryptedFile = new File(decryptedDirectory);
        File[] decrptFiles = decryptedFile.listFiles();
        for (File file : decrptFiles) {
            Log.d(TAG, "listFiles: decrypted: " + file.getAbsolutePath());
            setDecryptedSongPath(file.getAbsolutePath());
        }
    }

    public static File checkFileExistsForMd5(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            return file;
        }
        return null;
    }

    public static String calcMd5(File file) {
        BufferedInputStream bis = null;
        DigestInputStream dis = null;
        byte[] fileBuf = new byte[2048];

        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            MessageDigest md = MessageDigest.getInstance("MD5");
            dis = new DigestInputStream(bis, md);

            while (dis.read(fileBuf) == fileBuf.length) ;
            return Base64.encodeToString(md.digest(), Base64.NO_WRAP);

        } catch (Exception e) {
//            Log.d(TAG, "calcMd5: exception: " + e.getMessage());
        } finally {
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }
}
