package com.wander.danmu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wander.danmu.Utils.BitmapTools;
import com.wander.danmu.Utils.CircleImageView;
import com.wander.danmu.Utils.StringUtil;
import com.wander.danmu.Utils.emoji.FaceConversionUtil;
import com.wander.danmu.danmu.CommentNew;
import com.wander.danmu.danmu.DanmuSurface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DanmuSurface danmuSurface;
    private List<CommentNew> list = new ArrayList<>();
    private RelativeLayout relativeLayout;
    public ImageView imageView;
    private TextView textView;
    private RelativeLayout shot;
    private CircleImageView header;
    private int position;
    private int numPager = 1;
    private boolean needLoadMore = true;
    private boolean isLoading;
    private LinearLayout danmu1, danmu2, danmu3, danmu4;
    private CommentNew tempComment;


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

//        danmuSurface.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, Main2Activity.class));
//            }
//        });
        initView();


        danmuSurface.setZOrderOnTop(true);
        danmuSurface.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        loadData();


    }


    void initView() {
        imageView = (ImageView) findViewById(R.id.image);
        shot = (RelativeLayout) findViewById(R.id.play_common_rl);
        textView = (TextView) findViewById(R.id.play_common_text_content);
        header = (CircleImageView) findViewById(R.id.play_common_img_usericon);
        shot.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
//                Message msg = new Message();
//                msg.what = 1;
//                handler.sendMessageAtTime(msg, 100);
            }
        });
        shot.setDrawingCacheEnabled(true);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.add(position + 1, tempComment);
            }
        });

        danmu1 = (LinearLayout) findViewById(R.id.danMu1);
        danmu2 = (LinearLayout) findViewById(R.id.danMu2);
        danmu3 = (LinearLayout) findViewById(R.id.danMu3);
        danmu4 = (LinearLayout) findViewById(R.id.danMu4);
        danmu1.setDrawingCacheEnabled(true);
        danmu2.setDrawingCacheEnabled(true);
        danmu3.setDrawingCacheEnabled(true);
        danmu4.setDrawingCacheEnabled(true);

    }

    public boolean getDanmu(int number) {
        synchronized (list) {
            position++;
            if (position >= list.size() && needLoadMore && !isLoading) {
                isLoading = true;
                numPager++;
                loadData();
            }
            if (list != null && list.size() <= 0) {
                return false;
            }
            if (position >= list.size()) {
                position--;
                return false;
            }
            CommentNew commentNew = list.get(position);

            switch (number) {
                case 1:
                    getBitmap(danmu1, commentNew, number);
                    break;
                case 2:
                    getBitmap(danmu2, commentNew, number);
                    break;
                case 3:
                    getBitmap(danmu3, commentNew, number);
                    break;
                case 4:
                    getBitmap(danmu4, commentNew, number);
                    break;
                default:
                    break;
            }
        }
        return true;
    }

    private void getBitmap(final LinearLayout view, final CommentNew commentNew, final int number) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView content0 = (TextView) view.findViewById(R.id.danMu_content);
                ImageView header0 = (ImageView) view.findViewById(R.id.danMu_header);
                try {
                    content0.setText(FaceConversionUtil.getInstace(MainActivity.this).getExpressionString(URLDecoder.decode(commentNew.getContent(), "utf-8")));
                    String decode = URLDecoder.decode(commentNew.getAvatar(), "utf-8");
                    ImageLoader.getInstance().displayImage(StringUtil.smartPicUrl(decode, 's', commentNew.getUid()), header0, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String s, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String s, View view, FailReason failReason) {
                            Message msg = new Message();
                            msg.what = number;
                            handler.sendMessageAtTime(msg, 100);
                        }

                        @Override
                        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                            Message msg = new Message();
                            msg.what = number;
                            handler.sendMessageAtTime(msg, 100);
                        }

                        @Override
                        public void onLoadingCancelled(String s, View view) {

                        }
                    });
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
/*            if (msg.what == 1) {
                Bitmap bitmap = shot.getDrawingCache();
                imageView.setImageBitmap(bitmap);
//                BitmapTools.saveBitmap2file(bitmap, "good", MainActivity.this);
            }*/
            switch (msg.what) {
                case 0:
                    Bitmap bitmap = shot.getDrawingCache(true);
                    imageView.setImageBitmap(bitmap);
                    tempComment.setContent("我新加了一个评论");
                    break;
                case 1:
                    Bitmap drawingCache1 = danmu1.getDrawingCache();
                    danmuSurface.setDanmuBitmap(1, drawingCache1);
                    break;
                case 2:
                    Bitmap drawingCache2 = danmu2.getDrawingCache();
                    danmuSurface.setDanmuBitmap(2, drawingCache2);
                    break;
                case 3:
                    Bitmap drawingCache3 = danmu3.getDrawingCache();
                    danmuSurface.setDanmuBitmap(3, drawingCache3);
                    break;
                case 4:
                    Bitmap drawingCache4 = danmu4.getDrawingCache();
                    danmuSurface.setDanmuBitmap(4, drawingCache4);
                    break;
                default:
                    break;
            }

        }
    };

    void loadData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, "http://nksingserver.kuwo.cn/nks/mobile/comment?act=list&uid=162444908&sid=426615546&wid=104329393&page=" + numPager, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                JSONObject object = null;
                try {
                    if (TextUtils.isEmpty(s)) {
                        return;
                    }
                    object = new JSONObject(s);
                    Gson gson = new Gson();
                    if (numPager == 1) {
                        list = gson.fromJson(object.optString("list"), new TypeToken<List<CommentNew>>() {
                        }.getType());
                        if (list.size() < 20) {
                            needLoadMore = false;
                        }
                        if (list.size() > 0) {
                            danmuSurface.startTimer();
                        }
                    } else {
                        List<CommentNew> commentNews = gson.fromJson(object.optString("list"), new TypeToken<List<CommentNew>>() {
                        }.getType());
                        if (commentNews.size() < 20) {
                            needLoadMore = false;
                        }
                        list.addAll(commentNews);
                        isLoading = false;
                    }
//// TODO: 2015/12/4 text
                    Log.e("list", numPager + "\t" + list.get(0).toString());
                    try {
                        tempComment = list.get(3);
                        textView.setText(FaceConversionUtil.getInstace(MainActivity.this).getExpressionString(URLDecoder.decode(tempComment.getContent(), "utf-8")));
                        ImageLoader.getInstance().displayImage(URLDecoder.decode(tempComment.getAvatar(), "utf-8"), header, new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String s, View view) {

                            }

                            @Override
                            public void onLoadingFailed(String s, View view, FailReason failReason) {

                            }

                            @Override
                            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                                header.setImageBitmap(bitmap);
                                Message msg = new Message();
                                msg.what = 0;
                                handler.sendMessageAtTime(msg, 100);
                            }

                            @Override
                            public void onLoadingCancelled(String s, View view) {

                            }
                        });
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
