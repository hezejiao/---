package com.sary.arounddepot.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Environment;
import android.util.Log;
import android.view.View;

@SuppressWarnings("rawtypes")
public class BitMapUtil {
	private static final Size ZERO_SIZE = new Size(0, 0);
	private static final Options OPTIONS_GET_SIZE = new Options();
	private static final Options OPTIONS_DECODE = new Options();
	// 线程请求创建图片的队列
	private static final Queue TASK_QUEUE = new LinkedList();
	// 保存队列中正在处理的图片的key,有效防止重复添加到请求创建队列
	private static final Set TASK_QUEUE_INDEX = new HashSet();
	static {
		OPTIONS_GET_SIZE.inJustDecodeBounds = true;
		// 初始化创建图片线程,并等待处理
		new Thread() {
			{
				setDaemon(true);
			}

			public void run() {
				while (true) {
					synchronized (TASK_QUEUE) {
						if (TASK_QUEUE.isEmpty()) {
							try {
								TASK_QUEUE.wait();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
					QueueEntry entry = (QueueEntry) TASK_QUEUE.poll();
					String key = createKey(entry.path, entry.width,
							entry.height);
					TASK_QUEUE_INDEX.remove(key);
					createBitmap(entry.path, entry.width, entry.height);
				}
			}
		}.start();
	}
	
	public static Bitmap originalImg(File file){
		Bitmap resizeBmp = null;
	    try {
	    	 //SD卡是否存在
			 if(!isCanUseSD()){
		    	return null;
		     }
			//文件是否存在
			 if(!file.exists()){
				 return null;
			 }
			resizeBmp = BitmapFactory.decodeFile(file.getPath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return resizeBmp;
	}
	
	public static boolean isCanUseSD() { 
	    try { 
	        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); 
	    } catch (Exception e) { 
	        e.printStackTrace(); 
	    } 
	    return false; 
    } 

	// 创建键
	private static String createKey(String path, int width, int height) {
		if (null == path || path.length() == 0) {
			return "";
		}
		return path + "_" + width + "_" + height;
	}

	public static Bitmap getBitmap(String path) {
		if (path == null) {
			return null;
		}
		File file = new File(path);
		int width = 0;
		int height = 0;
		if (file.exists()) {
			Size size = getBitMapSize(path);
			if (size.equals(ZERO_SIZE)) {
				return null;
			}
			width = size.getWidth();
			height = size.getHeight();
		}
		Bitmap bitMap = null;
		try {
			bitMap = createBitmap(path, width, height);
			return bitMap;
		} catch (OutOfMemoryError err) {
			if (bitMap != null && !bitMap.isRecycled()) {
				bitMap.recycle();
				bitMap = null;
			}
			System.out.print("BitMapUtil=92==OOM===getBitmap=="
					+ err.toString());
		}
		return null;
	}

	public static Size getBitMapSize(String path) {
		File file = new File(path);
		if (file.exists()) {
			InputStream in = null;
			try {
				in = new FileInputStream(file);
				BitmapFactory.decodeStream(in, null, OPTIONS_GET_SIZE);
				return new Size(OPTIONS_GET_SIZE.outWidth,
						OPTIONS_GET_SIZE.outHeight);
			} catch (FileNotFoundException e) {
				return ZERO_SIZE;
			} finally {
				closeInputStream(in);
			}
		}
		return ZERO_SIZE;
	}

	// 通过图片路径,宽度高度创建一个Bitmap对象
	private static Bitmap createBitmap(String path, int width, int height) {
		File file = new File(path);
		if (file.exists()) {
			InputStream in = null;
			Bitmap bitMap = null;
			try {
				in = new FileInputStream(file);
				Size size = getBitMapSize(path);
				if (size.equals(ZERO_SIZE)) {
					return null;
				}
				int scale;
				if (Util.getApp().getScreenWidth() == 0
						|| size.getWidth() < Util.getApp().getScreenWidth()) {
					scale = 1;
				} else {
					scale = size.getWidth() / Util.getApp().getScreenWidth();
				}
				// int b = size.getHeight() / height;
				// scale = Math.max(a, b);
				synchronized (OPTIONS_DECODE) {
					OPTIONS_DECODE.inSampleSize = scale;
					OPTIONS_DECODE.inPreferredConfig = Bitmap.Config.ARGB_8888;
					OPTIONS_DECODE.inPurgeable = true;
					OPTIONS_DECODE.inInputShareable = true;
					OPTIONS_DECODE.inJustDecodeBounds = false;
					bitMap = BitmapFactory.decodeStream(in, null,
							OPTIONS_DECODE);
					return bitMap;
				}
			} catch (FileNotFoundException e) {
				System.out.print("BitMapUtil====createBitmap==" + e.toString());
			} catch (OutOfMemoryError e) {
				if (bitMap != null && !bitMap.isRecycled()) {
					bitMap.recycle();
					bitMap = null;
				}
				System.out.print("BitMapUtil===OOM===createBitmap=="
						+ e.toString());
			} catch (Exception e) {
			} finally {
				closeInputStream(in);
			}
		}
		return null;
	}
	
	/**
	 * 从view 得到图片
	 * @param view
	 * @return
	 */
	public static Bitmap getBitmapFromView(View view) {
        view.destroyDrawingCache();
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache(true);
        return bitmap;
	}

	// 关闭输入流
	private static void closeInputStream(InputStream in) {
		if (null != in) {
			try {
				in.close();
			} catch (IOException e) {
				Log.v("BitMapUtil", "closeInputStream==" + e.toString());
			}
		}
	}

	// 图片大小
	static class Size {
		private int width, height;

		Size(int width, int height) {
			this.width = width;
			this.height = height;
		}

		public int getWidth() {
			return width;
		}

		public int getHeight() {
			return height;
		}
	}

	// 队列缓存参数对象
	static class QueueEntry {
		public String path;
		public int width;
		public int height;
	}
}
