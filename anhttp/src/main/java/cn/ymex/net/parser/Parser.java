package cn.ymex.net.parser;


import java.lang.reflect.Type;

import cn.ymex.net.Response;

public interface Parser<F, T> {

    T convert(F value) throws Exception;

    void setType(Type type);

    Type getType();

    public class Factory{
        Parser<Response, String> createStringParser() {
            return null;
        }
    }
}