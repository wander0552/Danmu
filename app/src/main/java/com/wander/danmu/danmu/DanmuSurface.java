package com.wander.danmu.danmu;


import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wander on 2015/11/11.
 * email 805677461@qq.com
 */
public class DanmuSurface extends SurfaceView implements SurfaceHolder.Callback {
    private Context context;
    public static List<CommentNew> commentNews = new ArrayList<>();
    private final DanMuDraw danmu1;
    private String tag = "surfaceView";
    private Canvas canvas;
    private float step = 15;
    private Rect rect;
    private final DanMuDraw danmu2;
    private final DanMuDraw danmu3;

    public DanmuSurface(Context context) {
        super(context);
        this.context = context;
        rect = EntryActivity.rect;
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        danmu1 = new DanMuDraw(context, 0, 15);
        danmu2 = new DanMuDraw(context, danmu1.HEIGHT + 30, 10);
        danmu3 = new DanMuDraw(context, (danmu1.HEIGHT + 30) * 2, 20);
    }

    public void draw() {
        canvas = getHolder().lockCanvas();
        canvas.drawColor(Color.TRANSPARENT);
        Paint p = new Paint();
        //清屏
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawPaint(p);
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));

        //绘制弹幕开始

        danmu1.draw(canvas);
        danmu2.draw(canvas);
        danmu3.draw(canvas);

        getHolder().unlockCanvasAndPost(canvas);
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
            timer.schedule(task, 10, 200);
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
        startTimer();
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
