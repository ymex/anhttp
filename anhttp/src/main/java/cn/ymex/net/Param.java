package cn.ymex.net;

import android.support.annotation.Nullable;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;

/**
 * @author ymexc
 * @date 2018/6/27
 * Param 参数
 */
public final class Param {

    FormBody.Builder formBuilder;
    MultipartBody.Builder mutipartBuilder;

    private Param(FormBody.Builder formBuilder) {
        this.formBuilder = formBuilder;
    }

    private Param(MultipartBody.Builder mutipartBuilder) {
        this.mutipartBuilder = mutipartBuilder;
    }

    public static Param form() {
        return new Param(new FormBody.Builder());
    }

    public static Param multipart() {
        return new Param(new MultipartBody.Builder());
    }


    public Param add(String key, String value) {
        if (formBuilder != null) {
            formBuilder.add(key, value);
        } else if (mutipartBuilder != null) {
            mutipartBuilder.addFormDataPart(key, value);
        }
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
        }
        return mutipartBuilder.build();
    }

}
