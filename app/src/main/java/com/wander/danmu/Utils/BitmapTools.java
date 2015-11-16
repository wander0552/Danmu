package com.wander.danmu.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

import com.wander.danmu.Utils.dir.DirectoryContext;
import com.wander.danmu.Utils.dir.DirectoryManager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BitmapTools {
	
	/**
	 * 图片圆角处理
	 * @param bitmap
	 * @param pixels
	 * @return
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242; 
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels; 
		paint.setAntiAlias(true); 
		canvas.drawARGB(0, 0, 0, 0); 
		paint.setColor(color); 
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint); 
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint); 
		return output; 
		} 
	
	public static Bitmap readBitmapAutoSize(String filePath, int outWidth,
			int outHeight) {
		// outWidth和outHeight是目标图片的最大宽度和高度，用作限制
		FileInputStream fs = null;
		BufferedInputStream bs = null;
		try {
			fs = new FileInputStream(filePath);
			bs = new BufferedInputStream(fs);
			BitmapFactory.Options options = setBitmapOption(filePath, outWidth,
					outHeight);
			return BitmapFactory.decodeStream(bs, null, options);
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
		}finally {
			try {
				bs.close();
				fs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static BitmapFactory.Options setBitmapOption(String filePath,
			int width, int height) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inJustDecodeBounds = true;
		// 设置只是解码图片的边距，此操作目的是度量图片的实际宽度和高度
		BitmapFactory.decodeFile(filePath, opt);
		int outWidth = opt.outWidth; // 获得图片的实际高和宽
		int outHeight = opt.outHeight;
		opt.inDither = false;
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		// 设置加载图片的颜色数为16bit，默认是RGB_8888，表示24bit颜色和透明通道，但一般用不上
		opt.inSampleSize = 1;
		// 设置缩放比,1表示原比例，2表示原来的四分之一....
		// 计算缩放比
		if (outWidth != 0 && outHeight != 0 && width != 0 && height != 0) {
			int sampleSize = (outWidth / width + outHeight / height) / 2;
			opt.inSampleSize = sampleSize;
		}
		opt.inJustDecodeBounds = false;// 最后把标志复原
		return opt;
	}

	public static BitmapFactory.Options getBitmapOptions(int width, int height) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inJustDecodeBounds = true;
		int outWidth = opt.outWidth; // 获得图片的实际高和宽
		int outHeight = opt.outHeight;
		opt.inDither = false;
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		// 设置加载图片的颜色数为16bit，默认是RGB_8888，表示24bit颜色和透明通道，但一般用不上
		opt.inSampleSize = 1;
		// 设置缩放比,1表示原比例，2表示原来的四分之一....
		// 计算缩放比
		if (outWidth != 0 && outHeight != 0 && width != 0 && height != 0) {
			int sampleSize = (outWidth / width + outHeight / height) / 2;
			opt.inSampleSize = sampleSize;
		}

		opt.inJustDecodeBounds = false;// 最后把标志复原
		return opt;
	}

	public static Bitmap createBitmapByInputstream(Context context, int res,
			int width, int height) {
		InputStream is = context.getResources().openRawResource(res);
		BitmapFactory.Options opts = getBitmapOptions(width, height);
		return BitmapFactory.decodeStream(is, null, opts);
	}

	/**
	 * 保存bitmap到文件
	 *
	 * @param bmp
	 * @param filePath
	 * @return
	 */
	public static boolean saveBitmap2file(Bitmap bmp, String filePath) {
		CompressFormat format = Bitmap.CompressFormat.JPEG;
		int quality = 100;
		OutputStream stream = null;
		try {
			stream = new FileOutputStream(filePath);
			bmp.compress(format, quality, stream);
			stream.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 获取和保存当前屏幕的截图
	 */
	public static String saveCurrentScreen2Image(Activity context,
			String fileName) {
		// Bitmap bmp =
		// Bitmap.createBitmap(AppContext.SCREEN_WIDTH,AppContext.SCREEN_HIGHT,
		// Config.ARGB_8888 );
		// //获取屏幕
		String filePath = DirectoryManager.getDirPath(DirectoryContext.CACHE)
				+ File.separator + fileName + ".jpg";
		File tempFile = new File(filePath);
		if (tempFile.exists()) {
			tempFile.delete();
		}
		View decorview = context.getWindow().getDecorView();
		decorview.setDrawingCacheEnabled(true);
		Bitmap bmp = decorview.getDrawingCache();
		saveBitmap2file(bmp, filePath);
		return filePath;
	}

	/**
	 * 对图片进行裁剪，从上向下
	 *
	 * @author 王端晴
	 * @param source 原图片
	 * @return 对原图片进行裁剪，将裁剪好的图片返回，原图片不复存在
	 */
	public static Bitmap cropBitmap(Bitmap source) {
		int width = source.getWidth();
		int height = source.getHeight();
//		Bitmap bitmap = null;
//		if (width > Constants.USER_HEAD_PIC_WIDTH_MAX) {
//			float percent = width / Constants.USER_HEAD_PIC_WIDTH_MAX;
//			bitmap = scaleBitmap(source, (int) (width / percent), (int)(height / percent));
//		}
//
//		if (null != bitmap) {
//			width = bitmap.getWidth();
//			height = bitmap.getHeight();
//		}else {
//			bitmap = source;
//		}
		if (height >= width * 3 / 5) {
			height = width * 3 / 5;
		}else {
			return source;
		}
		if (height <= 0 || width <= 0) {
			return source;
		}
		source = Bitmap.createBitmap(source, 0, 0, width, height, null, false);
		return source;
	}

	/**
	 * 对图片进行裁剪，裁剪中间，从上向下
	 *
	 * @author 王端晴
	 * @param source
	 * @return
	 */
	public static Bitmap cropBitmapCenter(Bitmap source) {
		int width = source.getWidth();
		int height = source.getHeight();
		int from = 0;
		if (width > height) {
			from = (width - height) / 2;
			Bitmap bitmap = Bitmap.createBitmap(source, from, 0, height, height, null, false);
			return bitmap;
		}else {
			return source;
		}
	}

	/**
	 * 节省内容的方式，从本地读取图片
	 *
	 * @author 王端晴
	 * @param source
	 * @return
	 */
	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	// /**
	// * 节省内容的方式，从本地读取图片
	// *
	// * @author 王端晴
	// * @param source
	// * @return
	// */
	// public static Bitmap readBitMapDirectly(Context context, int resId) {
	// InputStream is = context.getResources().openRawResource(resId);
	// return BitmapFactory.decodeStream(is);
	// }

	
	/**
	 * 縮放Bitmap
	 * @param srcBitmap 元bitmap
	 * @param maxWidth	最大寬度
	 * @param maxHeight 最大高度
	 * @return
	 */
	public static Bitmap scaleBitmap(Bitmap srcBitmap, int maxWidth,
			int maxHeight) {
		if (srcBitmap == null) {
			return null;
		}
		int srcWidth = srcBitmap.getWidth();
		if (srcWidth > maxWidth) {
			srcBitmap = Bitmap.createScaledBitmap(srcBitmap, maxWidth, maxHeight, false);
			if (srcBitmap != null) {
				return srcBitmap;
			}
		}
		return srcBitmap;
	}
	
    public static void recycleBitmap(Bitmap bmp){
    	if(bmp != null && !bmp.isRecycled()){
    		bmp.recycle();
    	}
    }
}
