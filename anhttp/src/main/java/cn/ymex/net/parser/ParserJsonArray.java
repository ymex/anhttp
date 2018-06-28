package cn.ymex.net.parser;


import org.json.JSONArray;

import java.lang.reflect.Type;

import cn.ymex.net.Response;

/**
 *
 * @author ymex
 * 2018/6/25
 * ParserJsonArray
 */
public class ParserJsonArray extends ParserResponse<JSONArray> {

    @Override
    public JSONArray convert(Response value) throws Exception {
        assert value.getBody() != null;
        return new JSONArray(value.getBody().string());
    }

    @Override
    public Type getType() {
        return JSONArray.class;
    }
}
