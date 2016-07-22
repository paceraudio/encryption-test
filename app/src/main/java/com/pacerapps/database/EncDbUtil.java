package com.pacerapps.database;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.DatabaseOptions;
import com.couchbase.lite.Manager;
import com.couchbase.lite.android.AndroidContext;
import com.pacerapps.testencryption.EncApp;

import java.io.IOException;

/**
 * Created by jeffwconaway on 7/22/16.
 */
public class EncDbUtil {

    private static Manager manager;
    private static Database songDatabase;
    private static DatabaseOptions databaseOptions;

    public static final String SONG_DATABASE = "song-database";

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
}
