package cn.ymex.app;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.util.List;

import cn.ymex.net.AnHttp;
import cn.ymex.net.Response;
import cn.ymex.net.callback.ResponseCallback;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {
    private TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tvContent = findViewById(R.id.tvContent);
        tvContent.setMovementMethod(ScrollingMovementMethod.getInstance());
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestHistory();
            }
        });
    }

    private void requestHistory() {
        get();
    }


    private void get() {
//        AnHttp.get("content/2/1").call(new ResponseCallback<BaseModel<List<ResultsBean>>>() {
//            @Override
//            public void onResult(BaseModel<List<ResultsBean>> result, Response.Status status) {
//                if (status.isSuccessful()) {
//                    tvContent.setText(result.getResults().get(0).getContent());
//                }
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//                super.onError(throwable);
//                System.out.println("---------:" + throwable.getLocalizedMessage());
//            }
//        });

        AnHttp.get("http://192.168.0.110/api/search").call(new ResponseCallback<BaseModel<List<ResultsBean>>>() {
            @Override
            public void onResult(BaseModel<List<ResultsBean>> result, Response.Status status) {
                if (status.isSuccessful()) {
                    tvContent.setText(result.getMessage());
                }
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                System.out.println("---------:" + throwable.getLocalizedMessage());
            }
        });

    }


    public   Observable<Response> rxGet(final String url) {
        return Observable.create(new ObservableOnSubscribe<Response>() {
            @Override
            public void subscribe(ObservableEmitter<Response> emitter) throws Exception {
                Response response = AnHttp.get(url).call();
                emitter.onNext(response);
            }
        }).flatMap(new Function<Response, ObservableSource<Response>>() {
            @Override
            public ObservableSource<Response> apply(Response response) throws Exception {
                return Observable.just(response);
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
