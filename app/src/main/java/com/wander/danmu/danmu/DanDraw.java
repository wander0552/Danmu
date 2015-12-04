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
    private int tempStep;
    private boolean isLoading;
    private boolean hasNew;

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
        HEIGHT = PixelTools.dip2px(context, 36);
        rect = EntryActivity.rect;
        this.step = step;
        tempStep = step;
        setX(rect.right + PixelTools.dip2px(context, x));
        setY(height);
        bitmap = BitmapTools.readBitMap(context, R.drawable.transparency_bg);
        tempBitMap = BitmapTools.readBitMap(context, R.drawable.transparency_bg);
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
            context.getDanmu(number);
        } else {
            canvas.drawBitmap(bitmap, 0, 0, paint);
            setX(getX() - step);
            if (isLoading){
                setX(rect.right);
            }
            if (getX() < 0 - bitmap.getWidth()) {
                bitmap = BitmapTools.readBitMap(context, R.drawable.transparency_bg);
                isLoading = true;
                setX(rect.right);
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
}
