package com.pacerapps.background;

import android.util.Log;

import com.pacerapps.testencryption.EncryptionModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by jeffwconaway on 7/21/16.
 */
public class EncryptFileRunnable implements Runnable {
    String originalPath;
    String encryptedDirectory;
    String originalName;
    FileEncryptedListener listener;

    public static final String TAG = "jwc";

    public EncryptFileRunnable(String originalPath, String originalName, String encryptedDirectory, FileEncryptedListener listener) {
        this.originalPath = originalPath;
        this.originalName = originalName;
        this.encryptedDirectory = encryptedDirectory;
        this.listener = listener;
    }

    @Override
    public void run() {
        encryptFile();
    }

    private void encryptFile() {
        try {
            FileInputStream fileInputStream = new FileInputStream(originalPath);
            Log.d(TAG, "encryptFile: file input stream available: " + fileInputStream.available());
            String encryptedFilePath = encryptedDirectory + "/" + originalName;
            File encryptedFile = new File(encryptedFilePath);
            encryptedFile.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(encryptedFile);
            SecretKeySpec secretKeySpec = new SecretKeySpec("123456789QWERTYU".getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            CipherOutputStream cipherOutputStream = new CipherOutputStream(fileOutputStream, cipher);

            int i;
            byte[] bytes = new byte[1024];
            while ((i = fileInputStream.read(bytes)) != -1) {
                cipherOutputStream.write(bytes, 0, i);
                Log.d(TAG, "encryptFile: " + i);
            }
            cipherOutputStream.flush();
            cipherOutputStream.close();
            fileInputStream.close();
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IOException e) {
            Log.e(TAG, "encryptFile: ", e);
        }
        listener.onFileEncrypted();
    }
}
