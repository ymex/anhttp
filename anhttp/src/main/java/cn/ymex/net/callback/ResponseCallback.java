package cn.ymex.net.callback;


import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import cn.ymex.net.AnHttp;
import cn.ymex.net.Response;
import cn.ymex.net.exception.NetException;
import cn.ymex.net.parser.Parser;
import cn.ymex.net.parser.ParserJsonArray;
import cn.ymex.net.parser.ParserJsonObject;
import cn.ymex.net.parser.ParserString;
import okhttp3.ResponseBody;

/**
 * @author ymexc
 * 2018/6/25
 * About:响应处理
 * 注意：仅支持 String ,JSONObject,JSONArray,不支持泛型
 */
public class ResponseCallback<T> extends AbstractCallback<T> {

    Type mType;
    Parser<Response, ?> parser;

    public ResponseCallback() {
    }

    public ResponseCallback(Type mType) {
        this.mType = mType;
    }

    public ResponseCallback(Parser<Response, ?> parser) {
        this.parser = parser;
    }

    @Override
    public void onResult(T result, Response.Status status) {

    }

    @Override
    public T convert(Response value) throws Exception {

        if (parser == null) {
            parser = AnHttp.instance().getParser();
            if (parser == null) {
                if (mType == null) {
                    Type genType = getClass().getGenericSuperclass();
                    mType = ((ParameterizedType) genType).getActualTypeArguments()[0];
                }
                if (mType instanceof Class) {
                    parser = parseClass(value, (Class<?>) mType);
                } else {
                    throw new NetException(NetException.NOPARSER_MESSAGE);
                }
            }
        }
        //noinspection unchecked
        return (T) parser.convert(value);

    }

    private Parser<Response, ?> parseClass(Response response, Class<?> type) throws Exception {
        if (type == null) {
            return null;
        }
        ResponseBody body = response.getBody();
        if (body == null) {
            return null;
        }
        if (type == String.class) {
            return new ParserString();
        } else if (type == JSONObject.class) {
            return new ParserJsonObject();
        } else if (type == JSONArray.class) {
            return new ParserJsonArray();
        } else {
            throw new NetException(NetException.NOPARSER_MESSAGE);
        }
    }
}
