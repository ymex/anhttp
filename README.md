# anhttp
基于okhttp 的封装，方便简单。


```
compile 'cn.ymex.anhttp:anhttp:1.0.6'

#支持Gson转换
compile 'cn.ymex.anhttp:anhttp-gson:1.0.6'
```

## 使用

目前 anhttp 支持 `get`和`post`请求，后期拓展中。

异步请求
```
AnHttp.get(url).call(new ResponseCallback<BaseModel<List<ResultsBean>>>(){
    @Override
    public void onResult(BaseModel<List<ResultsBean>> result, Response.Status status) {
        if (status.isSuccessful()) {
            tvContent.setText(result.getResults().get(0).getContent());
        }
    }
});
```
