package cn.ymex.net.parser;


import java.lang.reflect.Type;

import cn.ymex.net.Response;

/**
 *
 * @author ymex
 *2018/6/25
 * ParserString
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
