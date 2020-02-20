package com.roamltd.modules.security;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Base64;

import com.roamltd.Utils;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyStore;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.GCMParameterSpec;

/**
 * Created by brent on 26/09/17.
 */

@RequiresApi(23)
public class AESEncryption implements Encryption.Accessor {

    private final byte[] name = new byte[]{65,69,83};
    private final byte[] transform = new byte[]{65,69,83,47,71,67,77,47,78,111,80,97,100,100,105,110,103};
    private final byte[] keyStoreName = new byte[]{65,110,100,114,111,105,100,75,101,121,83,116,111,114,101};
    private final int IV_LENGTH = 12;

    private KeyStore keyStore;

    public AESEncryption() throws Exception{
        keyStore = KeyStore.getInstance(Utils.parseString(keyStoreName));
        keyStore.load(null);
    }

    @Nullable
    @Override
    public String encryptText(@Nullable String text, @NonNull String keyAlias) throws Exception {
        if(text == null || text.length() == 0) return null;

        final Cipher cipher = Cipher.getInstance(Utils.parseString(transform));
        cipher.init(Cipher.ENCRYPT_MODE, getOrCreateSecretKey(keyAlias));

        byte[] iv = cipher.getIV();
        byte[] encryption = cipher.doFinal(text.getBytes("UTF-8"));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(encryption);
        outputStream.write(iv);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

    @Nullable
    @Override
    public String decryptText(@Nullable String encryptedText, @NonNull String keyAlias) throws Exception {
        if(encryptedText == null || encryptedText.length() == 0) return null;

        byte[] encryptedData = Base64.decode(encryptedText, Base64.DEFAULT);
        byte[] data = Arrays.copyOfRange(encryptedData, 0, encryptedData.length - IV_LENGTH);
        byte[] encryptionIv = Arrays.copyOfRange(encryptedData, encryptedData.length - IV_LENGTH, encryptedData.length);

        final Cipher cipher = Cipher.getInstance(Utils.parseString(transform));
        final GCMParameterSpec spec = new GCMParameterSpec(128, encryptionIv);
        cipher.init(Cipher.DECRYPT_MODE, getOrCreateSecretKey(keyAlias), spec);
        return new String(cipher.doFinal(data), "UTF-8");
    }

    /**
     * Creates or retrieves a key within the Android Keystore
     */
    private Key getOrCreateSecretKey(final String alias) throws Exception {
        if(keyStore.containsAlias(alias)) {
            return keyStore.getKey(alias, null);
        } else {
            final KeyGenerator keyGenerator = KeyGenerator
                    .getInstance(Utils.parseString(name), Utils.parseString(keyStoreName));

            keyGenerator.init(new KeyGenParameterSpec.Builder(alias,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build());

            return keyGenerator.generateKey();
        }
    }
}
