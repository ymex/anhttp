package cn.ymex.net;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.ResponseBody;

/**
 *Response
 */
public final class Response {

    private okhttp3.Call call;
    private okhttp3.Response response;

    private int code;

    public Response(Call call, okhttp3.Response response) {
        this.call = call;
        this.response = response;
    }

    public Response code(int code) {
        this.code = code;
        return this;
    }

    public Call getCall() {
        return call;
    }

    public ResponseBody getBody() {
        return response != null ? response.body() : null;
    }

    public okhttp3.Response getResponse() {
        return response;
    }

    public static class Status {

        private okhttp3.Response response;

        public Status(okhttp3.Response response) {
            this.response = response;
        }

        public long receivedResponseAtMillis() {
            return response.receivedResponseAtMillis();
        }

        public long sentRequestAtMillis() {
            return response.sentRequestAtMillis();
        }

        public boolean isRedirect() {
            return response.isRedirect();
        }

        public boolean isSuccessful() {
            return response.isSuccessful();
        }

        public Request request() {
            return response.request();
        }

        public Protocol protocol() {
            return response.protocol();
        }

        public String message() {
            return response.message();
        }

        public int code() {
            return response.code();
        }

        public Headers headers() {
            return response.headers();
        }
    }
}
