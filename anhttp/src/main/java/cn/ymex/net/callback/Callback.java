package cn.ymex.net.callback;


import cn.ymex.net.Response;
import cn.ymex.net.parser.Convert;

/**
 *
 * @author ymexc
 * 2018/6/25
 * 请示回调
 */
public interface Callback<T> extends Convert<Response, T> {
    /**
     * 请求前回调
     * ui 线程
     */
    void onPrepare();

    /**
     * 请求响应
     * 子线程
     */
    void onResponse(Response response);

    /**
     * 发生错误 或 请求失败
     * ui 线程
     */
    void onError(Throwable throwable);

    /**
     * 结束请求
     * ui 线程
     */
    void onComplete();

    /**
     * 数据转换，子线程
     */
    // T convert(Response value) throws Exception;
}
