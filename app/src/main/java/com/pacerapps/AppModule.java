package com.pacerapps;

import android.content.Context;

import com.pacerapps.repository.EncryptionRepository;
import com.pacerapps.repository.EncryptionRepositoryImpl;
import com.pacerapps.repository.database.CouchBaseLiteDbHelper;
import com.pacerapps.testencryption.EncryptionActivityPresenter;
import com.pacerapps.testencryption.EncryptionModelImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jeffwconaway on 4/20/17.
 */

@Module
public class AppModule {

    private Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Singleton @Provides
    public Context provideContext() {
        return context;
    }

    @Provides
    public EncryptionActivityPresenter providePresenter(Context context, EncryptionModelImpl model) {
        return new EncryptionActivityPresenter(context, model);
    }

    @Singleton @Provides
    public EncryptionModelImpl provideModel(EncryptionRepository repository) {
        return new EncryptionModelImpl(repository);
    }

    @Singleton @Provides
    public EncryptionRepository provideEncryptionRepository(Context context, CouchBaseLiteDbHelper couchBaseLiteDbHelper) {
        return new EncryptionRepositoryImpl(context, couchBaseLiteDbHelper);
    }

    @Singleton @Provides
    public CouchBaseLiteDbHelper provideCouchBaseLiteDb(Context context) {
        return new CouchBaseLiteDbHelper(context);
    }
}
