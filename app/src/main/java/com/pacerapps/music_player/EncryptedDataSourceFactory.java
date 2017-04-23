package com.pacerapps.music_player;

import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.TransferListener;

import javax.crypto.Cipher;

/**
 * Created by jeffwconaway on 4/23/17.
 */

public class EncryptedDataSourceFactory implements DataSource.Factory {


    private Cipher cipher;
    private TransferListener<? super DataSource> transferListener;

    public EncryptedDataSourceFactory(Cipher cipher, TransferListener<? super DataSource> listener) {
        this.cipher = cipher;
        this.transferListener = listener;
    }

    @Override
    public EncryptedDataSource createDataSource() {
        return new EncryptedDataSource(cipher, transferListener);
    }


}
