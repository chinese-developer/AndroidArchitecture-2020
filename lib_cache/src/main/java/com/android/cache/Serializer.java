/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 */

package com.android.cache;

import java.lang.reflect.Type;

public interface Serializer {

    String toJson(Object entity);

    <T> T fromJson(String json, Type clazz);

}
