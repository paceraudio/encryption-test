package com.pacerapps;

import com.pacerapps.background.DecryptFileRunnable;
import com.pacerapps.background.DecryptFromDbRunnable;
import com.pacerapps.background.EncryptFileRunnable;
import com.pacerapps.background.EncryptToDbRunnable;
import com.pacerapps.repository.EncryptionRepositoryImpl;
import com.pacerapps.repository.database.CouchBaseLiteDbHelper;
import com.pacerapps.repository.filesystem.FileSystemUtil;
import com.pacerapps.testencryption.EncryptionActivity;
import com.pacerapps.testencryption.EncryptionActivityPresenter;
import com.pacerapps.testencryption.EncryptionModelImpl;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by jeffwconaway on 4/20/17.
 */

@Singleton
@Component (modules = {AppModule.class})
public interface AppComponent {

    void inject(EncryptionActivity activity);

    void inject(DecryptFileRunnable runnable);

    void inject(DecryptFromDbRunnable runnable);

    void inject(EncryptFileRunnable runnable);

    void inject(EncryptToDbRunnable runnable);

    void inject(EncryptionRepositoryImpl repository);

    void inject(CouchBaseLiteDbHelper couchBaseLiteDbHelper);

    void inject(EncryptionModelImpl encryptionModelImpl);

    void inject(EncryptionActivityPresenter encryptionActivityPresenter);

    void inject(FileSystemUtil fileSystemUtil);
}
