package com.pacerapps;

import android.content.Context;

import com.pacerapps.repository.EncryptionRepository;
import com.pacerapps.repository.EncryptionRepositoryImpl;

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

    @Singleton @Provides
    public EncryptionRepository provideEncryptionRepository() {
        return new EncryptionRepositoryImpl();
    }
}
