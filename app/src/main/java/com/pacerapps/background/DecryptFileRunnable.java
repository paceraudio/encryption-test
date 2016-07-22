package com.pacerapps.background;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by jeffwconaway on 7/21/16.
 */
public class DecryptFileRunnable implements Runnable {

    String encryptedDir;
    String decryptedDir;
    String originalName;
    FileEncryptedListener listener;
    public static final String TAG = "jwc";

    public DecryptFileRunnable(String encryptedDir, String decryptedDir, String originalName, FileEncryptedListener listener) {
        this.encryptedDir = encryptedDir;
        this.decryptedDir = decryptedDir;
        this.originalName = originalName;
        this.listener = listener;
    }

    @Override
    public void run() {
        try {
            decrypt();
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException e) {
            Log.e(TAG, "run: ", e);
        }
    }

    void decrypt() throws IOException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException {

        String encrptPath = encryptedDir + "/" + originalName;
        String decrptPath = decryptedDir + "/" + originalName;
        File encryptedFile = new File(encrptPath);
        File decryptedFile = new File(decrptPath);
        decryptedFile.createNewFile();

        FileInputStream fileInputStream = new FileInputStream(encryptedFile);

        FileOutputStream fileOutputStream = new FileOutputStream(decryptedFile);
        SecretKeySpec secretKeySpec = new SecretKeySpec("123456789QWERTYU".getBytes(),
                "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        CipherInputStream cipherInputStream = new CipherInputStream(fileInputStream, cipher);
        int i;
        byte[] bytes = new byte[1024];
        while ((i = cipherInputStream.read(bytes)) != -1) {
            fileOutputStream.write(bytes, 0, i);
            Log.d(TAG, "encryptFile: " + i);
        }
        fileOutputStream.flush();
        fileOutputStream.close();
        cipherInputStream.close();
        listener.onFileDecrypted();
    }
}
