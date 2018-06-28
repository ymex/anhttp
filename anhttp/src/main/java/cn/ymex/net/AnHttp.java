package cn.ymex.net;

import java.util.concurrent.TimeUnit;

import cn.ymex.net.dispatch.MainThreadExecutor;
import cn.ymex.net.kits.HttpHeaders;
import cn.ymex.net.parser.Parser;
import okhttp3.Call;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import okhttp3.Headers;
import okhttp3.OkHttpClient;


/**
 * Created by ymexc on 2018/6/25.
 * AnHttp
 */
public class AnHttp {
    private static volatile AnHttp sAnHttp;
    private OkHttpClient okHttpClient;
    private MainThreadExecutor mainExecutor;

    private Headers headers;

    public static AnHttp instance() {
        if (sAnHttp == null) {
            synchronized (AnHttp.class) {
                if (sAnHttp == null) {
                    sAnHttp = new AnHttp();
                }
            }
        }
        return sAnHttp;
    }

    private AnHttp() {
        mainExecutor = new MainThreadExecutor();
    }

    public MainThreadExecutor getMainExecutor() {
        return mainExecutor;
    }

    public OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = defaultClient();
        }
        return okHttpClient;
    }

    public void setOkHttpClient(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }


    private OkHttpClient defaultClient() {
        return new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(5, 5, TimeUnit.MINUTES))
                .retryOnConnectionFailure(true)
                .dispatcher(new Dispatcher())
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.MINUTES)
                .build();
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    private Headers defaultHeaders() {
        return new Headers.Builder()
                .add("Accept-Language", HttpHeaders.getAcceptLanguage())
                .add("User-Agent", HttpHeaders.getUserAgent(null))
                .build();
    }

    public static Request get(String url) {
        return new Request().method(Request.Method.GET, url);
    }

    public static Request post(String url) {
        return new Request().method(Request.Method.POST, url);
    }


    public Headers getHeaders() {
        if (headers == null) {
            headers = defaultHeaders();
        }
        return headers;
    }

    /**
     * 取消请求
     *
     * @param tag
     */

    public void cancelTag(Object tag) {
        cancelTag(getOkHttpClient(), tag);
    }


    public static void cancelTag(OkHttpClient client, Object tag) {
        if (client == null || tag == null) {
            return;
        }

        for (Call call : client.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : client.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    private Parser<Response, ?> parser;

    public void setParser(Parser<Response, ?> parser) {
        this.parser = parser;
    }

    public Parser<Response, ?> getParser() {
        return parser;
    }

}
