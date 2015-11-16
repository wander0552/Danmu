package com.wander.danmu.Utils.dir;

import android.os.Environment;

import java.io.File;


public abstract class DirectoryContext {

	public static final String SDCARD_MAIN = "SDCARD_MAIN";
	public static final String SDCARD_HIDDEN = "SDCARD_HIDDEN";
	public static final String APP_CACHE = "APP_CACHE";
	public static final String CACHE = "CACHE";
	public static final String LOG = "LOG";
	
	private Directory mSdcardMain;
	private Directory mSdcardHidden;
	private Directory mAppCache;
	private Directory mCache;
	private Directory mLog;
	
	public DirectoryContext(String appName) {
		mSdcardMain  = new Directory();
		String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + appName;
		mSdcardMain.setName(path);
		mSdcardMain.setType(DirectoryContext.SDCARD_MAIN);
		
		mSdcardHidden  = new Directory();
		path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "." + appName;
		mSdcardHidden.setName(path);
		mSdcardHidden.setType(DirectoryContext.SDCARD_HIDDEN);
		
		mCache  = new Directory();
		mCache.setName("cache");
		mCache.setType(DirectoryContext.CACHE);
		mCache.setParent(mSdcardMain);

		mLog  = new Directory();
		mLog.setName("log");
		mLog.setType(DirectoryContext.LOG);
		mLog.setParent(mSdcardMain);
		
		AddChild(mSdcardMain, CACHE, "cache");
		AddChild(mSdcardMain, LOG, "log");
	}
	
	protected abstract void initDirectories();

	public Directory getSdcardMain() {
		return mSdcardMain;
	}

	public Directory getSdcardHidden() {
		return mSdcardHidden;
	}
	
	public Directory getCache() {
		return mCache;
	}
	
	public Directory getLog() {
		return mLog;
	}
	
	protected Directory AddChild(Directory parent, String type, String name) {
		Directory child = new Directory();
		child.setType(type);
		child.setName(name);
		child.setParent(parent);
		parent.addChild(child);
		return child;
	}
}
