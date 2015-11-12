package com.wander.danmu.danmu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

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
    private List<CommentNew> commentNews;
    private List<DanMu> danMuList;
    private Context context;
    private float step = 15;
    private final Paint paint;

    public DanmuBg(List<CommentNew> commentNews, Context context) {
        this.commentNews = commentNews;
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

        RectF rectF = new RectF(0, 0, 100, PixelTools.dip2px(context, HEIGHT));
        canvas.drawRoundRect(rectF, HEIGHT / 2, HEIGHT / 2, paint);
        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(14);
        canvas.drawText(danMuList.get(0).getCommentNew().getContent(), App.SCREEN_WIDTH, 0, textPaint);

        setX(getX() + step);
    }

    public void addComment(CommentNew commentNew){
        commentNews.add(commentNew);
    }
}
