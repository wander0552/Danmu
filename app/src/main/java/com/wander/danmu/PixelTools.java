package com.wander.danmu;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;

import java.net.ContentHandler;

/**
 * 工具类 完成对窗口和视图View的 宽度和高度的处理
 *
 * @author 端晴
 */
public class PixelTools {

    /**
     * 动态添加布局时需要减去的数
     */
    public static int minus = 0;
    static DisplayMetrics metrics = null;

    /**
     * 得到屏幕的高度和宽度
     *
     * @return 以两个元素的数组的形式返回
     */
    public static void getWindowWidthHeight(Activity activities, int[] widHei) {
        if (null == metrics) {
            metrics = new DisplayMetrics();
        }
        activities.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        widHei[0] = metrics.heightPixels;
        widHei[1] = metrics.widthPixels;
    }

    /**
     * 得到屏幕的宽度
     *
     * @return 以两个元素的数组的形式返回
     */
    public static int getWindowWidth(Activity activities) {
        if (null == metrics) {
            metrics = new DisplayMetrics();
        }
        activities.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    /**
     * 获取状态栏高度
     * @param activity
     * @return
     */
    public static Rect getStatusHeight(Activity activity) {
        Rect outRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
        return outRect;

    }


    /**
     * 得到一个view高度
     *
     * @param view
     * @param widHei
     */
    public static int getViewHeight(View view) {
        if (null == view) {
            return 0;
        }
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        return view.getMeasuredHeight();
    }

    /**
     * 得到一个view的宽度，一定要在view的内容设置完成之后调用，否则会测量不准确
     *
     * @param view
     * @param widHei
     */
    public static int getViewWidth(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        return view.getMeasuredWidth();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        try {
            float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        } catch (Exception e) {
            return (int) (dpValue * 1.5);
        }
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue
     * @param scale   （DisplayMetrics类中属性density）
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param fontScale （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param fontScale （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (spValue * scale + 0.5f);
    }

    public static float sp2px_f(Context context, float spValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return spValue * scale + 0.5f;
    }
}
