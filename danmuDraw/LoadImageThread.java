package cn.kuwo.sing.widget;

import android.graphics.Bitmap;
import android.os.*;
import android.os.Process;

import java.net.SocketException;
import java.util.LinkedList;
import java.util.concurrent.TimeoutException;

/**
 * Created by wander on 2015/9/17.
 * email 805677461@qq.com
 */
class LoadImageThread extends Thread {
    private LinkedList<SubDataBean> mPendingUrls; // 等待下载的队列

    public LoadImageThread() {
        mPendingUrls = new LinkedList<SubDataBean>();
    }

    public void addDownLoadTask(SubDataBean data, boolean isFirst) {
        if (isFirst) {
            mPendingUrls.addFirst(data);
        } else {
            mPendingUrls.addLast(data);
        }
    }

    @Override
    public void run() {
        try {
            android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_LOWEST);
        } catch (IllegalArgumentException e1) {
            e1.printStackTrace();
        } catch (SecurityException e1) {
            e1.printStackTrace();
        }

        while (mPendingUrls.size() > 0) {
            try {
                SubDataBean data = mPendingUrls.removeFirst();
                String downUrl = data.subImageUrl;
                boolean exist = ImageLoaderHelper.checkLocalImageByUrl(downUrl);
                if (!exist) {
                    Bitmap bitmap = BitmapUtil.getScaleBitmapFromUrl(downUrl, null, null);
                    ImageLoaderHelper.saveBitmapToSd(downUrl, bitmap);
                    bitmap.recycle();
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof TimeoutException || e instanceof SocketException) {
                    return;
                }
            }
        }
        super.run();
    }

}
