package com.sary.arounddepot.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.view.View;

/**
 * 异步加载图片
 */
public class AsyncImageLoader {
	// SoftReference是软引用，是为了更好的为了系统回收变量
	// private static HashMap<String, SoftReference<Drawable>> imageCache;
	private Bitmap bitmap = null;
	// 单例
	private static final AsyncImageLoader m_instance = new AsyncImageLoader();

	private AsyncImageLoader() {
	}

	public static AsyncImageLoader getInstance() {
		return m_instance;
	}

	// 回调接口
	public interface ImageCallback {
		public void imageLoaded(Drawable imageDrawable, View imageView,
				String imageUrl);
	}

	private static final double MB = 1024 * 1024;
	private static final int FREE_SD_SPACE_NEEDED_TO_CACHE = 10; // 10MB
	private static final int CACHE_SIZE = 8;
	private static final String WHOLESALE_CONV = ".jpg";
	private static final int HARD_CACHE_CAPACITY = 20; // 硬引用，完全占用内存，不建议设置很大
	private static final long mTimeDiff = 3600 * 1000 * 24 * 7; // 图片保存7天
	private static final int IO_BUFFER_SIZE = 4 * 1024;
	public static final String BITMAPSDCACHE = Environment
			.getExternalStorageDirectory() + "/PARK/";

	@SuppressWarnings("serial")
	private final static HashMap<String, Bitmap> mHardBitmapCache = new LinkedHashMap<String, Bitmap>(
			HARD_CACHE_CAPACITY / 2, 0.75f, true) {
		@Override
		protected boolean removeEldestEntry(
				LinkedHashMap.Entry<String, Bitmap> eldest) {
			if (size() > HARD_CACHE_CAPACITY) {
				// 当map的size大于20时，把最近不常用的key放到mSoftBitmapCache中，从而保证mHardBitmapCache的效率
				mSoftBitmapCache.put(eldest.getKey(),
						new SoftReference<Bitmap>(eldest.getValue()));
				return true;
			} else
				return false;
		}
	};

	/**
	 * 当mHardBitmapCache的key大于20的时候，会根据LRU算法把最近没有被使用的key放入到这个缓存中。
	 * Bitmap使用了SoftReference，当内存空间不足时，此cache中的bitmap会被垃圾回收掉
	 */
	private final static ConcurrentHashMap<String, SoftReference<Bitmap>> mSoftBitmapCache = new ConcurrentHashMap<String, SoftReference<Bitmap>>(
			HARD_CACHE_CAPACITY / 2);

	/**
	 * 从本地读取
	 * 
	 * @param ctx
	 * @param imageUrl
	 * @param imageView
	 * @param imageCallback
	 * @return
	 */
	public Drawable loadDrawableToSD(final Context ctx, final String imageUrl,
			final View imageView, final ImageCallback imageCallback) {
		if (imageView != null) {
			if (mHardBitmapCache.containsKey(imageUrl)) {
				// System.out.println("ImageCacheUtil=========Cache====");
				Bitmap bitmap = getBitmapFromCache(imageUrl);
				Drawable drawable = new BitmapDrawable(bitmap);
				if (drawable != null) {
					return drawable;
				}
			}

		}
		final Handler handler = new Handler() {
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((Drawable) message.obj, imageView,
						imageUrl);
			}
		};
		// 建立新一个新的线程下载图片
		new Thread() {
			@Override
			public void run() {
				Drawable drawable = loadImageFromUrl(ctx, imageUrl);
				// Util.getApp().setImageCache(imageUrl, new
				// SoftReference<Drawable>(drawable));
				Message message = handler.obtainMessage(0, drawable);
				handler.sendMessage(message);
			}
		}.start();
		return null;
	}

	/**
	 * 从缓存中获取图片
	 */
	private Bitmap getBitmapFromCache(String url) {
		// 先从mHardBitmapCache缓存中获取
		synchronized (mHardBitmapCache) {
			final Bitmap bitmap = mHardBitmapCache.get(url);
			if (bitmap != null) {
				// 如果找到的话，把元素移到linkedhashmap的最前面，从而保证在LRU算法中是最后被删除
				mHardBitmapCache.remove(url);
				mHardBitmapCache.put(url, bitmap);
				return bitmap;
			}
		}
		// 如果mHardBitmapCache中找不到，到mSoftBitmapCache中找
		SoftReference<Bitmap> bitmapReference = mSoftBitmapCache.get(url);
		if (bitmapReference != null) {
			final Bitmap bitmap = bitmapReference.get();
			if (bitmap != null) {
				return bitmap;
			} else {
				mSoftBitmapCache.remove(url);
			}
		}
		return null;
	}

	/**
	 * 网络加载图片
	 * 
	 * @param context
	 * @param imageUrl
	 * @return
	 */
	public static Drawable loadImageFromUrl(Context context, String imageUrl) {
		Drawable drawable = null;
		Bitmap bitmap = null;
		if (imageUrl == null || "null".equals(imageUrl) || "".equals(imageUrl)) {
			return null;
		}

		String dir = getDirectory();
		String fileName = convertUrlToFileName(imageUrl);
		File file = new File(dir + "/" + fileName);
		if (file.exists()) {
			// System.out.println("ImageCacheUtil=========SD====");
			bitmap = BitMapUtil.getBitmap(file.toString());
			// System.out.println("ImageCacheUtil========365"+file.getCanonicalPath());
		} else {
			// System.out.println("ImageCacheUtil=========Interent====" +
			// imageUrl);
			bitmap = downloadBitmapByURL(imageUrl);
		}
		if (bitmap == null) {
			return null;
		}
		saveBmpToSd(bitmap, imageUrl);
		mHardBitmapCache.put(imageUrl, bitmap);
		drawable = new BitmapDrawable(bitmap);

		return drawable;
	}

	public static String getDirectory() {
		// 图片在手机本地的存放路径,注意：fileName为空的情况
		String saveDir = "";
		saveDir = BITMAPSDCACHE;

		File dir = new File(saveDir);

		if (!dir.exists()) {
			dir.mkdir();
		}
		return saveDir;
	}

	public static String convertUrlToFileName(String imageUrl) {
		String fileName = null;

		if (imageUrl.toLowerCase().contains(".jpg")
				|| imageUrl.toLowerCase().contains(".png")
				|| imageUrl.toLowerCase().contains(".gif")) {
			// 获取url中图片的文件名与后缀
			if (imageUrl != null && imageUrl.length() != 0) {
				fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
			}
		} else {
			if (imageUrl.contains("/")) {
				String temp[] = imageUrl.split("/");
				fileName = temp[temp.length - 2] + "_" + temp[temp.length - 1]
						+ ".jpg";
			}
		}

		return fileName;
	}

	public static Bitmap downloadBitmapByURL(String url) {
		HttpGet httpRequest = null;
		HttpResponse response = null;
		try {

			URL m = new URL(url);
			try {
				httpRequest = new HttpGet(m.toURI());
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}

			response = (HttpResponse) NativeHttpClient.getHttpClient().execute(
					httpRequest);

			HttpEntity entity = response.getEntity();
			Bitmap bitmap = null;
			// BitmapFactory.Options opts = new BitmapFactory.Options();
			if (entity != null) {
				InputStream inputStream = null;
				OutputStream outputStream = null;
				// 关键就是这一段
				try {
					BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(
							entity);
					inputStream = bufHttpEntity.getContent();
					Options OPTIONS_DECODE = new Options();
					ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
					outputStream = new BufferedOutputStream(dataStream,
							IO_BUFFER_SIZE);
					copy(inputStream, outputStream);
					outputStream.flush();

					byte[] data = dataStream.toByteArray();

					OPTIONS_DECODE.inJustDecodeBounds = true;
					BitmapFactory.decodeByteArray(data, 0, data.length,
							OPTIONS_DECODE);
					int scale = 2;
					int outWidth = OPTIONS_DECODE.outWidth;
					int screenWidth = Util.getApp().getScreenWidth();
					System.out
							.println("AsyncImageLoader===outWidth : ScreenWidth()==="
									+ OPTIONS_DECODE.outWidth
									+ " : "
									+ Util.getApp().getScreenWidth());
					if (OPTIONS_DECODE.outWidth >= Util.getApp()
							.getScreenWidth()) {
						scale = (outWidth % screenWidth == 0) ? outWidth
								/ screenWidth : (outWidth / screenWidth + 1);
					} else {
						scale = 1;
					}
					System.out.println("AsyncImageLoader===scale===" + scale);
					OPTIONS_DECODE.inSampleSize = scale;
					OPTIONS_DECODE.inPreferredConfig = Bitmap.Config.ARGB_8888;
					OPTIONS_DECODE.inPurgeable = true;
					OPTIONS_DECODE.inInputShareable = true;
					OPTIONS_DECODE.inJustDecodeBounds = false;
					bitmap = BitmapFactory.decodeByteArray(data, 0,
							data.length, OPTIONS_DECODE);

					return bitmap;
				} catch (OutOfMemoryError err) {
					System.out.println("ImageCacheUtil===420==OOM========="
							+ err.getStackTrace());
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					if (outputStream != null) {
						outputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (Exception e) {
			return null;
		}
		return null;

	}

	/**
	 * 数据流转成btyle[]数组
	 * */
	private static byte[] getBytes(InputStream is) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] b = new byte[2048];
		int len = 0;
		try {
			while ((len = is.read(b, 0, 2048)) != -1) {
				baos.write(b, 0, len);
				baos.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] bytes = baos.toByteArray();
		return bytes;
	}

	public static void copy(InputStream in, OutputStream out)
			throws IOException {
		byte[] b = new byte[IO_BUFFER_SIZE];
		int read;
		while ((read = in.read(b)) != -1) {
			out.write(b, 0, read);
		}
	}

	private static void saveBmpToSd(Bitmap bm, String url) {
		String dir = getDirectory();
		removeExpiredCache(dir);
		if (bm == null) {
			System.out.println("trying to savenull bitmap");
			return;
		}
		// 判断sdcard上的空间
		if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
			removeCache(dir);
			System.out.println("Low free space onsd, do not cache");
			return;
		}
		String filename = convertUrlToFileName(url);

		File file = new File(dir + "/" + filename);
		try {
			if (!file.exists()) {
				file.createNewFile();
				OutputStream outStream = new FileOutputStream(file);
				bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
				outStream.flush();
				outStream.close();
				System.out.println("Image saved tosd");
			}
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException");
		} catch (IOException e) {
			System.out.println("IOException");
		}
	}

	/**
	 * 删除保存的图片
	 * 
	 * @param dirPath
	 */
	public static int delExpiredCache(String dirPath) {
		File dir = new File(dirPath);
		File[] files = dir.listFiles();
		if (files == null || files.length == 0) {
			return 0;
		}
		for (int i = 0; i < files.length; i++) {
			if (files[i].getName().contains(WHOLESALE_CONV)) {
				if (System.currentTimeMillis() - files[i].lastModified() > 0) {

					System.out.println("Clear some expiredcache files ");

					files[i].delete();

				}
			}
		}
		return 1;
	}

	/**
	 * 删除sd上缓存的所有文件
	 * 
	 * @return
	 */
	public static int delAllCacheFile() {
		File dir = new File(getDirectory());
		File[] files = dir.listFiles();
		if (files == null || files.length == 0) {
			return 0;
		}
		for (int i = 0; i < files.length; i++) {
			files[i].delete();
		}
		return 1;
	}

	/**
	 * 移除超过保存日期的图片文件
	 * 
	 * @param dirPath
	 */
	private static void removeExpiredCache(String dirPath) {
		File dir = new File(dirPath);
		File[] files = dir.listFiles();
		if (files == null) {
			return;
		}
		for (int i = 0; i < files.length; i++) {
			if (files[i].getName().contains(WHOLESALE_CONV)) {
				if (System.currentTimeMillis() - files[i].lastModified() > mTimeDiff) {

					System.out.println("Clear some expiredcache files ");

					files[i].delete();

				}
			}
		}
	}

	/**
	 * 计算存储目录下的文件大小，
	 * 当文件总大小大于规定的CACHE_SIZE或者sdcard剩余空间小于FREE_SD_SPACE_NEEDED_TO_CACHE的规定
	 * 那么删除40%最近没有被使用的文件
	 * 
	 * @param dirPath
	 * @param filename
	 */
	private static void removeCache(String dirPath) {
		File dir = new File(dirPath);
		File[] files = dir.listFiles();
		if (files == null) {
			return;
		}
		int dirSize = 0;
		for (int i = 0; i < files.length; i++) {
			if (files[i].getName().contains(WHOLESALE_CONV)) {
				dirSize += files[i].length();
			}
		}
		if (dirSize > CACHE_SIZE * MB
				|| FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
			int removeFactor = (int) ((0.4 * files.length) + 1);

			Arrays.sort(files, new FileLastModifSort());

			System.out.println("Clear some expiredcache files ");

			for (int i = 0; i < removeFactor; i++) {

				if (files[i].getName().contains(WHOLESALE_CONV)) {

					files[i].delete();

				}

			}

		}

	}

	/**
	 * 计算sdcard上的剩余空间
	 * 
	 * @return
	 */
	private static int freeSpaceOnSd() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());
		double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat
				.getBlockSize()) / MB;
		// System.out.println("=================" + sdFreeMB);
		return (int) sdFreeMB;
	}

	/**
	 * 根据文件的最后修改时间进行排序 *
	 */
	static class FileLastModifSort implements Comparator<File> {

		public int compare(File arg0, File arg1) {
			if (arg0.lastModified() > arg1.lastModified()) {
				return 1;
			} else if (arg0.lastModified() == arg1.lastModified()) {
				return 0;
			} else {
				return -1;
			}
		}
	}

	/****
	 * 处理图片bitmap size exceeds VM budget （Out Of Memory 内存溢出）
	 */
	private static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	public static int computeSampleSize(BitmapFactory.Options options,
			int target) {
		int w = options.outWidth;
		int h = options.outHeight;
		int candidateW = w / target;
		int candidateH = h / target;
		int candidate = Math.max(candidateW, candidateH);
		if (candidate == 0)
			return 1;
		if (candidate > 1) {
			if ((w > target) && (w / candidate) < target)
				candidate -= 1;
		}
		if (candidate > 1) {
			if ((h > target) && (h / candidate) < target)
				candidate -= 1;
		}
		return candidate;
	}

	public void destroyBitmap() {
		if (null != bitmap && !bitmap.isRecycled()) {
			bitmap.recycle();
		}
	}
}
