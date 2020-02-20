package com.roamltd.modules.security;

import android.content.Context;
import android.content.SharedPreferences;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.roamltd.Utils;

import java.security.SecureRandom;

public class EncryptionModule extends ReactContextBaseJavaModule {

    //Generic Name to made de-compilation harder
    private final String ALIAS_ACCESS = "Context";

    private final Encryption.Accessor encryptionAccessor;
    private final SharedPreferences sharedPreferences;

    public EncryptionModule(ReactApplicationContext reactContext) {
        super(reactContext);

        String identity = String.valueOf(reactContext.getPackageName().hashCode());
        encryptionAccessor = Encryption.getEncryption(reactContext.getApplicationContext());
        sharedPreferences = reactContext.getSharedPreferences(identity, Context.MODE_PRIVATE);
    }

    @Override
    public String getName() {
        return "RNKeychain";
    }

    @ReactMethod
    public void getValueForKey(String key, final Promise promise) {
        String value = sharedPreferences.getString(key, null);
        if(value == null) {
            promise.resolve(null);
            return;
        }
        try {
            promise.resolve(encryptionAccessor.decryptText(value, ALIAS_ACCESS));
        } catch (Exception e) {
            e.printStackTrace();
            promise.reject("E_DECRYPT", e.getMessage());
        }
    }

    @ReactMethod
    public void setValueForKey(String key, String value) throws Exception {
        if(value == null) {
            deleteValueForKey(key);
            return;
        }

        String encrypted = encryptionAccessor.encryptText(value, ALIAS_ACCESS);
        sharedPreferences.edit().putString(key, encrypted).apply();
    }

    @ReactMethod
    public void deleteValueForKey(String key) {
        sharedPreferences.edit().remove(key).apply();
    }

    @ReactMethod
    public void generateKeyWithLength(int length, final Promise promise) {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[length];
        random.nextBytes(bytes);
        promise.resolve(Utils.bytesToHex(bytes));
    }

}
