/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

package com.android.cache;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import timber.log.Timber;

public class JsonSerializer implements Serializer {

    private final Gson GSON = new GsonBuilder()
            .excludeFieldsWithModifiers(Modifier.TRANSIENT)
            .excludeFieldsWithModifiers(Modifier.STATIC)
            .create();

    @Override
    public String toJson(Object entity) {
        if (entity == null) {
            return "";
        }
        try {
            return GSON.toJson(entity);
        } catch (Exception e) {
            Timber.tag(CacheConstantKt.TAG).e("JsonSerializer toJson error with: entity = %s", entity.toString());
        }
        return "";
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T fromJson(String json, Type clazz) {
        try {
            if (clazz == String.class) {
                return (T) json;
            } else {
                return GSON.fromJson(json, clazz);
            }
        } catch (Exception e) {
            Timber.tag(CacheConstantKt.TAG).e("JsonSerializer fromJson error with: json = " + json + " class = " + clazz);
        }
        return null;
    }

}