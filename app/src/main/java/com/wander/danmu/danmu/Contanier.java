package com.wander.danmu.danmu;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

public class Contanier {

    private List<Contanier> children = null;
    private float x = 0, y = 0;

    public Contanier() {
        children = new ArrayList<Contanier>();
    }

    public void draw(Canvas canvas) {
        canvas.save();
        canvas.translate(getX(), getY());
        childrenView(canvas);
        for (Contanier c : children) {
            c.draw(canvas);
        }
        canvas.restore();
    }

    public void childrenView(Canvas canvas) {
    }

    public void addChildrenView(Contanier child) {
        children.add(child);
    }

    public void removeChildrenView(Contanier child) {
        children.remove(child);
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
