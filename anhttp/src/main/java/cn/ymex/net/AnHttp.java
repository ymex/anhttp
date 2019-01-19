package cn.ymex.net;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import cn.ymex.net.dispatch.MainThreadExecutor;
import cn.ymex.net.interceptor.LogInterceptor;
import cn.ymex.net.kits.HttpHeaders;
import cn.ymex.net.kits.SSLKit;
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
    private String baseUrl;
    private Context context;
    private Parser<Response, ?> parser;
    private boolean debug = false;
    private OkHttpClient okHttpClient;
    private MainThreadExecutor mainExecutor;
    private boolean autoProvingResponseCode;
    private long readTimeout = 60;
    private long connectTimeout = 60;
    private long writeTimeout = 60;
    /**
     * 共同头部
     */
    private Headers commonHeaders;
    /**
     * 共同参数
     */
    private Map<String, String> commonParams;


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
     * 自动校验返回状态码，400
     *
     * @param autoProvingResponseCode
     * @return
     */
    public AnHttp setAutoProvingResponseCode(boolean autoProvingResponseCode) {
        this.autoProvingResponseCode = autoProvingResponseCode;
        return this;
    }

    public boolean isAutoProvingResponseCode() {
        return autoProvingResponseCode;
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
     * 设置统一请求地址
     *
     * @param baseUrl
     * @return
     */
    public AnHttp setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * 添加公共头部
     *
     * @param headers
     * @return
     */
    public AnHttp addCommonHeaders(Headers headers) {
        if (headers == null) {
            return this;
        }
        Headers.Builder builder = getCommonHeaders().newBuilder();
        Iterator<String> iterator = headers.names().iterator();
        while (iterator.hasNext()) {
            String name = iterator.next();
            String value = headers.get(name);
            if (!TextUtils.isEmpty(value)) {
                builder.add(name, value);
            }

        }
        commonHeaders = builder.build();
        return this;
    }

    /**
     * 添加公共头部
     *
     * @param k
     * @param v
     * @return
     */
    public AnHttp addCommonHeader(String k, String v) {
        commonHeaders = getCommonHeaders().newBuilder().add(k, v).build();
        return this;
    }

    /**
     * 清空公共头部
     *
     * @return
     */
    public AnHttp clearCommonHeader() {
        commonHeaders = null;
        commonHeaders = getCommonHeaders();
        return this;
    }

    public Headers getCommonHeaders() {
        if (commonHeaders == null) {
            commonHeaders = defaultHeaders();
        }
        return commonHeaders;
    }

    /**
     * 添加公共参数
     *
     * @param k
     * @param v
     * @return
     */
    public AnHttp addCommonParam(String k, String v) {
        getCommonParams().put(k, v);
        return this;
    }

    public AnHttp addCommonParams(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return this;
        }
        getCommonParams().putAll(params);
        return this;
    }


    public AnHttp setReadTimeout(long time) {
        this.readTimeout = time;
        return this;
    }

    public AnHttp setConnectTimeout(long time) {
        this.connectTimeout = time;
        return this;
    }

    public AnHttp setWriteTimeout(long time) {
        this.writeTimeout = time;
        return this;
    }

    public AnHttp clearCommonParams() {
        getCommonParams().clear();
        return this;
    }

    public Map<String, String> getCommonParams() {
        if (commonParams == null) {
            commonParams = new LinkedHashMap<>();
        }
        return commonParams;
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
        //信任所有证书
        SSLKit.SSLParams sslParams = SSLKit.getSslSocketFactory(null, null, null);
        return new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(5, 5, TimeUnit.MINUTES))
                .retryOnConnectionFailure(true)
                .dispatcher(new Dispatcher())
                .addInterceptor(logInterceptor)
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .build();
    }


    private Headers defaultHeaders() {
        return new Headers.Builder()
                .add("Accept-Language", HttpHeaders.getAcceptLanguage())
                .add("User-Agent", HttpHeaders.getUserAgent(context))
                .build();
    }

    /**************http method***************/

    public static Request get(@NonNull String url) {
        return new Request().method(Request.Method.GET, url);
    }

    public static Request post(@NonNull String url) {
        return new Request().method(Request.Method.POST, url);
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
