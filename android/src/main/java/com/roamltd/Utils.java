package com.roamltd;

import android.util.Log;

public class Utils {

    public static String A_GENRERIC_UNKNOWN_ERROR = "ER_43927";

    public static String parseString(Object object) {
        if(object == null) return null;
        if(object instanceof String){
            byte[] bytes = ((String) object).getBytes();
            StringBuilder builder = new StringBuilder()
                    .append("Consider using ")
                    .append("new byte[]{");

            for(int i = 0; i < bytes.length; i++) {
                if(i != 0){
                    builder.append(',');
                }
                builder.append(bytes[i]);
            }
            builder.append('}');
            Log.e("UTIL", builder.toString());
            return (String) object;
        }

        if(object instanceof byte[]) {
            return new String((byte[]) object);
        }

        return object.toString();
    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}


