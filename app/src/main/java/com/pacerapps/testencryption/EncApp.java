package com.pacerapps.testencryption;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.pacerapps.database.EncDbUtil;

import java.io.IOException;

/**
 * Created by jeffwconaway on 7/22/16.
 */
public class EncApp extends Application {

    private static Context context;
    private static final String TAG = "jwc";

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        initSongDb();
    }

    public static Context getContext() {
        return context;
    }

    private static void initSongDb() {
        try {
            Database db = EncDbUtil.getSongInfoDataBaseInstance();
            Log.d(TAG, "initSongDb: doc count: " + db.getDocumentCount());
//            ActCouchDbUtil.createSongView(db);
//            ActCouchDbUtil.createSongBlobView();
        } catch (IOException e) {
            Log.e(TAG, "initSongDb: problem getting db: ", e);
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "initSongDb: couchbase ex: ", e);
        }
    }
}
