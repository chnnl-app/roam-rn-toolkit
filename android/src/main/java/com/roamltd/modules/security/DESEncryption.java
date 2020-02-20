package com.roamltd.modules.security;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;

import com.roamltd.Utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * Created by brent on 26/09/17.
 */

public class DESEncryption implements Encryption.Accessor {

    private final byte[] name = new byte[]{68,69,83};
    private final byte[] transform = new byte[]{68,69,83,47,67,84,82,47,78,111,80,97,100,100,105,110,103};

    @Nullable
    @Override
    public String encryptText(@Nullable String text, @NonNull String keyAlias) throws Exception {
        if(text == null || text.length() == 0) return null;

        DESKeySpec keySpec = new DESKeySpec(Utils.A_GENRERIC_UNKNOWN_ERROR.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(Utils.parseString(name));
        SecretKey skey = keyFactory.generateSecret(keySpec);

        byte[] cleartext = text.getBytes("UTF8");

        Cipher cipher = Cipher.getInstance(Utils.parseString(transform));
        byte[] iv = new byte[cipher.getBlockSize()];
        for(int i = 0; i < iv.length; i++) iv[i] = (byte)i;
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, skey, ivParameterSpec);
        return Base64.encodeToString(cipher.doFinal(cleartext), Base64.DEFAULT);
    }

    @Nullable
    @Override
    public String decryptText(@Nullable String encryptedText, @NonNull String keyAlias) throws Exception {
        if(encryptedText == null || encryptedText.length() == 0) return null;

        DESKeySpec keySpec = new DESKeySpec(Utils.A_GENRERIC_UNKNOWN_ERROR.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(Utils.parseString(name));
        SecretKey skey = keyFactory.generateSecret(keySpec);

        byte[] encrypedPwdBytes = Base64.decode(encryptedText, Base64.DEFAULT);

        Cipher cipher = Cipher.getInstance(Utils.parseString(transform));
        byte[] iv = new byte[cipher.getBlockSize()];
        for(int i = 0; i < iv.length; i++) iv[i] = (byte)i;
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.DECRYPT_MODE, skey, ivParameterSpec);
        byte[] plainTextBytes = (cipher.doFinal(encrypedPwdBytes));
        return new String(plainTextBytes);
    }
}
