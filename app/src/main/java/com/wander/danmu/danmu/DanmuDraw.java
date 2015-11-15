package com.wander.danmu.danmu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;

import com.wander.danmu.EntryActivity;
import com.wander.danmu.PixelTools;


/**
 * Created by wander on 2015/11/11.
 * email 805677461@qq.com
 * 弹幕绘制
 */
public class DanmuDraw extends Contanier {
    public int HEIGHT;
    private Context context;
    private float step = 15;
    private final Paint paint;
    private TextPaint textPaint;
    private Rect rect;
    private int height;

    public DanmuDraw(Context context, int height, int step) {
        this.context = context;
        this.height = height;
        HEIGHT = PixelTools.dip2px(context, 36);
        rect = EntryActivity.rect;
        this.step = step;
        setX(rect.right + PixelTools.dip2px(context, 200));
        setY(height);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(0x55000000);
        paint.setStrokeWidth(0.3f);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(PixelTools.sp2px(context, 14));
    }

    @Override
    public void childrenView(Canvas canvas) {
        super.childrenView(canvas);

        RectF rectF = new RectF(0, 0, PixelTools.dip2px(context, 200), HEIGHT);
        canvas.drawRoundRect(rectF, HEIGHT / 2, HEIGHT / 2, paint);
        canvas.drawText("text赞一个", HEIGHT, rect.top, textPaint);

        setX(getX() - step);
        if (getX() < 0 - PixelTools.dip2px(context, 200)) {
            setX(rect.right);
        }
    }
}
