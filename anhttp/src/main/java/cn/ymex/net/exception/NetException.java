package cn.ymex.net.exception;

import android.annotation.TargetApi;
import android.os.Build;

/**
 *
 * @author ymexc
 * 2018/6/25
 */
public class NetException extends Exception {

    public static final String NOPARSER_MESSAGE = "Type that is not supported, please customize type conversion Parser";



    public NetException() {
    }

    public NetException(String message) {
        super(message);
    }

    public NetException(String message, Throwable cause) {
        super(message, cause);
    }

    public NetException(Throwable cause) {
        super(cause);
    }

    @TargetApi(Build.VERSION_CODES.N)
    public NetException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
