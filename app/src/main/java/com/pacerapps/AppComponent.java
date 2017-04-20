package com.pacerapps;

import com.pacerapps.background.DecryptFileRunnable;
import com.pacerapps.background.DecryptFromDbRunnable;
import com.pacerapps.background.EncryptFileRunnable;
import com.pacerapps.background.EncryptToDbRunnable;
import com.pacerapps.repository.EncryptionRepositoryImpl;
import com.pacerapps.testencryption.EncryptionActivity;

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


}
