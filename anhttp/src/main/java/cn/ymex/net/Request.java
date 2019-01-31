package cn.ymex.net;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;

import cn.ymex.net.callback.Callback;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.internal.http.HttpMethod;

/**
 * Created by ymexc on 2018/6/25.
 * Request
 */
public class Request {
    private OkHttpClient okClient;
    private okhttp3.Request okRequest;
    private okhttp3.Call okCall;
    private RequestBody okRequestBody;
    private Map<String, String> requestBodyParam;
    private Headers okHeaders;

    private String mUrl;
    private String mMethod;
    private Object mTag;
    private Executor uiExecutor;


    Request() {
        okClient = AnHttp.instance().getOkHttpClient();
        uiExecutor = AnHttp.instance().getMainExecutor();
        requestBodyParam = new HashMap<>();
    }

    public Request url(String url) {
        this.mUrl = url;
        return this;
    }


    public Request method(@NonNull String method, @NonNull String url) {
        this.mUrl = url;
        this.mMethod = method;
        return this;
    }

    public Request tag(Object tag) {
        this.mTag = tag;
        return this;
    }


    public Request param(Param param) {
        this.okRequestBody = resetRequestBody(param).build();
        return this;
    }

    public Request param(RequestBody param) {
        this.okRequestBody = param;
        return this;
    }

    private Param resetRequestBody(Param param) {
        Map<String, String> commonParam = AnHttp.instance().getCommonParams();

        for (Map.Entry<String, String> entry : commonParam.entrySet()) {
            if (!param.innerParam.containsKey(entry.getKey())) {
                param.add(entry.getKey(), entry.getValue());
            }
        }

        for (Map.Entry<String, String> entry : param.innerParam.entrySet()) {
            requestBodyParam.put(entry.getKey(), entry.getValue());
        }
        return param;
    }

    public Request headers(Headers headers) {
        if (headers == null) {
            return this;
        }
        Headers.Builder builder = getHeaders().newBuilder();
        Iterator<String> iterator = headers.names().iterator();
        while (iterator.hasNext()) {
            String name = iterator.next();
            String value = headers.get(name);
            if (!TextUtils.isEmpty(value)) {
                builder.add(name, value);
            }

        }
        this.okHeaders = builder.build();
        return this;
    }

    public Request header(String key, String value) {
        okHeaders = getHeaders().newBuilder().add(key, value).build();
        return this;
    }

    public Request header(Map<String, String> headers) {
        Headers.Builder builder = getHeaders().newBuilder();

        Set<Map.Entry<String, String>> entries = headers.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            String name = entry.getKey();
            String value = entry.getValue();
            builder.add(name, value);
        }
        this.okHeaders = builder.build();
        return this;
    }

    private Headers getHeaders() {
        if (this.okHeaders == null) {
            this.okHeaders = AnHttp.instance().getCommonHeaders();
        }
        return okHeaders;
    }

    private void sendOnPrepare(final Callback callback) {
        uiExecutor.execute(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onPrepare();
                }
            }
        });
    }

    private void sendOnFailure(final Callback callback, final Throwable throwable) {
        if (callback != null) {
            uiExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    callback.onError(throwable);
                    callback.onComplete();
                }
            });
        }
    }

    private void sendOnResponse(Call call, final Callback callback, okhttp3.Response response) {
        if (callback != null) {
            Response responseWarp = new Response(call, response)
                    .code(response.code());

            callback.onResponse(responseWarp);
            uiExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    callback.onComplete();
                }
            });
        }
    }


    public Response call() throws IOException {
        okRequest = buildRequest();
        okCall = okClient.newCall(okRequest);
        okhttp3.Response response = okCall.execute();
        return new Response(okCall, response);
    }

    public void call(final Callback callback) {
        sendOnPrepare(callback);

        okRequest = buildRequest();
        okCall = okClient.newCall(okRequest);
        okCall.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                sendOnFailure(callback, e);
            }

            @Override
            public void onResponse(Call call, @NonNull okhttp3.Response response) throws IOException {
                sendOnResponse(call, callback, response);
            }
        });
    }


    public RequestBody getOkRequestBody() {
        if (okRequestBody == null && HttpMethod.requiresRequestBody(mMethod)) {
            okRequestBody = resetRequestBody(Param.form()).build();
        }
        return okRequestBody;
    }


    private String getUrl() {
        String url = mUrl;
        if (!(url.startsWith("http://") || url.startsWith("https://"))) {
            String baseUrl = AnHttp.instance().getBaseUrl();
            if (!TextUtils.isEmpty(baseUrl)) {
                this.mUrl = baseUrl + url;
            } else {
                this.mUrl = url;
            }
        }
        if (!HttpMethod.permitsRequestBody(mMethod)) {
            if (getOkRequestBody() == null) {
                resetRequestBody(Param.form());
            }

            if (requestBodyParam.isEmpty()) {
                return mUrl;
            }
            StringBuilder stringBuilder = new StringBuilder("");
            if (!mUrl.contains("?")) {
                stringBuilder.append("?");
            }
            for (Map.Entry<String, String> entry : requestBodyParam.entrySet()) {
                stringBuilder.append(entry.getKey());
                stringBuilder.append("=");
                stringBuilder.append(entry.getValue());
                stringBuilder.append("&");
            }

            stringBuilder.replace(stringBuilder.lastIndexOf("&"), stringBuilder.length(), "");
            mUrl = mUrl + stringBuilder.toString();
        }
        return mUrl;
    }

    private okhttp3.Request buildRequest() {

        return new okhttp3.Request.Builder()
                .url(getUrl())
                .tag(mTag)
                .headers(getHeaders())
                .method(mMethod, HttpMethod.permitsRequestBody(mMethod) ? getOkRequestBody() : null)
                .build();
    }

    public static interface Method {
        String GET = "GET";

        String POST = "POST";

        String PUT = "PUT";

        String DELETE = "DELETE";

        String HEAD = "HEAD";

        String PATCH = "PATCH";
    }
}
