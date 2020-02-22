package com.zzh.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description:
 * @Author: Administrator
 * @Date: 2020/2/18 20:08
 */
@Slf4j
public class HttpUtil
{
    private static final MediaType mediaType = MediaType.parse("application/json");
    private static OkHttpClient httpClient;

    static
    {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);

        httpClient = builder.build();
    }


    public static <T> T doPostSync(String url, String body, Class<T> clazz)
    {
        RequestBody requestBody = RequestBody.create(mediaType, body);

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        try
        {
            Response response = httpClient.newCall(request).execute();
            if (!response.isSuccessful())
            {
                throw new IOException("Unexpected code " + response);
            }
            ResponseBody responseBody = response.body();
            String content = responseBody.string();
            return JSONObject.parseObject(content, clazz);
        } catch (Exception e)
        {
            log.error("login error", e);
        }
        return null;
    }
}
