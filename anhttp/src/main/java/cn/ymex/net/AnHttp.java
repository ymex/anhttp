package cn.ymex.net;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import cn.ymex.net.dispatch.MainThreadExecutor;
import cn.ymex.net.interceptor.LogInterceptor;
import cn.ymex.net.kits.HttpHeaders;
import cn.ymex.net.parser.Parser;
import okhttp3.Call;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import okhttp3.Headers;
import okhttp3.OkHttpClient;


/**
 * @author ymexc
 * 2018/6/25
 * AnHttp
 */
public class AnHttp {

    private static volatile AnHttp sAnHttp;

    private Context context;
    private Parser<Response, ?> parser;
    private boolean debug = false;
    private OkHttpClient okHttpClient;
    private Headers headers;
    private MainThreadExecutor mainExecutor;


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

    public boolean isDebug() {
        return debug;
    }

    /**
     * 设置是否是dev 环境，默认okHttpClient会打印相关日志
     *
     * @param debug
     * @return
     */
    public AnHttp setDebug(boolean debug) {
        this.debug = debug;
        return this;
    }

    private AnHttp() {
        mainExecutor = new MainThreadExecutor();
    }

    /**
     * 主线程
     *
     * @return
     */
    public MainThreadExecutor getMainExecutor() {
        return mainExecutor;
    }

    public OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = defaultClient();
        }
        return okHttpClient;
    }

    /**
     * 设置OkHttpClient
     *
     * @param okHttpClient
     * @return
     */
    public AnHttp setOkHttpClient(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
        return this;
    }

    /**
     * 设置 content
     *
     * @param context
     * @return
     */
    public AnHttp setContext(Context context) {
        if (context != null) {
            this.context = context.getApplicationContext();
        }
        return this;
    }

    /**
     * 设置公共请求头
     *
     * @param headers
     * @return
     */
    public AnHttp setHeaders(Headers headers) {
        this.headers = headers;
        return this;
    }


    /**
     * 设置全局响应转换
     *
     * @param parser
     * @return
     */
    public AnHttp setParser(Parser<Response, ?> parser) {
        this.parser = parser;
        return this;
    }

    private OkHttpClient defaultClient() {
        LogInterceptor logInterceptor = new LogInterceptor();
        logInterceptor.setLevel(isDebug() ? LogInterceptor.Level.BODY : LogInterceptor.Level.NONE);
        return new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(5, 5, TimeUnit.MINUTES))
                .retryOnConnectionFailure(true)
                .dispatcher(new Dispatcher())
                .addInterceptor(logInterceptor)
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.MINUTES)
                .build();
    }


    private Headers defaultHeaders() {
        return new Headers.Builder()
                .add("Accept-Language", HttpHeaders.getAcceptLanguage())
                .add("User-Agent", HttpHeaders.getUserAgent(context))
                .build();
    }

    /**************http method***************/

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


    public Parser<Response, ?> getParser() {
        return parser;
    }

}
