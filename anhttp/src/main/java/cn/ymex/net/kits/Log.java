package cn.ymex.net.kits;

import cn.ymex.net.AnHttp;

/**
 * 日志打印
 */
public class Log {
    private static String TAG = "AnHttp-->";

    public static void info(String message, Throwable throwable) {
        if (AnHttp.instance().isDebug()) {
            android.util.Log.i(TAG, message, throwable);
        }
    }

    public static void error(String message, Throwable throwable) {
        if (AnHttp.instance().isDebug()) {
            android.util.Log.e(TAG, message, throwable);
        }
    }

}
