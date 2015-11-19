package com.wander.danmu;

import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.BitmapUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wander.danmu.Utils.BitmapTools;
import com.wander.danmu.Utils.emoji.FaceConversionUtil;
import com.wander.danmu.danmu.CommentNew;
import com.wander.danmu.danmu.DanmuSurface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private DanmuSurface danmuSurface;
    private List<CommentNew> list = new ArrayList<>();
    private RelativeLayout relativeLayout;
    private ImageView imageView;
    private TextView textView;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        relativeLayout = (RelativeLayout) findViewById(R.id.relative);
        danmuSurface = new DanmuSurface(this);
        relativeLayout.addView(danmuSurface);
        initView();


        danmuSurface.setZOrderOnTop(true);
        danmuSurface.getHolder().setFormat(PixelFormat.TRANSLUCENT);
//        danmuSurface.startTimer();
        loadData();

        BitmapUtils bitmapUtils = new BitmapUtils(this);
//        bitmapUtils.getBitmapFileFromDiskCache(imgURL);

        ImageLoader instance = ImageLoader.getInstance();
    }

    void initView(){
        imageView = (ImageView) findViewById(R.id.image);
        textView = (TextView) findViewById(R.id.content);
        textView.setVisibility(View.INVISIBLE);
        textView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessageAtTime(msg, 100);
            }
        });
/*        textView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessageAtTime(msg, 100);
            }

            @Override
            public void onViewDetachedFromWindow(View v) {

            }
        });*/
        textView.setDrawingCacheEnabled(true);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1){
                Bitmap bitmap = textView.getDrawingCache();
                imageView.setImageBitmap(bitmap);
                BitmapTools.saveBitmap2file(bitmap,"good",MainActivity.this);
            }
        }
    };

    void loadData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, "http://nksingserver.kuwo.cn/nks/mobile/comment?act=list&uid=162444908&sid=426615546&wid=104329393&page=1", new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                JSONObject object = null;
                try {
                    object = new JSONObject(s);
                    Gson gson = new Gson();
                    list = gson.fromJson(object.optString("list"), new TypeToken<List<CommentNew>>() {
                    }.getType());
                    DanmuSurface.commentNews.addAll(list);
                    Log.e("list", list.get(0).toString());
                    try {
                        textView.setText(FaceConversionUtil.getInstace(MainActivity.this).getExpressionString(URLDecoder.decode(list.get(3).getContent(), "utf-8")));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(request);
    }

    @Override
    protected void onPause() {
        danmuSurface.stopTimer();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        danmuSurface.stopTimer();
        super.onDestroy();
    }
}
