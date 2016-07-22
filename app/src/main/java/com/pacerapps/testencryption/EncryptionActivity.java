package com.pacerapps.testencryption;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class EncryptionActivity extends AppCompatActivity implements View.OnClickListener, EncryptionActivityView {

    Button encryptButton;
    Button decryptButton;
    Button playOriginalButton;
    Button playEncryptedButton;
    Button playDeryptedButton;
    Button stopButton;

    EncryptionActivityPresenter presenter;

    public static final String TAG = "jwc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encryption);
        encryptButton = (Button) findViewById(R.id.button_encrypt);
        decryptButton = (Button) findViewById(R.id.button_decrypt);
        playOriginalButton = (Button) findViewById(R.id.button_play_source);
        playDeryptedButton = (Button) findViewById(R.id.button_play_decrypt);
        playEncryptedButton = (Button) findViewById(R.id.button_play_encrypted);
        stopButton = (Button) findViewById(R.id.button_stop);
        presenter = new EncryptionActivityPresenter(getApplicationContext()
                , this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.makeDirectory();
        encryptButton.setOnClickListener(this);
        decryptButton.setOnClickListener(this);
        playOriginalButton.setOnClickListener(this);
        playDeryptedButton.setOnClickListener(this);
        playEncryptedButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: RUNNING!!!");

        if (v == encryptButton) {
            presenter.encryptFile();
        } else if (v == decryptButton) {
            presenter.decryptFile();
        } else if (v == playOriginalButton) {
            presenter.playOriginal();
        } else if (v == playDeryptedButton) {
            presenter.playDecrypted();
        } else if (v == stopButton) {
            presenter.stop();
        } else if (v == playEncryptedButton) {
            presenter.playEncrypted();
        }
    }

    @Override
    public void onEncrypt() {
        Log.d(TAG, "onEncrypt: running!");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                makeEncryptionToast();
            }
        });

    }

    private void makeEncryptionToast() {
        Toast toast = Toast.makeText(this, "File Encrypted", Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void onDecrypt() {
        Log.d(TAG, "onDecrypt: running!");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                makeDecryptionToast();
            }
        });

    }

    private void makeDecryptionToast() {
        Toast toast = Toast.makeText(this, "File Decrypted", Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void onPlayOriginal() {
        Log.d(TAG, "onPlayOriginal: running!");
    }

    @Override
    public void onPlayDecrypted() {
        Log.d(TAG, "onPlayDecrypted: running!");
    }

    @Override
    public void onPlayEncrypted() {
        Log.d(TAG, "onPlayEncrypted: running!");
    }

    @Override
    public void onStopClicked() {
        Log.d(TAG, "onStopClicked: running!");
    }

    @Override
    public void onDirCreated(boolean exists) {
        /*Toast toast = Toast.makeText(this, "Directory created!!! " + presenter.getSongDirectory(), Toast.LENGTH_LONG);
        if (!exists) {
            toast.setText("Creating directory failed!");
        }
        toast.show();
        return exists;*/

        Log.d(TAG, "onDirCreated: " + presenter.getSongDirectory());
        Log.d(TAG, "onDirCreated: " + presenter.getEncryptDirectory());
        Log.d(TAG, "onDirCreated: " + presenter.getDecryptDirectory());

    }
}
