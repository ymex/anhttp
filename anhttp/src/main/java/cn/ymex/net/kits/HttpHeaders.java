package cn.ymex.net.kits;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class HttpHeaders {
    public static final String KEY_ACCEPT_LANGUAGE = "Accept-Language";
    public static final String KEY_USER_AGENT = "User-Agent";
    private static String webUserAgent = "okhttp/3.10.0";

    public static void setWebUserAgent(String webUserAgent) {
        HttpHeaders.webUserAgent = webUserAgent;
    }

    public static long getDate(String gmtTime) {
        try {
            return parseGMTToMillis(gmtTime);
        } catch (ParseException e) {
            return 0;
        }
    }

    public static String getDate(long milliseconds) {
        return formatMillisToGMT(milliseconds);
    }

    public static long getExpiration(String expiresTime) {
        try {
            return parseGMTToMillis(expiresTime);
        } catch (ParseException e) {
            return -1;
        }
    }

    public static long getLastModified(String lastModified) {
        try {
            return parseGMTToMillis(lastModified);
        } catch (ParseException e) {
            return 0;
        }
    }

    public static String getCacheControl(String cacheControl, String pragma) {
        // first http1.1, second http1.0
        if (cacheControl != null) {

            return cacheControl;
        } else if (pragma != null) {

            return pragma;
        }
        return null;
    }


    /**
     * Accept-Language: zh-CN,zh;q=0.8
     */
    public static String getAcceptLanguage() {

        Locale locale = Locale.getDefault();
        String language = locale.getLanguage();
        String country = locale.getCountry();
        StringBuilder acceptLanguageBuilder = new StringBuilder(language);
        if (!TextUtils.isEmpty(country)) {
            acceptLanguageBuilder.append('-').append(country).append(',').append(language).append(";q=0.8");
        }
        return acceptLanguageBuilder.toString();

    }


    public static String getUserAgent(Context context) {
        if (context == null) {
            return webUserAgent;
        }
        String agent = "";
        try {
            Class<?> sysResCls = Class.forName("com.android.internal.R$string");
            Field webUserAgentField = sysResCls.getDeclaredField("web_user_agent");
            Integer resId = (Integer) webUserAgentField.get(null);
            agent = context.getString(resId);
        } catch (Exception ignored) {
        }
        if (!TextUtils.isEmpty(agent)) {
            webUserAgent = agent;
        }

        Locale locale = Locale.getDefault();
        StringBuffer buffer = new StringBuffer();
        // Add version
        final String version = Build.VERSION.RELEASE;
        if (version.length() > 0) {
            buffer.append(version);
        } else {
            buffer.append("1.0");
        }
        buffer.append("; ");
        final String language = locale.getLanguage();
        if (language != null) {
            buffer.append(language.toLowerCase(locale));
            final String country = locale.getCountry();
            if (!TextUtils.isEmpty(country)) {
                buffer.append("-");
                buffer.append(country.toLowerCase(locale));
            }
        } else {
            // default to "en"
            buffer.append("en");
        }
        // add the model for the release build
        if ("REL".equals(Build.VERSION.CODENAME)) {
            final String model = Build.MODEL;
            if (model.length() > 0) {
                buffer.append("; ");
                buffer.append(model);
            }
        }
        final String id = Build.ID;
        if (id.length() > 0) {
            buffer.append(" Build/");
            buffer.append(id);
        }
        return String.format(webUserAgent, buffer, "Mobile ");
    }

    public static final String FORMAT_HTTP_DATA = "EEE, dd MMM y HH:mm:ss 'GMT'";
    public static final TimeZone GMT_TIME_ZONE = TimeZone.getTimeZone("GMT");

    public static long parseGMTToMillis(String gmtTime) throws ParseException {
        if (TextUtils.isEmpty(gmtTime)) {
            return 0;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_HTTP_DATA, Locale.US);
        formatter.setTimeZone(GMT_TIME_ZONE);
        Date date = formatter.parse(gmtTime);
        return date.getTime();
    }

    public static String formatMillisToGMT(long milliseconds) {
        Date date = new Date(milliseconds);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_HTTP_DATA, Locale.US);
        simpleDateFormat.setTimeZone(GMT_TIME_ZONE);
        return simpleDateFormat.format(date);
    }
}
