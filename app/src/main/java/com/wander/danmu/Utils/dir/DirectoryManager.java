package com.wander.danmu.Utils.dir;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;


public class DirectoryManager {
	private final static String TAG = "DirectoryManager";
	
	private static DirectoryContext sContext;
	private static HashMap<String, String> sDirs;
	
	public static boolean init(DirectoryContext context) {
		sContext = context;
		sDirs = new HashMap<String, String>();

	    if (!isExternalAvailable()) {
	        return false;
	    }

		return createAll();
	}
	
	public static File getDir( String type ) {
		String path = getDirPath(type);
		if (path != null) {
			return new File(path);
		}
		return null;
	}
	
	public static String getDirPath(String type) {
		if (sDirs == null)
			return null;
		return sDirs.get(type);
	}
	
	public static File getFile(String type, String fileName) {
		String path = getFilePath(type, fileName);
		return new File(path);
	}
	
	public static String getFilePath(String type, String fileName) {
		return getDirPath(type) + File.separator + fileName;
	}
	
	/**
	 * 检测是否挂载外部存储卡
	 * @return
	 */
	public static boolean isExternalAvailable() {
		String state = Environment.getExternalStorageState();
		return state.equals(Environment.MEDIA_MOUNTED);
	}
	
	/**
	 * 检查当前外部存储卡剩余空间大小
	 */
	public static long getExternalAvailableMemorySize() {
		if (isExternalAvailable()) {
			File path = Environment.getExternalStorageDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long availableBlocks = stat.getAvailableBlocks();
			long availableSize = availableBlocks * blockSize;
			return availableSize;
		}
		return -1;
	}
	
	private static boolean createAll() {
		sContext.initDirectories();
		
		boolean ret = false;
		ret = createDirectory(sContext.getSdcardMain());
		ret = ret && createDirectory(sContext.getSdcardHidden());
		return ret;
	}
	
	private static boolean createDirectory(Directory directory) {
		boolean ret = true;
		String path = null;
		Directory parent = directory.getParent();
		// 这是一个根目录
		if (parent == null) {
			path = directory.getName();	
		}
		else {
			File file = getDir(parent.getType());
			path = file.getAbsolutePath() + File.separator + directory.getName();
		}
		
		// 先创建当前目前是否存在
		File file = new File(path);
		if (!file.exists()) {
			ret = file.mkdirs();
		} 
		
		if (!ret) {
			return false;
		}
		
		sDirs.put(directory.getType(), path);
		
		// 再创建各子目录
		Collection<Directory> children = directory.getChildren();
		if (children != null) {
			for (Directory dir: children) {
				if (!createDirectory(dir)) {
					return false;
				}
			}
		}	
		
		return ret;
	}
}




