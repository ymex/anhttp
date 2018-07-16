package cn.ymex.net.callback;


import cn.ymex.net.AnHttp;
import cn.ymex.net.Response;
import cn.ymex.net.exception.NetException;
import cn.ymex.net.kits.Log;

/**
 * @author ymexc 2018/6/25
 */
public abstract class AbstractCallback<T> implements Callback<T> {

    @Override
    public void onPrepare() {

    }

    @Override
    public void onResponse(final Response response) {
        try {
            final T t = convert(response);
            AnHttp.instance().getMainExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        onResult(t, new Response.Status(response.getResponse()));
                    } catch (NetException e) {
                        onError(e);
                    }
                }
            });
        } catch (final Exception e) {
            AnHttp.instance().getMainExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    onError(e);
                }
            });
        }

    }

    /**
     * 数据结果
     * ui 线程
     * 注意：
     *
     * @param status
     * @param result
     */
    public abstract void onResult(T result, Response.Status status) throws NetException;

    @Override
    public void onError(Throwable throwable) {
        Log.error("AnHttp error:", throwable);
    }

    @Override
    public void onComplete() {

    }

    @Override
    public abstract T convert(Response value) throws Exception;


    /**
     * 请求响应成功
     * @param response
     * @return
     */
    public boolean isHttpSuccess(Response response) {
        int code = response.getResponse().code();
        return code >= 200 && code < 400;
    }

}
