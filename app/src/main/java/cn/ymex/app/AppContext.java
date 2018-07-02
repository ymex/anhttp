package cn.ymex.app;

import android.app.Application;

import cn.ymex.anhttp.parser.gson.ParserGson;
import cn.ymex.net.AnHttp;

/**
 * Created by ymex on 2018/7/1.
 * About:
 */
public class AppContext extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AnHttp.instance().setDebug(true)
                .setContext(this).setParser(new ParserGson());
    }
}
