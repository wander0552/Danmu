package com.wander.danmu.danmu;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.wander.danmu.EntryActivity;
import com.wander.danmu.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wander on 2015/11/11.
 * email 805677461@qq.com
 */
public class DanmuSurface extends SurfaceView implements SurfaceHolder.Callback {
    private MainActivity context;
    public static List<CommentNew> commentNews = new ArrayList<>();
    private String tag = "surfaceView";
    private Canvas canvas;
    private float step = 15;
    private Rect rect;
    private DanDraw danmu1;
    private DanDraw danmu2;
    private DanDraw danmu3;
    private DanDraw danmu4;
    private Bitmap bitmap1;
    private Bitmap bitmap2;
    private Bitmap bitmap3;
    private Bitmap bitmap4;
    private final Paint p;
    private PorterDuffXfermode xfermode;
    private PorterDuffXfermode xfermode1;

    public DanmuSurface(MainActivity context) {
        super(context);
        this.context = context;
        rect = EntryActivity.rect;
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        //清屏参数
        p = new Paint();
        xfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
        xfermode1 = new PorterDuffXfermode(PorterDuff.Mode.SRC);

        danmu1 = new DanDraw(context, 0, 7, 50, 1);
        danmu2 = new DanDraw(context, danmu1.HEIGHT + 30, 8, 30, 2);
        danmu3 = new DanDraw(context, (danmu1.HEIGHT + 30) * 2, 5, 0, 3);
        danmu4 = new DanDraw(context, (danmu1.HEIGHT + 30) * 3, 6, 70, 4);
    }

    public void draw() {
        canvas = getHolder().lockCanvas();
        canvas.drawColor(Color.TRANSPARENT);
        //清屏
        p.setXfermode(xfermode);
        canvas.drawPaint(p);
        p.setXfermode(xfermode1);

        //绘制弹幕开始

        danmu1.draw(canvas);
        danmu2.draw(canvas);
        danmu3.draw(canvas);
        danmu4.draw(canvas);

        getHolder().unlockCanvasAndPost(canvas);
    }

    public void setDanmuBitmap(int number,Bitmap bitmap){
        switch (number){
            case 1:
                danmu1.setBitmap(bitmap);
                break;
            case 2:
                danmu2.setBitmap(bitmap);
                break;
            case 3:
                danmu3.setBitmap(bitmap);
                break;
            case 4:
                danmu4.setBitmap(bitmap);
                break;
            default:
                break;
        }

    }

    public Timer timer = null;
    private TimerTask task = null;

    public void startTimer() {
        if (timer == null) {
            timer = new Timer();
            task = new TimerTask() {
                @Override
                public void run() {
                    draw();
                }
            };
            timer.schedule(task, 10, 40);
        }
    }

    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(tag, tag + "\t" + holder.toString());
//        startTimer();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(tag, "surfaceChanged\t" + holder.toString());
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(tag, "surfaceDestroyed\t" + holder.toString());
        stopTimer();
    }
}
