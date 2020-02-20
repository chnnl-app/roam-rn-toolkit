package com.roamltd.modules.security;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by brent on 15/11/17.
 */

public class EncryptionStub implements Encryption.Accessor {

    @Nullable
    @Override
    public String encryptText(@Nullable String text, @NonNull String keyAlias) throws Exception {
        return text;
    }

    @Nullable
    @Override
    public String decryptText(@Nullable String encryptedText, @NonNull String keyAlias) throws Exception {
        return encryptedText;
    }
}
