package cn.ymex.net.parser;


import org.json.JSONObject;

import java.lang.reflect.Type;

import cn.ymex.net.Response;

/**
 *
 * @author ymex
 * 2018/6/25
 * ParserJsonObject
 */
public class ParserJsonObject extends ParserResponse<JSONObject> {

    @Override
    public JSONObject convert(Response value) throws Exception {
        assert value.getBody() != null;
        return new JSONObject(value.getBody().string());
    }

    @Override
    public Type getType() {
        return JSONObject.class;
    }
}
