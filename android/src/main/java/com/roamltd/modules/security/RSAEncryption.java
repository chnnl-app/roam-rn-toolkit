package com.roamltd.modules.security;

import android.content.Context;
import android.security.KeyPairGeneratorSpec;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Base64;

import com.roamltd.Utils;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.crypto.Cipher;
import javax.security.auth.x500.X500Principal;

/**
 * Created by brent on 26/09/17.
 */

@RequiresApi(18)
public class RSAEncryption implements Encryption.Accessor {

    private final byte[] transform = new byte[]{82,83,65,47,69,67,66,47,80,75,67,83,49,80,97,100,100,105,110,103};
    private final byte[] name = new byte[]{82,83,65};
    private final byte[] keyStoreName = new byte[]{65,110,100,114,111,105,100,75,101,121,83,116,111,114,101};
    private String keyAuthority;

    private KeyStore keyStore;
    private Context applicationContext;

    public RSAEncryption(@NonNull Context context, String certificateAuthority) throws Exception{
        this.applicationContext = context.getApplicationContext();

        keyAuthority = String.format(Locale.US, "CN=%s, O=%s", certificateAuthority, certificateAuthority);
        keyStore = KeyStore.getInstance(Utils.parseString(keyStoreName));
        keyStore.load(null);
    }

    @Nullable
    @Override
    public String encryptText(@Nullable String text, @NonNull String keyAlias) throws Exception {
        if(text == null || text.length() == 0) return null;

        final Cipher cipher = Cipher.getInstance(Utils.parseString(transform));
        cipher.init(Cipher.ENCRYPT_MODE, getOrCreatePublicKeyForEncryption(keyAlias));
        return Base64.encodeToString(cipher.doFinal(text.getBytes("UTF-8")), Base64.DEFAULT);
    }

    @Nullable
    @Override
    public String decryptText(@Nullable String encryptedText, @NonNull String keyAlias) throws Exception {
        if(encryptedText == null || encryptedText.length() == 0) return null;

        final Cipher cipher = Cipher.getInstance(Utils.parseString(transform));
        cipher.init(Cipher.DECRYPT_MODE, getPrivateKeyForDecryption(keyAlias));

        return new String(cipher.doFinal(Base64.decode(encryptedText, Base64.DEFAULT)), "UTF-8");
    }


    /* Helpers */


    /**
     * Get RSA private key for decryption
     */
    private Key getPrivateKeyForDecryption(String alias) throws Exception{
        if(keyStore.containsAlias(alias)) {
            KeyStore.PrivateKeyEntry privateKeyEntry
                    = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, null);
            return privateKeyEntry.getPrivateKey();
        } else {
            return null;
        }
    }

    /**
     * Get or create (and return) a RSA public key for encryption
     */

    private Key getOrCreatePublicKeyForEncryption(String alias) throws Exception {
        if(keyStore.containsAlias(alias)) {
            KeyStore.PrivateKeyEntry privateKeyEntry
                    = (KeyStore.PrivateKeyEntry)keyStore.getEntry(alias, null);
            return privateKeyEntry.getCertificate().getPublicKey();
        } else {
            KeyPair keyPair =  createKeyForJellyBeanWithAlias(alias);
            return keyPair == null ? null : keyPair.getPublic();
        }
    }

    /**
     * Create key for string alias
     * Support API 18 + using RSA
     * @param alias alias for key
     * @return the key, or null if failed to create
     */
    private KeyPair createKeyForJellyBeanWithAlias(@NonNull String alias) throws Exception{
        //Valid for 10 years, but doesn't need to be accurate.
        Date start = new Date();
        Date end = new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(3650));

        Random rand = new Random();
        BigInteger random = new BigInteger(4, rand); //MAX (2^4-1)

        KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(applicationContext)
                .setAlias(alias)
                .setSubject(new X500Principal(keyAuthority))
                .setSerialNumber(random)
                .setStartDate(start)
                .setEndDate(end)
                .build();

        KeyPairGenerator kpg = KeyPairGenerator.getInstance(Utils.parseString(name), Utils.parseString(keyStoreName));
        kpg.initialize(spec);
        return kpg.generateKeyPair();
    }
}
