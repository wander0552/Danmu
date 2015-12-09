package com.wander.danmu.danmu;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.wander.danmu.EntryActivity;
import com.wander.danmu.MainActivity;
import com.wander.danmu.PixelTools;
import com.wander.danmu.R;
import com.wander.danmu.Utils.BitmapTools;

import java.util.Random;


/**
 * Created by wander on 2015/11/11.
 * email 805677461@qq.com
 * 弹幕绘制
 */
public class DanDraw extends baseDraw {
    public int HEIGHT;
    private MainActivity context;
    /**
     * 弹幕移动的速度
     */
    private float step = getStep();
    private final Paint paint;
    private Rect rect;
    private int height;
    private Bitmap bitmap;
    private int number;
    private boolean isLoading;
    private boolean hasNew;

    /**
     * 单个弹幕
     *
     * @param context
     * @param height  弹幕所在位置
     * @param x       弹幕生成的起始位置
     * @param number  弹幕的编号
     */
    public DanDraw(MainActivity context, int height, int x, int number) {
        this.context = context;
        this.height = height;
        this.number = number;
        HEIGHT = PixelTools.dip2px(context, 36);
        rect = EntryActivity.rect;
        setX(rect.right + PixelTools.dip2px(context, x));
        setY(height);
        bitmap = BitmapTools.readBitMap(context, R.drawable.transparency_bg);
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    public void drawView(Canvas canvas) {
        super.drawView(canvas);

        //画背景
        if (bitmap == null || bitmap.isRecycled()) {
//            canvas.drawBitmap(tempBitMap, 0, 0, paint);
            setX(rect.right);
            hasNew = context.getDanmu(number);
        } else {
            if (!isLoading){
                canvas.drawBitmap(bitmap, 0, 0, paint);
                setX(getX() - step);
            }
            if (getX() < 0 - bitmap.getWidth()) {
                bitmap = BitmapTools.readBitMap(context, R.drawable.transparency_bg);
                isLoading = true;
                setX(rect.right);
                step = getStep();
                hasNew = context.getDanmu(number);
            }
            if (!hasNew){
                hasNew =context.getDanmu(number);
            }
        }
    }


    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        isLoading = false;
        this.bitmap = bitmap;
    }

    int getStep(){
        return  new Random().nextInt(3)+4;
    }
}
