package cn.ymex.net;

import android.support.annotation.Nullable;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author ymexc
 * 2018/6/27
 * Param 参数
 */
public final class Param {

    FormBody.Builder formBuilder;
    MultipartBody.Builder mutipartBuilder;
    okhttp3.RequestBody requestBody;
    Map<String, String> innerParam = new HashMap<>();


    String jsonMediaType = null;

    private Param(FormBody.Builder formBuilder) {
        this.formBuilder = formBuilder;
    }

    private Param(MultipartBody.Builder mutipartBuilder) {
        this.mutipartBuilder = mutipartBuilder;
    }

    private Param(RequestBody body) {
        this.requestBody = body;
    }

    private Param() {
        this.jsonMediaType = "application/json; charset=utf-8";
    }

    public static Param form() {
        return new Param(new FormBody.Builder());
    }

    public static Param multipart() {
        return new Param(new MultipartBody.Builder());
    }

    public static Param json() {
        return new Param();
    }

    public static Param param(RequestBody requestBody) {
        return new Param(requestBody);
    }

    public Param addEncoded(String key, String value) {
        if (formBuilder != null) {
            formBuilder.addEncoded(key, value);
        } else if (mutipartBuilder != null) {
            mutipartBuilder.addFormDataPart(key, value);
        }
        innerParam.put(key, value);
        return this;
    }

    public Param add(String key, String value) {
        if (formBuilder != null) {
            formBuilder.add(key, value);
        } else if (mutipartBuilder != null) {
            mutipartBuilder.addFormDataPart(key, value);
        }
        innerParam.put(key, value);
        return this;
    }

    public Param type(MediaType type) {
        if (mutipartBuilder != null) {
            mutipartBuilder.setType(type);
        }
        return this;
    }

    /**
     * Add a part to the body.
     */
    public Param add(okhttp3.RequestBody body) {
        if (mutipartBuilder != null) {
            mutipartBuilder.addPart(body);
        }
        return this;
    }

    /**
     * Add a part to the body.
     */
    public Param add(@Nullable Headers headers, okhttp3.RequestBody body) {
        if (mutipartBuilder != null) {
            mutipartBuilder.addPart(headers, body);
        }
        return this;
    }

    /**
     * Add a part to the body.
     */
    public Param add(MultipartBody.Part part) {
        if (mutipartBuilder != null) {
            mutipartBuilder.addPart(part);
        }
        return this;
    }

    /**
     * Add a form data part to the body.
     */
    public Param add(String name, @Nullable String filename, okhttp3.RequestBody body) {
        if (mutipartBuilder != null) {
            mutipartBuilder.addFormDataPart(name, filename, body);
        }
        return this;
    }


    public okhttp3.RequestBody build() {
        if (formBuilder != null) {
            return formBuilder.build();
        } else if (mutipartBuilder != null) {
            return mutipartBuilder.build();
        } else if (requestBody != null) {
            return requestBody;
        } else if (jsonMediaType != null) {

            return RequestBody.create(MediaType.parse(jsonMediaType), param2Json());
        } else {
            return RequestBody.create(null, "");
        }
    }

    /**
     * innerParam 参数转换成字串
     * @return
     */
    public String param2Json() {
        JSONObject object = new JSONObject(innerParam);
        return object.toString();
    }

}
