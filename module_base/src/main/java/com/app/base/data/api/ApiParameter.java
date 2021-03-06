package com.app.base.data.api;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.android.base.utils.StringChecker;
import com.app.base.AppContext;
import com.app.base.data.DataConfig;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import timber.log.Timber;

public class ApiParameter {

    public static void init() {
        // to do，move it to native
    }

    // dev uat release
    public static int PLATFORM_COUNT = -1;
    @NonNull
    public static String PLATFORM = "setup here gradle.properties";
    @NonNull
    public static String BASE_URL = "setup here gradle.properties";
    @NonNull
    public static String BASE_URL_FOR_H5 = "setup here gradle.properties";

    // keep it the same as platform name from gradle.properties
    public static String PLATFORM_REGULAR = "regular";
    public static String PLATFORM_B = "b";

    // --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    // keys values
    final static String SIGN_KEY = "sign";
    final static String APP_TOKEN_KEY = "app_token";
    private final static String GNW_APP_ID_KEY = "dm_music_appid";
    private final static String API_VERSION_NAME_KEY = "api_versionName";

    public final static String API_VERSION_VALUE = "1.0.0"; // 接口版本
    private static String GNW_APP_ID_VALUE = "437EC0AC7F0000015E2BBF4849643C96";

    // tags
    static final String APP_TOKEN_HEADER = "need_app_token";
    public static final String WITH_APP_TOKEN = APP_TOKEN_HEADER + ":true";

    // --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    static boolean isHeaderValueTrue(String value) {
        Timber.d("isHeaderValueTrue() called with: value = [" + value + "]");
        return "true".equals(value);
    }

    /**
     * 生成公共的参数，以Map<String, String> 的形式返回
     *
     * @return 公共的参数
     */
    public static Map<String, String> generateCommonParamsMap() {
        checkInitialize();
        Map<String, String> map = new HashMap<>();
        map.put(GNW_APP_ID_KEY, GNW_APP_ID_VALUE);
        map.put(API_VERSION_NAME_KEY, API_VERSION_VALUE);
        return map;
    }

    /**
     * 生成公共的参数，以Map<String, String> 的形式返回
     *
     * @return 公共的参数
     */
    static JsonObject generateCommonParamsJson() {
        checkInitialize();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(GNW_APP_ID_KEY, GNW_APP_ID_VALUE);
        jsonObject.addProperty(API_VERSION_NAME_KEY, API_VERSION_VALUE);
        return jsonObject;
    }

    public static RequestBody createFormBody(Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        Set<Map.Entry<String, String>> entries = params.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            builder.add(entry.getKey(), entry.getValue());
        }
        return builder.build();
    }

    /**
     * 生成公共的参数，以 Map<String, RequestBody>的形式返回
     *
     * @return 公共的参数
     */
    public static Map<String, RequestBody> generateCommonMultiPartRequestBody() {
        checkInitialize();
        Map<String, RequestBody> params = new HashMap<>();
        Map<String, String> stringStringMap = generateCommonParamsMap();
        Set<Map.Entry<String, String>> entries = stringStringMap.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            params.put(entry.getKey(), RequestBody.create(null, entry.getValue()));
        }
        return params;
    }

    private static void checkInitialize() {
        if (StringChecker.isEmpty(AppContext.get().appDataSource.appToken()) || StringChecker.isEmpty(GNW_APP_ID_VALUE)) {
            throw new IllegalStateException("ApiParameter has not initialize");
        }
    }

    public static void addImageFileParts(Map<String, RequestBody> requestBody, String name, List<File> files) {
        RequestBody photoBody;
        int index = 1;
        for (File file : files) {
            if (!file.exists()) {
                continue;
            }
            photoBody = RequestBody.create(MediaType.parse("image/*"), file);
            requestBody.put(name + "_" + index + "\"; filename=\"" + file.getName() + "", photoBody);
            index++;
        }
    }

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    static RequestBody createJsonBody(String json) {
        return RequestBody.create(JSON, json);
    }

    /**
     * for h5
     */
    public static String genSignAndGenerateRequestParams(String json, Boolean isNeedChildDeviceId, Boolean isNeedToken, Boolean isNeedChildId) {
        JsonObject jsonObject = generateCommonParamsJson();
        /*添加app token*/
        if (isNeedToken) {
            String appToken = DataConfig.getInstance().getAppToken();
            jsonObject.addProperty(ApiParameter.APP_TOKEN_KEY, appToken == null ? "" : appToken);
        }
        /*添加参数*/
        if (!TextUtils.isEmpty(json) && (json.startsWith("{") && json.endsWith("}"))) {
            try {
                JsonElement parse = new JsonParser().parse(json);
                if (parse.isJsonObject()) {
                    JsonObject obj = parse.getAsJsonObject();
                    for (String key : obj.keySet()) {
                        JsonElement element = obj.get(key);
                        if (element.isJsonObject() || element.isJsonArray()) {
                            jsonObject.add(key, element);
                        } else {
                            jsonObject.addProperty(key, element.getAsString());
                        }
                    }
                }
            } catch (JsonSyntaxException e) {
                Timber.e(e, "parse json error");
            }
        }
        String sign = SignTools.genSign(jsonObject, String.valueOf(Timestamp.getTimestamp()), AppContext.get().appDataSource.appToken());
        jsonObject.addProperty(ApiParameter.SIGN_KEY, sign);
        return jsonObject.toString();
    }

}
