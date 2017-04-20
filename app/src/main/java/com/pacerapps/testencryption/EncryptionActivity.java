package com.pacerapps.testencryption;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pacerapps.EncApp;

public class EncryptionActivity extends AppCompatActivity implements View.OnClickListener, EncryptionActivityView {

    Button encryptButton;
    Button decryptButton;
    Button encryptToDbButton;
    Button decryptFromDbButton;
    Button playOriginalButton;
    Button playEncryptedButton;
    Button playDeryptedButton;
    Button playDecryptedFromDbButton;
    Button stopButton;
    TextView statusTextView;

    EncryptionActivityPresenter presenter;

    public static final String TAG = "jwc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encryption);
        encryptButton = (Button) findViewById(R.id.button_encrypt);
        decryptButton = (Button) findViewById(R.id.button_decrypt);
        encryptToDbButton = (Button) findViewById(R.id.button_add_to_db);
        decryptFromDbButton = (Button) findViewById(R.id.button_decrypt_from_db);
        playOriginalButton = (Button) findViewById(R.id.button_play_source);
        playDeryptedButton = (Button) findViewById(R.id.button_play_decrypt);
        playEncryptedButton = (Button) findViewById(R.id.button_play_encrypted);
        playDecryptedFromDbButton = (Button) findViewById(R.id.button_play_decrypted_from_db);
        stopButton = (Button) findViewById(R.id.button_stop);
        statusTextView = (TextView) findViewById(R.id.textview_status);
        presenter = new EncryptionActivityPresenter(getApplicationContext()
                , this);

        EncApp.getInstance().getAppComponent().inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.makeDirectory();
        encryptButton.setOnClickListener(this);
        decryptButton.setOnClickListener(this);
        encryptToDbButton.setOnClickListener(this);
        decryptFromDbButton.setOnClickListener(this);
        playOriginalButton.setOnClickListener(this);
        playDeryptedButton.setOnClickListener(this);
        playEncryptedButton.setOnClickListener(this);
        playDecryptedFromDbButton.setOnClickListener(this);
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
        } else if (v == encryptToDbButton) {
            presenter.encryptToDb();
        } else if (v == decryptFromDbButton) {
            presenter.decryptFromDb();
        } else if (v == playDecryptedFromDbButton) {
            presenter.playDecryptedFromDb();
        }
    }

    private void makeTheToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM, 0, 0);
                toast.show();
            }
        });

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
        makeTheToast("File Encrypted");
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
        makeTheToast("File Decrypted");
    }

    @Override
    public void onSongEncryptedToDb() {
        makeTheToast("Song Encrypted To DB");
    }

    @Override
    public void onSongDecryptedFromDb() {
        makeTheToast("Song Decrypted From DB");
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
    public void onPlayDecryptedFromDb() {
        Log.d(TAG, "onPlayDecryptedFromDb: running!");
    }

    @Override
    public void onMusicPlaying(String songPath) {
        String nowPlaying = "PLAYING " + songPath;
        statusTextView.setText(nowPlaying);
    }

    @Override
    public void onMusicStopped() {
        statusTextView.setText("");
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
