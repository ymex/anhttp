package cn.ymex.net.parser;


import org.json.JSONArray;

import cn.ymex.net.Response;

/**
 * Created by ymex on 2018/6/25.
 * ParserJsonArray
 */
public class ParserJsonArray extends ParserResponse<JSONArray> {

    @Override
    public JSONArray convert(Response value) throws Exception {
        assert value.getBody() != null;
        return new JSONArray(value.getBody().string());
    }
}
