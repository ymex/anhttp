package cn.ymex.net.parser;


import java.lang.reflect.Type;

import cn.ymex.net.Response;

/**
 * Created by ymex on 2018/6/25.
 * ParserResponse
 */
public abstract class ParserResponse<T> implements Parser<Response, T> {
    private Type mType;

    @Override
    public abstract T convert(Response value) throws Exception;

    @Override
    public void setType(Type type) {
        this.mType = type;
    }

    @Override
    public Type getType() {
        return mType;
    }
}
