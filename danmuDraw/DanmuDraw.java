package cn.kuwo.sing.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by wander on 2015/9/17.
 * email 805677461@qq.com
 */

public class DanmuDraw extends SurfaceView implements SurfaceHolder.Callback
{
    public final static LinkedList<SubDataBean> SUBDATATASK = new LinkedList<SubDataBean>(); // 附加数据的静态缓存
    private final int CODE_REFRESHBARRAGE = 0xff10;
    private final int refresh_duration = 180000;
    private SurfaceHolder mHolder;
    private DrawThread drawThread;
    private cn.kuwo.sing.widget.WaterfallTextDrawer mTextDrawer;
    private Handler mHandler;
    private SubdataThread downSubDataThread;
    public DanmuDraw(Context context)
    {
        super(context);
        try {
            init();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public DanmuDraw(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        try {
            init();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void init()
    {
        mHolder = getHolder();
        mHolder.addCallback(this);
        setZOrderOnTop(true);
        mHolder.setFormat(PixelFormat.TRANSPARENT);// 设置背景透明

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg)
            {
                switch (msg.what) {
                    case CODE_REFRESHBARRAGE:
                        if (downSubDataThread == null || !downSubDataThread.isAlive()) {
                            downSubDataThread = new SubdataThread();
                            downSubDataThread.start();
                        }
                        mHandler.sendEmptyMessageDelayed(CODE_REFRESHBARRAGE, refresh_duration);
                        break;

                    default:
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        if (SUBDATATASK == null || SUBDATATASK.size() == 0) {
            downSubDataThread = new SubdataThread();
            downSubDataThread.start();
        }
        mHandler.removeMessages(CODE_REFRESHBARRAGE);
        mHandler.sendEmptyMessageDelayed(CODE_REFRESHBARRAGE, refresh_duration);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        startTextDraw();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        stopTextDrawing();
        mHandler.removeMessages(CODE_REFRESHBARRAGE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        try {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_UP:
                    float x = event.getX();
                    float y = event.getY();
                    DrawBean bean = mTextDrawer.getDrawBeanAtPoint(x, y);
                    if (bean != null) {
                        KzhuoHandleSubplace.handleFunctionMsgClick(getContext(), bean.getSubData());
                    }
                    break;

                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public void startTextDraw()
    {
        stopTextDrawing();

        int surWidth = getWidth();
        int surHeight = getHeight();
        RectF randomSrcRectF = new RectF(0, 0, surWidth, surHeight);

        float waterWidth = getResources().getDimension(R.dimen.watersur_width);
        float waterHeight = waterWidth * 320 / 355.f; // 图片尺寸为355*303，按比例换算;

        RectF targetRectF = new RectF((surWidth - waterWidth) / 2, (surHeight - waterHeight) / 2, (surWidth + waterWidth) / 2, (surHeight + waterHeight) / 2);
        drawThread = new DrawThread(randomSrcRectF, targetRectF);
        drawThread.start();
    }

    public void stopTextDrawing()
    {
        if (drawThread != null && drawThread.isDrawing()) {
            drawThread.stopDrawing();
            drawThread = null;
        }
    }

    public void pause()
    {
        if (drawThread != null) {
            drawThread.pauseDraw();
        }
    }

    public void resume()
    {
        if (drawThread != null) {
            drawThread.resumeDraw();
        }
    }
    public boolean isTextDrawing()
    {
        if (drawThread != null) {
            return drawThread.isDrawing();
        }
        return false;
    }

    private class DrawThread extends Thread
    {
        private AtomicBoolean isRuning = new AtomicBoolean(false);
        private Object lock = new Object();
        private boolean suspendFlag = false;
        public DrawThread(RectF randomSrcRectF, RectF targetRectF)
        {
            super("drawthread");
            if (mTextDrawer != null) {
                mTextDrawer.destory();
            }
            mTextDrawer = new cn.kuwo.sing.widget.WaterfallTextDrawer(getContext(), randomSrcRectF);

        }

        public void pauseDraw()
        {
            suspendFlag = true;
        }

        public void resumeDraw()
        {
            suspendFlag = false;
            synchronized (lock) {
                lock.notifyAll();
            }

        }



        @Override
        public void run()
        {
            try {
                isRuning.set(true);
            } catch (IllegalArgumentException e1) {
                e1.printStackTrace();
            } catch (SecurityException e1) {
                e1.printStackTrace();
            }
            Canvas canvas = null;
            long frameDuration = 0;
            while (isRuning.get()) {

                try {
                    synchronized (lock) {
                        if (suspendFlag) {
                            lock.wait();
                        }
                    }

                    frameDuration = System.currentTimeMillis();
                    canvas = mHolder.lockCanvas();
                    if (canvas == null) {
                        Loger.e("test.chen", " water canvas  === null");
                        try {
                            Thread.sleep(40);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        continue;
                    }
                    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    mTextDrawer.draw(canvas);
                    mHolder.unlockCanvasAndPost(canvas);
                    frameDuration = System.currentTimeMillis() - frameDuration;
                    try {
                        if (16 - frameDuration > 0) {
                            Thread.sleep(16 - frameDuration);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            super.run();
        }

        public void stopDrawing()
        {
            isRuning.set(false);
            if (mTextDrawer != null) {
                mTextDrawer.destory();
            }
        }
        public boolean isDrawing()
        {
            return isRuning.get();
        }
    }

    private class SubdataThread extends Thread
    {
        @Override
        public void run()
        {
            try {
                JSONObject barrageDatas = JSONParserUtil.getBarrageData();
                if (barrageDatas != null && "000".equals(barrageDatas.opt("code")) && mTextDrawer != null) {
                    JSONArray dataList = barrageDatas.optJSONArray("list");
                    if (dataList.length() > 0) {
                        mTextDrawer.cleanSubData();
                    }
                    for (int i = 0; i < dataList.length(); i++) {
                        JSONObject obj = dataList.optJSONObject(i);
                        String showString = obj.optString("showString");
                        String imageUrl = obj.optString("icon");
                        String userId = obj.optString("userId");
                        if (showString != null && showString.length() > 0) {
                            SubDataBean data = new SubDataBean(userId, imageUrl, showString);
                            data.extData = obj;
                            mTextDrawer.addSubShowData(data);
                        }
                    }
                }
                downSubDataThread = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.run();
        }
    }

}
