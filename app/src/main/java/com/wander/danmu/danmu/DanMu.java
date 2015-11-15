package com.wander.danmu.danmu;

import com.wander.danmu.App;

import java.util.Random;

/**
 * Created by wander on 2015/11/12.
 * email 805677461@qq.com
 */
public class DanMu {
    private CommentNew commentNew;
    private float width;
    private float start;
    private float height;

    public void init() {
        setStart();
        setWidth();
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public CommentNew getCommentNew() {
        return commentNew;
    }

    public void setCommentNew(CommentNew commentNew) {
        this.commentNew = commentNew;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth() {
        this.width = 100;
    }

    public float getStart() {
        return start;
    }

    public void setStart() {
        Random random = new Random();
        int nextInt = random.nextInt(50);
        this.start = App.SCREEN_WIDTH + nextInt;
    }
}
