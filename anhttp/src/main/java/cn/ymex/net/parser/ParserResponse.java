package cn.ymex.net.parser;


import cn.ymex.net.Response;

/**
 * Created by ymex on 2018/6/25.
 * ParserResponse
 */
public abstract class ParserResponse<T> implements Parser<Response, T> {

    @Override
    public abstract T convert(Response value) throws Exception;
}
