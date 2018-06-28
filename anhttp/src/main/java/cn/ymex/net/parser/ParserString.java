package cn.ymex.net.parser;


import java.lang.reflect.Type;

import cn.ymex.net.Response;

/**
 * Created by ymex on 2018/6/25.
 * About:
 */
public class ParserString extends ParserResponse<String> {
    @Override
    public String convert(Response value) throws Exception {
        assert value.getResponse().body() != null;
        return value.getResponse().body().string();
    }

    @Override
    public Type getType() {
        return String.class;
    }
}
