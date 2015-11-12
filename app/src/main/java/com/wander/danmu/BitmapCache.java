package com.wander.danmu;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader;


/**
 * Created by wander on 2015/11/12.
 * email 805677461@qq.com
 */
public class BitmapCache extends LruCache<String,Bitmap> implements ImageLoader.ImageCache{
    /**
     * @param maxSize for caches that do not override {@link #sizeOf}, this is
     *                the maximum number of entries in the cache. For all other caches,
     *                this is the maximum sum of the sizes of the entries in this cache.
     */
    public BitmapCache(int maxSize) {
        super(maxSize);
    }

    @Override
    public Bitmap getBitmap(String s) {
        return null;
    }

    @Override
    public void putBitmap(String s, Bitmap bitmap) {

    }
}
