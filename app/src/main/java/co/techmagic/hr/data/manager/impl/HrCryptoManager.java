package co.techmagic.hr.data.manager.impl;

import android.util.Base64;
import android.util.Log;

import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import co.techmagic.hr.presentation.util.JniUtils;

/**
 * Created by techmagic on 3/27/17.
 */

public class HrCryptoManager {


    public String encrypt(String data) {
        byte[] input = data.getBytes();
        String key = JniUtils.getInstance().getKey();
        String iv = JniUtils.getInstance().getInitializationVector();
        byte[] keyBytes = key.getBytes();
        byte[] ivBytes = iv.getBytes();
        byte[] encryptedBytes = processData(Cipher.ENCRYPT_MODE, input, keyBytes, ivBytes);
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
    }


    public String decrypt(String data, int tokenLength) {
        byte[] input = Base64.decode(data, Base64.DEFAULT);
        String key = JniUtils.getInstance().getKey();
        String iv = JniUtils.getInstance().getInitializationVector();
        byte[] keyBytes = key.getBytes();
        byte[] ivBytes = iv.getBytes();
        byte[] decryptedBytes = processData(Cipher.DECRYPT_MODE, input, keyBytes, ivBytes);
        return decryptedBytes == null ? null : new String(Arrays.copyOf(decryptedBytes, tokenLength));
    }


    private byte[] processData(int mode, byte[] input, byte[] keyBytes, byte[] ivBytes) {
        byte[] processed = null;
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivspec = new IvParameterSpec(ivBytes);
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(mode, key, ivspec);
            processed = new byte[cipher.getOutputSize(input.length)];
            int processedLength = cipher.update(input, 0, input.length, processed, 0);
            cipher.doFinal(processed, processedLength);
        } catch (Exception e) {
            Log.e("processData", Log.getStackTraceString(e));
        }
        return processed;
    }
}

