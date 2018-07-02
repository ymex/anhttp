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
public class ResponseCallback<E> extends AbstractCallback<E> {

    private Type mType;
    private Parser<Response, ?> parser;

    public ResponseCallback() {
    }

    public ResponseCallback(Type mType) {
        this.mType = mType;
    }

    public ResponseCallback(Parser<Response, ?> parser) {
        this.parser = parser;
    }

    @Override
    public E convert(Response value) throws Exception {

        if (null == mType) {
            Type genType = getClass().getGenericSuperclass();
            mType = ((ParameterizedType) genType).getActualTypeArguments()[0];
        }

        if (null == parser) {
            parser = AnHttp.instance().getParser();
            if (null == parser) {
                if (mType instanceof Class) {
                    parser = parseClass(value, (Class<?>) mType);
                } else {
                    throw new NetException("Type " + mType + NetException.NOPARSER_MESSAGE);
                }
            }
        }


        parser.setType(mType);
        //noinspection unchecked
        return (E) parser.convert(value);

    }

    /**
     * 结果处理
     *
     * @param result
     * @param status
     */
    @Override
    public void onResult(E result, Response.Status status) {

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
            throw new NetException(mType + ":" + NetException.NOPARSER_MESSAGE);
        }
    }


}
