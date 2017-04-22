package com.pacerapps.repository.filesystem;

import android.util.Base64;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;

import static com.pacerapps.util.Constants.TAG;

/**
 * Created by jeffwconaway on 4/21/17.
 */

public class Md5Util {

    public boolean compareMd5s(String originalMd5, String decryptedSongMd5) {
        Log.d(TAG, "compareMd5s() called with: " + "originalMd5 = [" + originalMd5 + "], decryptedSongMd5 = [" + decryptedSongMd5 + "]");
        boolean equals = originalMd5.equals(decryptedSongMd5);
        Log.d(TAG, "compareMd5s() returned: " + equals);
        return equals;
    }

    public String calcMd5(File file) {
        BufferedInputStream bis = null;
        DigestInputStream dis = null;
        byte[] fileBuf = new byte[2048];

        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            MessageDigest md = MessageDigest.getInstance("MD5");
            dis = new DigestInputStream(bis, md);

            while (dis.read(fileBuf) == fileBuf.length) ;
            return Base64.encodeToString(md.digest(), Base64.NO_WRAP);

        } catch (Exception e) {
//            Log.d(TAG, "calcMd5: exception: " + e.getMessage());
        } finally {
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

}
