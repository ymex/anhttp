package cn.ymex.anhttp.parser.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import cn.ymex.net.Response;
import cn.ymex.net.parser.ParserResponse;

/**
 * Created by ymexc on 2018/6/28.
 * About:TODO
 */
public class ParserGson extends ParserResponse {

    private Gson gson;

    public ParserGson() {
        gson = new Gson();
    }

    @Override
    public Object convert(Response value) throws Exception {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(getType()));
        return adapter.fromJson(value.getResponse().body().string());
    }
}
