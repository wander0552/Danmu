package cn.kuwo.sing.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;


import java.util.ArrayList;
import java.util.Random;

/**
 *
 * <pre>
 * 业务名:
 * 功能说明: 画随机文本的功能类
 * 编写日期:	2014-9-24
 * 作者:	ChenShuai
 *
 * </pre>
 */
public class WaterfallTextDrawer
{
	private ArrayList<DrawBean> mDrawingBeanList;
	/**
	 * 记录将要出站的drawbean（还在屏幕边上,已经出现但还没完全离开屏幕）；
	 */
	private ArrayList<DrawBean> mReadtoOutList;
	private ArrayList<DrawBean> mDrawDoneList;
	private int minDrawbeanNum = 3;
	private int maxDrawbeanNum = 5;
	private float mDensity;
	private Paint mTextPaint;
	private int defaultItemHeight = 35;
	private float defualtSpeed = 1.5f;
	private SubMirrorView mMirrorView;
	private Random random;
	private LoadImageThread mLoadingImageThread;
	/**
	 * 初始化数据体的线程， 避免在主线程中初始化造成的卡顿
	 */
	private HandlerThread initDrawBeanThread;
	private Handler initDrawBeanHandler;
	private boolean isAdding;
	private RectF mSrcRectf;
	public WaterfallTextDrawer(Context context, RectF radomSrcRectF)
	{
		mDensity = context.getResources().getDisplayMetrics().density;
		defualtSpeed = defualtSpeed * mDensity;
		defaultItemHeight = (int) context.getResources().getDimension(R.dimen.barrage_avatar_size);

		mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mTextPaint.setTextSize(15 * mDensity);
		mTextPaint.setColor(Color.WHITE);
		mSrcRectf = radomSrcRectF;

		mMirrorView = new SubMirrorView(context);

		random = new Random();

		mDrawingBeanList = new ArrayList<DrawBean>();
		mDrawDoneList = new ArrayList<DrawBean>();
		mReadtoOutList = new ArrayList<DrawBean>();
		initDrawBeanThread = new HandlerThread("drawbean", Process.THREAD_PRIORITY_BACKGROUND);
		initDrawBeanThread.start();
		initDrawBeanHandler = new Handler(initDrawBeanThread.getLooper()) {
			@Override
			public void handleMessage(Message msg)
			{
				try {
					isAdding = true;
					addNewDrawBean(msg.what);
					isAdding = false;
				} catch (Exception e) {
					e.printStackTrace();
				}
				super.handleMessage(msg);
			}
		};

		addNewDrawBean(2);

	}

	public void draw(Canvas canvas)
	{
		try {
			boolean hasDones = mDrawingBeanList.size() < minDrawbeanNum;
			DrawBean drawBean = null;

			// 画还没画完的path，并将画完的添加到doneList中
			for (int i = 0; i < mDrawingBeanList.size();) { // the best for code
				drawBean = mDrawingBeanList.get(i);
				if (!drawBean.isDone()) {
					canvas.drawBitmap(drawBean.getDrawData(), drawBean.getXLocation(), drawBean.getYLocation(), drawBean.getDrawPaint());
					if (drawBean.isOuted()) {
						hasDones = mReadtoOutList.remove(drawBean);
					}
					drawBean.moveToNextLocation();
					i++;
				} else {
					hasDones = true;
					mDrawingBeanList.remove(drawBean);
					mDrawDoneList.add(drawBean);
					WaterTextSurfaceView.SUBDATATASK.addLast(drawBean.getSubData());
				}
			}
			if (hasDones && !isAdding && mDrawingBeanList.size() < maxDrawbeanNum) { // 如果有进行完的则进行随机添加，
				int what = maxDrawbeanNum - mDrawingBeanList.size() - mReadtoOutList.size();
				if (what > 0) {
					initDrawBeanHandler.sendEmptyMessage(what);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public RectF getSrcRectf()
	{
		return mSrcRectf;
	}
	private void addNewDrawBean(int maxAvalable)
	{

		try {
			if (WaterTextSurfaceView.SUBDATATASK.size() == 0) {
				return;
			}
			ArrayList<int[]>  reduceSpaces  = new ArrayList<int[]>();
			for (int i = 0; i < mReadtoOutList.size(); i++) {
				DrawBean  drawb = mReadtoOutList.get(i);
				int y = (int) drawb.getYLocation();
				reduceSpaces.add(new int[] { y, y + drawb.getHeigth() });
			}
			ArrayList<int[]> avalableSections = new ArrayList<int[]>();
			int[]  srcSpace = new int[]{(int) mSrcRectf.top,(int) mSrcRectf.bottom};
			AlgorithmUtils.waterfallSpace(srcSpace, reduceSpaces, defaultItemHeight, avalableSections);
			int addedNum = 0;
			while (avalableSections.size() > 0 && addedNum < maxAvalable && mDrawingBeanList.size() < maxDrawbeanNum) {
				int[] section = avalableSections.remove(0);
//				int yStart = (int) (section[0] + (section[1] - section[0] - defaultItemHeight) *random.nextFloat());
				int yStart = section[0];
				if (yStart <= srcSpace[0]) {
					yStart = srcSpace[0];
				}
				if (yStart + defaultItemHeight > srcSpace[1]) {
					yStart = srcSpace[1] - (defaultItemHeight + yStart - srcSpace[1]) - defaultItemHeight;
				}
				float randScale = random.nextFloat() / 2.0f + 1;
				float xStartOffset = 0;
				DrawBean drawBean = null;
				SubDataBean subData = WaterTextSurfaceView.SUBDATATASK.pollFirst();
				if (subData == null) {
					return;
				}
				if (mDrawDoneList.size() > 0) {
					drawBean = mDrawDoneList.remove(0);
					drawBean.setDrawParams(mSrcRectf.right, xStartOffset, yStart, mSrcRectf.left, yStart, defualtSpeed * randScale, mTextPaint, subData);
				}else {
					drawBean = new DrawBean(mSrcRectf.right, xStartOffset, yStart, mSrcRectf.left, yStart, defualtSpeed * randScale, mTextPaint, subData);
				}

				mDrawingBeanList.add(drawBean);
				mReadtoOutList.add(drawBean);
				addedNum++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 *
	 * 方法说明：添加附加显示数据， 附加数据不足时会自动随机创建一些数字显示
	 *
	 * @param data
	 */
	public void addSubShowData(SubDataBean data)
	{
		try {
			WaterTextSurfaceView.SUBDATATASK.addFirst(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public DrawBean getDrawBeanAtPoint(float x, float y)
	{
		for (DrawBean bean : mDrawingBeanList) {
			if (bean.containsPoint(x, y) && !bean.isDone()) {
				return bean;
			}
		}
		return null;
	}

	public void cleanSubData()
	{
		try {
			WaterTextSurfaceView.SUBDATATASK.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void destory()
	{
		try {
			if (initDrawBeanThread != null) {
				initDrawBeanThread.quit();
				initDrawBeanThread = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * <pre>
	 * 业务名:  y = ax+b
	 * 功能说明:
	 * 编写日期:	2014-9-25
	 * 作者:	ChenShuai
	 *
	 * </pre>
	 */
	public class DrawBean
	{
		private float xDrawingPosition;
		private float yDrawingPosition;
		private float xTargetPosition;
		private float xStartPosition;
		private float xAbsStep;
		private float xTotalDistance;
		private SubDataBean subData;
		private Bitmap drawBit; // 需要画的内容
		private Paint textDrawPaint;

		public DrawBean(float xStart, float xStartOffset, float yStart, float xTarget, float yTarget, float absStep, Paint textPaint, SubDataBean drawData)
		{
			super();
			setDrawParams(xStart, xStartOffset, yStart, xTarget, yTarget, absStep, textPaint, drawData);
		}

		public void setDrawParams(float xStart, float xStartOffset, float yStart, float xTarget, float yTarget, float absStep, Paint textPaint, SubDataBean subDatas)
		{
			textDrawPaint = textPaint;
			this.subData = subDatas;
			// 获取缓存的图像
			drawBit = BitmapCacheInstance.getInstance().getBitmapCache(subData.getHashKey());

			if (drawBit == null || drawBit.isRecycled()) {
				mMirrorView.reset();
				String htmlText = subData.subString;
				if (htmlText != null && htmlText.length() > 0) {
					mMirrorView.setHtmlText(htmlText);
				}
				if (subData.subImageUrl != null && subData.subImageUrl.length() > 0) {
					Bitmap avatarBit = getImageFromCache(subData);
					if (avatarBit != null && !avatarBit.isRecycled()) {
						mMirrorView.setImage(avatarBit);
						drawBit = mMirrorView.getDrawMirror();
						BitmapCacheInstance.getInstance().addBitmapCache(subData.getHashKey(), drawBit); // 将加入文本和图片的view，（同时含有文本和头像的数据只有在头像加载成功的情况下才会缓存）
						avatarBit.recycle();
						avatarBit = null;
					} else {
						drawBit = mMirrorView.getDrawMirror();
					}
				} else {
					drawBit = mMirrorView.getDrawMirror();
					BitmapCacheInstance.getInstance().addBitmapCache(subData.getHashKey(), drawBit); // 将只含有文本的view图像缓存
				}

			} else {
				// Loger.d("test.chen", "HasMirror cache ====" + subData.subString);
			}


			xTargetPosition = xTarget - drawBit.getWidth();
			xTotalDistance = xTarget - xStart;
			xStartPosition = xStart;
			xDrawingPosition = xStart + xStartOffset;
			yDrawingPosition = yStart;

			xAbsStep = absStep; // x周的变化速度
		}

		public Bitmap getDrawData()
		{
			if (drawBit == null || drawBit.isRecycled()) {
				if (mMirrorView != null) {
					drawBit = mMirrorView.getDrawMirror();
					BitmapCacheInstance.getInstance().addBitmapCache(subData.getHashKey(), drawBit);
				}
			}
			return drawBit;
		}

		public SubDataBean getSubData()
		{
			return subData;
		}

		public Paint getDrawPaint()
		{
			return textDrawPaint;
		}

		public void moveToNextLocation()
		{
			xDrawingPosition -= xAbsStep;
		}

		public float getXLocation()
		{
			return xDrawingPosition;
		}

		public float getYLocation()
		{
			return yDrawingPosition;
		}

		public int getHeigth()
		{
			return drawBit.getHeight();
		}

		public int getWidth()
		{
			return drawBit.getWidth();
		}

		public boolean isDone()
		{
			return xDrawingPosition <= xTargetPosition;
		}

		/**
		 *
		 * 方法说明：判断xy的点是否在该drawbean的区域内
		 *
		 * @param x
		 * @param y
		 * @return
		 */
		public boolean containsPoint(float x, float y)
		{
			boolean result = x >= xDrawingPosition && x < (xDrawingPosition + getWidth()) && y >= yDrawingPosition && y < (yDrawingPosition + getHeigth());
			return result;

		}

		/**
		 *
		 * 方法说明：判断是否已经离开了屏幕右边边缘
		 *
		 * @return
		 */
		public boolean isOuted()
		{

			return xDrawingPosition <= xStartPosition - drawBit.getWidth();
		}

		/**
		 *
		 * 方法说明：获取当前path移动的距离比例; 0 到1.f
		 *
		 * @return
		 */
		public float getMoveScale()
		{
			return 1.f - (xTargetPosition - xDrawingPosition) / xTotalDistance;
		}
	}

	private Bitmap getImageFromCache(SubDataBean dataBean)
	{
		String imageUrl = dataBean.subImageUrl;
		Bitmap bitmap = ImageLoaderHelper.getBitmapSdcard(imageUrl);
		if (bitmap == null || bitmap.isRecycled()) {
			if (mLoadingImageThread == null || !mLoadingImageThread.isAlive()) {
				mLoadingImageThread = new LoadImageThread();
				mLoadingImageThread.addDownLoadTask(dataBean, true);
				mLoadingImageThread.start();
			} else {
				mLoadingImageThread.addDownLoadTask(dataBean, false);
			}

		}
		return bitmap;

	}

}
