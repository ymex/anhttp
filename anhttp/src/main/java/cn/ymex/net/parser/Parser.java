package cn.ymex.net.parser;


import cn.ymex.net.Response;

public interface Parser<F, T> {
    T convert(F value) throws Exception;

    public class Factory{
        Parser<Response, String> createStringParser() {
            return null;
        }
    }
}