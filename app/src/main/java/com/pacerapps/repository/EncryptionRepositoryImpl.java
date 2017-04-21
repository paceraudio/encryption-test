package com.pacerapps.repository;

import android.content.Context;
import android.util.Log;

import com.couchbase.lite.Attachment;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.DatabaseOptions;
import com.couchbase.lite.Document;
import com.couchbase.lite.Manager;
import com.couchbase.lite.SavedRevision;
import com.couchbase.lite.UnsavedRevision;
import com.couchbase.lite.android.AndroidContext;
import com.pacerapps.EncApp;
import com.pacerapps.background.FileEncryptedListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;

/**
 * Created by jeffwconaway on 7/22/16.
 */

public class EncryptionRepositoryImpl implements EncryptionRepository {

    private static Manager manager;
    private static Database songDatabase;
    private static DatabaseOptions databaseOptions;

    private static final String SONG_PATH_KEY = "songPathKey";
    private static final String SONG_FILE_NAME_KEY = "songFileName";

    private static final String SONG_DATABASE = "song-database";
    private static final String MP3_TYPE = "mp3Type";

    private static final String TAG = "jwc";

    @Inject
    Context context;

    public EncryptionRepositoryImpl() {
        EncApp.getInstance().getAppComponent().inject(this);
        initRepository();

    }

    @Override
    public void initRepository() {
        try {
            initDatabase();
        } catch (IOException | CouchbaseLiteException e) {
            Log.e(TAG, "initRepository: ", e);
        }
    }

    @Override
    public void retrieveSongAttachment(String md5, String decryptedFromDbPath, String fileName) {
        try {
            retrieveSongAttachmentFromDb(md5, decryptedFromDbPath, fileName);
        } catch (IOException | CouchbaseLiteException e) {
            Log.e(TAG, "retrieveSongAttachment: ", e);
        }
    }

    @Override
    public void createSongDocument(String md5, String path, String fileName) {
        try {
            createSongDocumentInDb(md5, path, fileName);
        } catch (IOException | CouchbaseLiteException e) {
            Log.e(TAG, "createSongDocument: ", e);
        }
    }

    @Override
    public void decryptFile(String encryptedDir, String decryptedDir, String originalName, FileEncryptedListener listener) {
        try {
            decrypt(encryptedDir, decryptedDir, originalName, listener);
        } catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            Log.e(TAG, "decryptFile: ", e);
        }
    }

    @Override
    public void encryptFile(String originalPath, String originalName, String encryptedDirectory, FileEncryptedListener listener) {
        encrypt(originalPath, originalName, encryptedDirectory, listener);
    }

    private Manager getSongsDbManagerInstance() throws IOException, CouchbaseLiteException {
        if (manager == null) {
            manager = new Manager(new AndroidContext(context), Manager.DEFAULT_OPTIONS);
            String key = "FAKE_PASSWORD";
            databaseOptions = new DatabaseOptions();
            databaseOptions.setCreate(true);
            databaseOptions.setEncryptionKey(key);
        }
        return manager;
    }

    private void initDatabase() throws IOException, CouchbaseLiteException {
        manager = getSongsDbManagerInstance();
        if (songDatabase == null && manager != null) {
            songDatabase = manager.openDatabase(SONG_DATABASE, databaseOptions);
        }
    }

    private Database getSongInfoDataBaseInstance() throws IOException, CouchbaseLiteException {
        manager = getSongsDbManagerInstance();
        if (songDatabase == null && manager != null) {
            songDatabase = manager.openDatabase(SONG_DATABASE, databaseOptions);
        }
        return songDatabase;
    }

    private void createSongDocumentInDb(String md5, String path, String fileName) throws IOException, CouchbaseLiteException {
        Database db = getSongInfoDataBaseInstance();
        Document songDoc = db.getDocument(md5);
        Map<String, Object> props = songDoc.getProperties();
        HashMap<String, Object> properties = new HashMap<>(3);
        properties.putAll(props);
        properties.put(SONG_PATH_KEY, path);
        properties.put(SONG_FILE_NAME_KEY, fileName);
        songDoc.putProperties(properties);
        addAttachmentToSongDocInDb(md5, path);
    }

    private void addAttachmentToSongDocInDb(String md5, String originalPath) throws IOException, CouchbaseLiteException {
        Database db = getSongInfoDataBaseInstance();
        Document songDoc = db.getExistingDocument(md5);
        InputStream inputStream = null;
        File originalFile = new File(originalPath);
        if (songDoc != null && originalFile.exists()) {
            inputStream = new FileInputStream(originalFile);
            UnsavedRevision unsavedRevision = songDoc.getCurrentRevision().createRevision();
            String attachmentName = md5 + originalFile.getName();
            unsavedRevision.setAttachment(attachmentName, MP3_TYPE, inputStream);
            unsavedRevision.save();
        }
    }


    private void retrieveSongAttachmentFromDb(String md5, String pathtoWriteTo, String originalFileName) throws IOException, CouchbaseLiteException {
        Database db = getSongInfoDataBaseInstance();
        Document songDoc = db.getExistingDocument(md5);
        if (songDoc != null) {
            SavedRevision savedRevision = songDoc.getCurrentRevision();
            String attachmentName = md5 + originalFileName;
            Attachment attachment = savedRevision.getAttachment(attachmentName);
            if (attachment != null) {
                InputStream inputStream = attachment.getContent();
                byte[] bytes = new byte[1024];
                File file = new File(pathtoWriteTo);
                file.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                int i;
                while ((i = inputStream.read(bytes)) != -1) {
                    fileOutputStream.write(bytes, 0, i);
                    Log.d(TAG, "encryptFile: " + i);
                }
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }
    }

    private void decrypt(String encryptedDir, String decryptedDir, String originalName, FileEncryptedListener listener) throws IOException, NoSuchAlgorithmException,
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

    private void encrypt(String originalPath, String originalName, String encryptedDirectory, FileEncryptedListener listener) {
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
