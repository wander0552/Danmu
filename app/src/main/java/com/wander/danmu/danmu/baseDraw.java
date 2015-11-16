package com.wander.danmu.danmu;

import android.graphics.Canvas;

/**
 * Created by wander on 2015/11/16.
 * email 805677461@qq.com
 */
public class baseDraw {
    private float x = 0, y = 0;

    public void draw(Canvas canvas){
        canvas.save();
        canvas.translate(getX(), getY());
        drawView(canvas);
        canvas.restore();
    }
    public void drawView(Canvas canvas) {
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

}
