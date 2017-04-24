package com.pacerapps.repository.filesystem;

import android.content.Context;
import android.util.Log;

import com.pacerapps.EncApp;
import com.pacerapps.background.EncryptionModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;

import static com.pacerapps.util.Constants.TAG;

/**
 * Created by jeffwconaway on 4/21/17.
 */

public class FileSystemUtil {

    @Inject
    Context context;

    public FileSystemUtil() {
        EncApp.getInstance().getAppComponent().inject(this);

    }

    public boolean makeDirectories(EncryptionModel model) {

        String path = context.getExternalFilesDir(null) + "/song";
        String encryptedDir = context.getExternalFilesDir(null) + "/encrypted";
        String decryptedDir = context.getExternalFilesDir(null) + "/decrypted";
        String decryptedFromDbDir = context.getExternalFilesDir(null) + "/decrypted_from_db";
        File file = new File(path);
        boolean exists = (file.mkdirs() || file.isDirectory());
        if (exists) {
            String songDirectory = file.getAbsolutePath();
            model.onSongDirCreated(songDirectory);
        }
        File encrypt = new File(encryptedDir);
        boolean encryptExists = (encrypt.mkdirs() || encrypt.isDirectory());
        if (encryptExists) {
            String encryptedDirectory = encrypt.getAbsolutePath();
            model.onEncryptedDirCreated(encryptedDirectory);
        }
        File decrypted = new File(decryptedDir);
        boolean decryptedExists = (decrypted.mkdirs() || decrypted.isDirectory());
        if (decryptedExists) {
            String decryptedDirectory = decrypted.getAbsolutePath();
            model.onDecryptedDirCreated(decryptedDirectory);
        }
        File decryptedDb = new File(decryptedFromDbDir);
        boolean decryptedFromDbExists = (decryptedDb.mkdirs() || decryptedDb.isDirectory());
        if (decryptedFromDbExists) {
            String decryptedFromDbDirectory = decryptedDb.getAbsolutePath();
            model.onDecryptedFromDbDirCreated(decryptedFromDbDirectory);
        }
        Log.d(TAG, "makeDirectories() returned: " + (exists && encryptExists && decryptedExists && decryptedFromDbExists));
        return exists && encryptExists && decryptedExists && decryptedFromDbExists;
    }

    public File checkFileExists(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            return file;
        }
        return null;
    }

    public boolean checkFileExistsAndIsFile(File file) {
        return file.exists() && file.isFile();
    }

    public void encryptToFileSystem(String originalPath, String originalName, String encryptedDirectory, EncryptionModel model) {
        try {
            FileInputStream fileInputStream = new FileInputStream(originalPath);
            Log.d(TAG, "encryptFile: file input stream available: " + fileInputStream.available());
            String encryptedFilePath = encryptedDirectory + "/" + originalName;
            File encryptedFile = new File(encryptedFilePath);
            encryptedFile.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(encryptedFile);

            Cipher cipher = initCipher();
            CipherOutputStream cipherOutputStream = new CipherOutputStream(fileOutputStream, cipher);

            int i;
            byte[] bytes = new byte[1024];
            while ((i = fileInputStream.read(bytes)) != -1) {
                cipherOutputStream.write(bytes, 0, i);
                //Log.d(TAG, "encryptFile: " + i);
            }
            cipherOutputStream.flush();
            cipherOutputStream.close();
            fileInputStream.close();

            model.onEncryptedFileCreated(encryptedFile);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IOException | InvalidAlgorithmParameterException e) {
            Log.e(TAG, "encryptFile: ", e);
            model.onError(e);
        }

    }

    public void decryptToFileSystem(String encryptedDir, String decryptedDir, String originalName, EncryptionModel model) {

        try {
            String encrptPath = encryptedDir + "/" + originalName;
            String decrptPath = decryptedDir + "/" + originalName;
            File encryptedFile = new File(encrptPath);
            File decryptedFile = new File(decrptPath);
            decryptedFile.createNewFile();

            FileInputStream fileInputStream = new FileInputStream(encryptedFile);
            FileOutputStream fileOutputStream = new FileOutputStream(decryptedFile);

            Cipher cipher = initCipher();
            CipherInputStream cipherInputStream = new CipherInputStream(fileInputStream, cipher);

            int i;
            byte[] bytes = new byte[1024];
            while ((i = cipherInputStream.read(bytes)) != -1) {
                fileOutputStream.write(bytes, 0, i);
                //Log.d(TAG, "encryptFile: " + i);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            cipherInputStream.close();
            model.onFileDecrypted(decryptedFile);
        } catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            Log.e(TAG, "decryptToFileSystem: ", e);
            model.onError(e);
        }
    }

    public Cipher initCipher() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException {
        SecretKeySpec secretKeySpec = new SecretKeySpec("123456789QWERTYU".getBytes(), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec("0123459876543210".getBytes());
        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);
        return cipher;
    }
}
