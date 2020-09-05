/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 */

package com.android.cache;

public class StorageContext {

    private static Serializer sSerializer;

    public static void setSerializer(Serializer serializer) {
        sSerializer = serializer;
    }

    static Serializer provideSerializer() {
        if (sSerializer != null) {
            return sSerializer;
        }
        return Holder.JSON_SERIALIZER;
    }

    private static class Holder {
        private static final Serializer JSON_SERIALIZER = new JsonSerializer();
    }

}
