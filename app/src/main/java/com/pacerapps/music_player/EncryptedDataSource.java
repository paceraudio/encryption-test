package com.pacerapps.music_player;

import android.net.Uri;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.TransferListener;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;

/**
 * Created by jeffwconaway on 4/22/17.
 */

class EncryptedDataSource implements DataSource {

    private long bytesRemaining;
    private boolean opened;
    private CipherInputStream cipherInputStream;
    private Cipher cipher;
    private TransferListener<? super EncryptedDataSource> transferListener;

    EncryptedDataSource(Cipher cipher, TransferListener<? super DataSource> transferListener) {
        this.cipher = cipher;
        this.transferListener = transferListener;
    }

    @Override
    public long open(DataSpec dataSpec) throws IOException {
        try {
            File file = new File(dataSpec.uri.getPath());

            cipherInputStream = new CipherInputStream(new FileInputStream(file), cipher);

            long skipped = cipherInputStream.skip(dataSpec.position);
            if (skipped < dataSpec.position) {
                throw new EOFException();
            }
            if (dataSpec.length != C.LENGTH_UNSET) {
                bytesRemaining = dataSpec.length;
            } else {
                bytesRemaining = cipherInputStream.available();
                if (bytesRemaining == 0) {
                    bytesRemaining = C.LENGTH_UNSET;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        opened = true;
        if (transferListener != null) {
            transferListener.onTransferStart(this, dataSpec);
        }

        return bytesRemaining;
    }

    @Override
    public int read(byte[] buffer, int offset, int readLength) throws IOException {
        if (bytesRemaining == 0) {
            return -1;
        } else {
            int bytesRead = 0;

            int bytesToRead = bytesRemaining == C.LENGTH_UNSET ? readLength
                    : (int) Math.min(bytesRemaining, readLength);
            try {
                bytesRead = cipherInputStream.read(buffer, offset, bytesToRead);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (bytesRead > 0) {
                if (bytesRemaining != C.LENGTH_UNSET) {
                    bytesRemaining -= bytesRead;
                }
                if (transferListener != null) {
                    transferListener.onBytesTransferred(this, bytesRead);
                }
            }

            return bytesRead;
        }
    }

    @Override
    public Uri getUri() {
        return null;
    }

    @Override
    public void close() throws IOException {

    }
}
