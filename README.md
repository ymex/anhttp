[ ![Download](https://api.bintray.com/packages/ymex/maven/anhttp/images/download.svg) ](https://bintray.com/ymex/maven/anhttp/_latestVersion)

# anhttp
基于okhttp 的封装，方便简单。


```
compile 'cn.ymex.anhttp:anhttp:1.0.8'

#支持Gson转换
compile 'cn.ymex.anhttp:anhttp-gson:1.0.8'
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
