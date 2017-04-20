package com.pacerapps;

import android.app.Application;
import android.content.Context;

/**
 * Created by jeffwconaway on 7/22/16.
 */

public class EncApp extends Application {

    private static Context context;
    private static final String TAG = "jwc";
    private static EncApp encApp;
    private AppComponent appComponent;


    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        encApp = this;
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(getApplicationContext())).build();
        //initSongDb();
        //initRepository();
    }

    public static EncApp getInstance() {
        return encApp;
    }

    public static Context getContext() {
        return context;
    }

    /*private void initRepository() {
        repository.initRepository();
    }*/
    /*private void initSongDb() {
        try {
            Database db = EncryptionRepositoryImpl.getSongInfoDataBaseInstance();
            Log.d(TAG, "initSongDb: doc count: " + db.getDocumentCount());
        } catch (IOException e) {
            Log.e(TAG, "initSongDb: problem getting db: ", e);
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "initSongDb: couchbase ex: ", e);
        }
    }*/

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
