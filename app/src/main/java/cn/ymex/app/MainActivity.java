package cn.ymex.app;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.ymex.anhttp.parser.gson.ParserGson;
import cn.ymex.net.AnHttp;
import cn.ymex.net.Response;
import cn.ymex.net.callback.ResponseCallback;
import cn.ymex.net.exception.NetException;

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
        AnHttp.instance().setParser(new ParserGson());
    }

    private void requestHistory() {
        String url = "http://gank.io/api/history/content/2/1";
        AnHttp.get(url).call(new ResponseCallback<BaseModel<List<ResultsBean>>>(){
            @Override
            public void onResult(BaseModel<List<ResultsBean>> result, Response.Status status) {
                if (status.isSuccessful()) {
                    tvContent.setText(result.getResults().get(0).getContent());
                }
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                System.out.println("---------------"+throwable.getLocalizedMessage());
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
