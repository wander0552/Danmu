package com.wander.danmu.danmu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;

import com.wander.danmu.App;
import com.wander.danmu.PixelTools;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by wander on 2015/11/11.
 * email 805677461@qq.com
 * 弹幕绘制和数据处理
 */
public class DanmuBg extends Contanier {
    private final float HEIGHT = 36;
    public static List<CommentNew> commentNews = new ArrayList<>();
    private List<DanMu> danMuList;
    private Context context;
    private float step = 15;
    private final Paint paint;

    public DanmuBg(Context context) {
        this.context = context;
        danMuList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            DanMu danMu = new DanMu();
            danMuList.add(danMu);
        }
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(0x88000000);
        paint.setStrokeWidth(0.3f);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    public void childrenView(Canvas canvas) {
        super.childrenView(canvas);

        RectF rectF = new RectF(0, 0, PixelTools.dip2px(context,200), PixelTools.dip2px(context, HEIGHT));
        canvas.drawRoundRect(rectF, HEIGHT / 2, HEIGHT / 2, paint);
        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.RED);
        textPaint.setTextSize(PixelTools.sp2px(context, 50));
        canvas.drawText("text赞一个", 200, 200, textPaint);

        setX(getX() + step);
    }

    public static void addComment(CommentNew commentNew){
        commentNews.add(commentNew);
    }

    public static void addComments(List<CommentNew> commentNews){
        commentNews.addAll(commentNews);
    }
}
