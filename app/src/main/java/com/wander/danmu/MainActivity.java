package com.wander.danmu;

import android.graphics.PixelFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.BitmapUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wander.danmu.danmu.CommentNew;
import com.wander.danmu.danmu.DanmuBg;
import com.wander.danmu.danmu.DanmuSurface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DanmuSurface danmuSurface;
    private List<CommentNew> list = new ArrayList<>();
    private RelativeLayout relativeLayout;

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


        danmuSurface.setZOrderOnTop(true);
        danmuSurface.getHolder().setFormat(PixelFormat.TRANSLUCENT);
//        danmuSurface.startTimer();
        loadData();

        BitmapUtils bitmapUtils = new BitmapUtils(this);
//        bitmapUtils.getBitmapFileFromDiskCache(imgURL);

        ImageLoader instance = ImageLoader.getInstance();
    }

    void loadData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, "http://nksingserver.kuwo.cn/nks/mobile/comment?act=list&wid=104232590&page=1&levellimit=on", new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                JSONObject object = null;
                try {
                    object = new JSONObject(s);
                    Gson gson = new Gson();
                    list = gson.fromJson(object.optString("list"), new TypeToken<List<CommentNew>>() {
                    }.getType());
                    DanmuBg.addComments(list);
                    Log.e("list", list.get(0).toString());
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

}
