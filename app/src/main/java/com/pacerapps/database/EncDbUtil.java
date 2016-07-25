package com.pacerapps.database;

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
import com.couchbase.lite.support.Base64;
import com.pacerapps.testencryption.EncApp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jeffwconaway on 7/22/16.
 */
public class EncDbUtil {

    private static Manager manager;
    private static Database songDatabase;
    private static DatabaseOptions databaseOptions;

    private static final String SONG_PATH_KEY = "songPathKey";
    private static final String SONG_FILE_NAME_KEY = "songFileName";

    public static final String SONG_DATABASE = "song-database";
    public static final String MP3_TYPE = "mp3Type";

    private static final String TAG = "jwc";

    public static Manager getSongsDbManagerInstance() throws IOException, CouchbaseLiteException {
        if (manager == null) {
            manager = new Manager(new AndroidContext(EncApp.getContext()), Manager.DEFAULT_OPTIONS);
            String key = "FAKE_PASSWORD";
            //String key = "WRONG_FAKE_PASSWORD";
            databaseOptions = new DatabaseOptions();
            databaseOptions.setCreate(true);
            databaseOptions.setEncryptionKey(key);
            //Database database = manager.openDatabase("db", databaseOptions);
        }
        return manager;
    }

    public static Database getSongInfoDataBaseInstance() throws IOException, CouchbaseLiteException {
        manager = getSongsDbManagerInstance();
        if (songDatabase == null && manager != null) {
            //songDatabase = manager.getDatabase(SONG_DATABASE);
            songDatabase = manager.openDatabase(SONG_DATABASE, databaseOptions);
        }
        return songDatabase;
    }

    public static void createSongDocument(String md5, String path, String fileName) throws IOException, CouchbaseLiteException{
        Database db = getSongInfoDataBaseInstance();
        Document songDoc = db.getDocument(md5);
        Map<String, Object> props = songDoc.getProperties();
        HashMap<String, Object> properties = new HashMap<>(3);
        properties.putAll(props);
        properties.put(SONG_PATH_KEY, path);
        properties.put(SONG_FILE_NAME_KEY, fileName);
        songDoc.putProperties(properties);
        addAttachmentToSongDoc(md5, path);
    }

    public static void addAttachmentToSongDoc(String md5, String originalPath) throws IOException, CouchbaseLiteException{
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

    public static void retrieveSongAttachment(String md5, String pathtoWriteTo, String originalFileName) throws IOException, CouchbaseLiteException{
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

    /*public static String writeSongAttachmentToFile(PlaylistItem songToExtract) throws CouchbaseLiteException, IOException{
        String songPath = songToExtract.getSongPath();
        //String exSongPath = "";
        String fileName = songToExtract.getSongFileName();
        String extractedSongPath = ActFileUtil.getMp3StorageDirectory() + "/" + fileName;

        *//*String[] splitStr = songPath.split(Pattern.quote("mp3"));
        StringBuilder sb = new StringBuilder(splitStr[0]);
        sb.append("extracted.mp3");
        //sb.append(splitStr[1]);
        exSongPath = sb.toString();*//*

        Database db = getSongInfoDataBaseInstance();
        Document blobDoc = db.getExistingDocument(songPath);
        if (blobDoc != null) {
            SavedRevision revision = blobDoc.getCurrentRevision();
            String attName = songToExtract.getSongMd5() + songToExtract.getSongFileName();
            Attachment attachment = revision.getAttachment(attName);
            if (attachment != null) {
                InputStream stream = attachment.getContent();
                byte[] buffer = new byte[stream.available()];
                File songFile = new File(extractedSongPath);
                songFile.createNewFile();
                FileOutputStream fos = new FileOutputStream(songFile);
                fos.write(buffer);
            } else {
                Log.d(TAG, "writeSongAttachmentToFile: ATTACHMENT IS NULL!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            }
        }
        Log.d(TAG, "writeSongAttachmentToFile() returned: " + extractedSongPath);
        ArrayList<File> songFiles = ActFileUtil.obtainAllSongsFilesOnDevice();
        for (File file : songFiles) {
            Log.d(TAG, "song file on device: " + file.getName());
        }
        return extractedSongPath;
    }

    public static void createSongAttachment(File songFile, String md5, Document blobDoc) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(songFile);
            UnsavedRevision revision = blobDoc.getCurrentRevision().createRevision();
            String attachmentName = md5 + songFile.getName();

            revision.setAttachment(attachmentName, MP3_TYPE, inputStream);
            revision.save();

        } catch (FileNotFoundException | CouchbaseLiteException e) {
            Log.e(TAG, "createSongAttachment: ", e);
        }
    }*/
}
