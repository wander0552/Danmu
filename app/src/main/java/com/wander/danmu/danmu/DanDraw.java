package com.wander.danmu.danmu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wander.danmu.EntryActivity;
import com.wander.danmu.MainActivity;
import com.wander.danmu.PixelTools;
import com.wander.danmu.R;
import com.wander.danmu.Utils.BitmapTools;
import com.wander.danmu.Utils.CircleImageView;
import com.wander.danmu.Utils.emoji.FaceConversionUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;


/**
 * Created by wander on 2015/11/11.
 * email 805677461@qq.com
 * 弹幕绘制
 */
public class DanDraw extends baseDraw {
    public int HEIGHT;
    private MainActivity context;
    private float step = 15;
    private final Paint paint;
    private Rect rect;
    private int height;
    private Bitmap bitmap;
    private Bitmap tempBitMap;
    private int number;
    private CommentNew commentNew;
    private TextView content;
    private CircleImageView header;
    private View view;

    /**
     * 单个弹幕
     *
     * @param context
     * @param height  弹幕所在位置
     * @param step    弹幕的移动速度
     * @param x       弹幕生成的起始位置
     * @param number  弹幕的编号
     */
    public DanDraw(MainActivity context, int height, int step, int x, int number) {
        this.context = context;
        this.height = height;
        this.number = number;
        initView();
        HEIGHT = PixelTools.dip2px(context, 36);
        rect = EntryActivity.rect;
        this.step = step;
        setX(rect.right + PixelTools.dip2px(context, x));
        setY(height);
        bitmap = BitmapTools.readBitMap(context, R.drawable.header);
        tempBitMap = BitmapTools.readBitMap(context, R.drawable.transparency_bg);
        commentNew = context.getDanmu();
        setDanmu();
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    public void drawView(Canvas canvas) {
        super.drawView(canvas);

        //画背景
        if (bitmap == null || bitmap.isRecycled()) {
            canvas.drawBitmap(tempBitMap, 0, 0, paint);
            setX(getX() - step);
            if (getX() < 0 - tempBitMap.getWidth()) {
                bitmap = BitmapTools.readBitMap(context, R.drawable.header);
                setX(rect.right);
                commentNew = context.getDanmu();
                setDanmu();
            }
        } else {
            canvas.drawBitmap(bitmap, 0, 0, paint);
            setX(getX() - step);
            if (getX() < 0 - bitmap.getWidth()) {
                bitmap = BitmapTools.readBitMap(context, R.drawable.header);
                setX(rect.right);
                commentNew = context.getDanmu();
                setDanmu();
            }
        }
    }

    private void initView() {
        view = LayoutInflater.from(context).inflate(R.layout.item_danmu, null);
        content = (TextView) view.findViewById(R.id.danMu_content);
        header = (CircleImageView) view.findViewById(R.id.danMu_header);
        view.setDrawingCacheEnabled(true);
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                bitmap = view.getDrawingCache();
                context.imageView.setImageBitmap(bitmap);
//                BitmapTools.saveBitmap2file(bitmap, "good", context);
            }
        }
    };

    public void setDanmu() {
        if (commentNew == null) {
            return;
        }
        try {
            content.setText(FaceConversionUtil.getInstace(context).getExpressionString(URLDecoder.decode(commentNew.getContent(), "utf-8")));
            try {
                String decode = URLDecoder.decode(commentNew.getAvatar(), "utf-8");
                ImageLoader.getInstance().loadImage(decode, new ImageLoadingListener() {
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
                        msg.what = 1;
                        handler.sendMessageAtTime(msg, 100);
                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {

                    }
                });

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
