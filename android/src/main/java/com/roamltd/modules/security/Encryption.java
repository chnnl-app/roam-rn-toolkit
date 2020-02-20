package com.roamltd.modules.security;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by brent on 26/09/17.
 */

public final class Encryption {

    public interface Accessor {
        @Nullable
        String encryptText(@Nullable String text, @NonNull String keyAlias) throws Exception;

        @Nullable
        String decryptText(@Nullable String encryptedText, @NonNull String keyAlias) throws Exception;
    }


    public static Accessor getEncryption(@NonNull Context applicationContext) {
        return getEncryption(applicationContext, Build.VERSION.SDK_INT);
    }

    public static Accessor getEncryption(@NonNull Context applicationContext, int requestedAPILevel){
        return getEncryption(applicationContext, applicationContext.getPackageName().replace('.', '_'), requestedAPILevel);
    }

    public static Accessor getEncryption(@NonNull Context applicationContext, @NonNull String certificateAuthority) {
        return getEncryption(applicationContext, certificateAuthority, Build.VERSION.SDK_INT);
    }

    public static Accessor getEncryption(@NonNull Context applicationContext, @NonNull String certificateAuthority, int requestedAPILevel) {
        try {
            if(Build.VERSION.SDK_INT >= 23 && requestedAPILevel >= 23) {
                return new AESEncryption();
            } else if(Build.VERSION.SDK_INT >= 18 && requestedAPILevel >= 18) {
                return new RSAEncryption(applicationContext, certificateAuthority);
            } else if(requestedAPILevel != 0){
                return new DESEncryption(); //DES (Not 'actual' encryption)
            }else {
                return new EncryptionStub();
            }

        } catch (Exception e) {
            Log.e("ENCRYPTION","Error setting up token controller");
            e.printStackTrace();
        }

        return null;
    }
}
