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
import android.text.TextPaint;

import com.wander.danmu.EntryActivity;
import com.wander.danmu.PixelTools;
import com.wander.danmu.R;


/**
 * Created by wander on 2015/11/11.
 * email 805677461@qq.com
 * 弹幕绘制
 */
public class DanDraw extends baseDraw {
    public int HEIGHT;
    private Context context;
    private float step = 15;
    private final Paint paint;
    private TextPaint textPaint;
    private Rect rect;
    private int height;
    private Bitmap header;
    private Paint headerPaint;
    private RectF rectF;
    private RectF headerRectF;
    private int danmuWidth;

    public DanDraw(Context context, int height, int step, int x) {
        this.context = context;
        this.height = height;
        HEIGHT = PixelTools.dip2px(context, 36);
        rect = EntryActivity.rect;
        this.step = step;
        setX(rect.right + PixelTools.dip2px(context, x));
        setY(height);
        header = BitmapFactory.decodeResource(context.getResources(), R.drawable.header);

        paint = new Paint();
        paint.setAntiAlias(true);
//        paint.setColor(0x55000000);
//        paint.setStrokeWidth(0.3f);
//        paint.setStyle(Paint.Style.FILL_AND_STROKE);

     /*   textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(PixelTools.sp2px(context, 14));

        BitmapShader bitmapShader = new BitmapShader(header, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        headerPaint = new Paint();
        headerPaint.setAntiAlias(true);
        headerPaint.setShader(bitmapShader);

        rectF = new RectF(0, 0, PixelTools.dip2px(context, 200), HEIGHT);
        headerRectF = new RectF(0, 0, PixelTools.dip2px(context, 36), HEIGHT);
        danmuWidth = PixelTools.dip2px(context, 200);*/
    }

    @Override
    public void drawView(Canvas canvas) {
        super.drawView(canvas);

        //画背景
        canvas.drawBitmap(header, 0, 0, paint);
//        canvas.drawRoundRect(rectF, HEIGHT / 2, HEIGHT / 2, paint);
        //画头像

//        canvas.drawCircle(0, rect.top, HEIGHT / 2, headerPaint);
//        canvas.drawRoundRect(headerRectF, HEIGHT / 2, HEIGHT / 2, headerPaint);

//        canvas.drawText("text赞一个", HEIGHT, rect.top, textPaint);

        setX(getX() - step);
        if (getX() < 0 - header.getWidth()) {
            setX(rect.right);
        }
    }
}
