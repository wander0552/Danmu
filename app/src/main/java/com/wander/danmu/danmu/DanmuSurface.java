package com.wander.danmu.danmu;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

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
    private final DanmuBg danmuBg;
    private String tag = "surfaceView";

    public DanmuSurface(Context context) {
        super(context);
        this.context = context;
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        danmuBg = new DanmuBg(context);
    }

    public void draw() {
        Canvas canvas = getHolder().lockCanvas();
        canvas.drawColor(Color.TRANSPARENT);
        danmuBg.draw(canvas);
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
            timer.schedule(task, 10, 500);
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
