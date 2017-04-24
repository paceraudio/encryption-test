package com.pacerapps.repository.database;

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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by jeffwconaway on 4/21/17.
 */

public class CouchBaseLiteDbHelper {

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

    public CouchBaseLiteDbHelper(Context context) {
        this.context = context;
        try {
            initDatabase();
        } catch (IOException | CouchbaseLiteException e) {
            Log.e(TAG, "CouchBaseLiteDbHelper: ", e);
        }
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

    public void initDatabase() throws IOException, CouchbaseLiteException {
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

    public void createSongDocumentInDb(String md5, String path, String fileName) throws IOException, CouchbaseLiteException {
        Database db = getSongInfoDataBaseInstance();
        Document songDoc = db.getDocument(md5);
        Map<String, Object> props = songDoc.getProperties();
        HashMap<String, Object> properties = new HashMap<>(3);
        if (props != null) {
            properties.putAll(props);
        }

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


    public void retrieveSongAttachmentFromDb(String md5, String pathtoWriteTo, String originalFileName) throws IOException, CouchbaseLiteException {
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
                    //Log.d(TAG, "encryptFile: " + i);
                }
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }
    }
}
