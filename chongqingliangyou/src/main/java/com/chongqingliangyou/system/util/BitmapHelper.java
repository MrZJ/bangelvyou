package com.chongqingliangyou.system.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * A class for helping deal the bitmap, like: get the orientation of the bitmap, compress bitmap etc.
 *
 * @author wangcccong
 * @version 1.140122 crated at: 2014-03-22 update at: 2014-06-26
 */
public class BitmapHelper {
	public static int width = 540;

	public static int height = 960;

	/**
	 * 压缩指定路径的图片，并得到图片对象
	 *
	 * @param path bitmap source path
	 * @return Bitmap {@link Bitmap}
	 */
	public final static Bitmap compressBitmap(String path, int rqsW, int rqsH) {
		final Options options = new Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		int s1 = computeSampleSize(options, -1, rqsW * rqsH);
		options.inSampleSize = s1;
		options.inJustDecodeBounds = false;
		Bitmap bitmap1 = null;
		try {
			bitmap1 = BitmapFactory.decodeFile(path, options);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap1;
	}

	/**
	 * 压缩指定路径图片，并将其保存在缓存目录中，通过isDelSrc判定是否删除源文件，并获取到缓存后的图片路径
	 *
	 * @param context
	 * @param srcPath
	 * @param rqsW
	 * @param rqsH
	 * @param isDelSrc
	 * @return
	 */
	public final static String compressBitmap(Context context, String srcPath, int rqsW, int rqsH, boolean isDelSrc) {
		Bitmap bitmap = compressBitmap(srcPath, rqsW, rqsH);
		File srcFile = new File(srcPath);
		String desPath = getImageCacheDir(context) + "min_" + srcFile.getName();
		int degree = getDegress(srcPath);
		try {
			if (degree != 0)
				bitmap = rotateBitmap(bitmap, degree);
			File file = new File(desPath);
			FileOutputStream fos = new FileOutputStream(file);
			bitmap.compress(CompressFormat.PNG, 70, fos);
			fos.close();
			if (isDelSrc)
				srcFile.deleteOnExit();
		} catch (Exception e) {
//			Log.e("BitmapHelper-->compressBitmap", e.getMessage() + "");
		}
		return desPath;
	}

	public final static String compressBitmap(Context context, String srcPath, Bitmap bitmap, boolean isDelSrc) {
		File srcFile = new File(srcPath);
		String desPath = getImageCacheDir(context) + "min_" + srcFile.getName();
		int degree = getDegress(srcPath);
		try {
			if (degree != 0)
				bitmap = rotateBitmap(bitmap, degree);
			File file = new File(desPath);
			FileOutputStream fos = new FileOutputStream(file);
			bitmap.compress(CompressFormat.PNG, 70, fos);
			fos.close();
			if (isDelSrc)
				srcFile.deleteOnExit();
		} catch (Exception e) {
//			Log.e("BitmapHelper-->compressBitmap", e.getMessage() + "");
		}
		return desPath;
	}

	/**
	 * get the orientation of the bitmap {@link ExifInterface}
	 *
	 * @param path
	 * @return
	 */
	public final static int getDegress(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * rotate the bitmap
	 *
	 * @param bitmap
	 * @param degress
	 * @return
	 */
	public static Bitmap rotateBitmap(Bitmap bitmap, int degress) {
		if (bitmap != null) {
			Matrix m = new Matrix();
			m.postRotate(degress);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
			return bitmap;
		}
		return bitmap;
	}

	/**
	 * caculate the bitmap sampleSize
	 * @return
	 */
	public final static int caculateInSampleSize(Options options, int dstWidth, int dstHeight) {
		final int srcWidth = options.outHeight;
		final int srcHeight = options.outWidth;
		final float srcAspect = (float) srcWidth / (float) srcHeight;
		final float dstAspect = (float) dstWidth / (float) dstHeight;

		if (srcAspect > dstAspect) {
			return srcWidth / dstWidth;
		} else {
			return srcHeight / dstHeight;
		}
	}

	public static int computeSampleSize(Options options, int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
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

	private static int computeInitialSampleSize(Options options, int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength),
				Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
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

	/**
	 * 压缩某个输入流中的图片，可以解决网络输入流压缩问题，并得到图片对象
	 * @return Bitmap {@link Bitmap}
	 */
	public final static Bitmap compressBitmap(InputStream is, int reqsW, int reqsH) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ReadableByteChannel channel = Channels.newChannel(is);
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			while (channel.read(buffer) != -1) {
				buffer.flip();
				while (buffer.hasRemaining())
					baos.write(buffer.get());
				buffer.clear();
			}
			byte[] bts = baos.toByteArray();
			Bitmap bitmap = compressBitmap(bts, reqsW, reqsH);
			is.close();
			channel.close();
			baos.close();
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 压缩指定byte[]图片，并得到压缩后的图像
	 *
	 * @param bts
	 * @param reqsW
	 * @param reqsH
	 * @return
	 */
	public final static Bitmap compressBitmap(byte[] bts, int reqsW, int reqsH) {
		final Options options = new Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(bts, 0, bts.length, options);
		options.inSampleSize = caculateInSampleSize(options, reqsW, reqsH);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeByteArray(bts, 0, bts.length, options);
	}

	/**
	 * 压缩已存在的图片对象，并返回压缩后的图片
	 *
	 * @param bitmap
	 * @param reqsW
	 * @param reqsH
	 * @return
	 */
	public final static Bitmap compressBitmap(Bitmap bitmap, int reqsW, int reqsH) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.PNG, 100, baos);
			byte[] bts = baos.toByteArray();
			Bitmap res = compressBitmap(bts, reqsW, reqsH);
			baos.close();
			return res;
		} catch (IOException e) {
			e.printStackTrace();
			return bitmap;
		}
	}

	/**
	 * 压缩资源图片，并返回图片对象
	 *
	 * @param res {@link Resources}
	 * @param resID
	 * @param reqsW
	 * @param reqsH
	 * @return
	 */
	public final static Bitmap compressBitmap(Resources res, int resID, int reqsW, int reqsH) {
		final Options options = new Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resID, options);
		options.inSampleSize = caculateInSampleSize(options, reqsW, reqsH);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resID, options);
	}

	/**
	 * 基于质量的压缩算法， 此方法未 解决压缩后图像失真问题 <br>
	 * 可先调用比例压缩适当压缩图片后，再调用此方法可解决上述问题
	 * @param maxBytes 压缩后的图像最大大小 单位为byte
	 * @return
	 */
	public final static Bitmap compressBitmap(Bitmap bitmap, long maxBytes) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.PNG, 100, baos);
			int options = 90;
			while (baos.toByteArray().length > maxBytes) {
				baos.reset();
				bitmap.compress(CompressFormat.PNG, options, baos);
				options -= 10;
			}
			byte[] bts = baos.toByteArray();
			Bitmap bmp = BitmapFactory.decodeByteArray(bts, 0, bts.length);
			baos.close();
			return bmp;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 得到指定路径图片的options
	 *
	 * @param srcPath
	 * @return Options {@link Options}
	 */
	public final static Options getBitmapOptions(String srcPath) {
		Options options = new Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(srcPath, options);
		return options;
	}

	/**
	 * 获取图片缓存路径
	 *
	 * @param context
	 * @return
	 */
	private static String getImageCacheDir(Context context) {
		String dir = getSaveImagesDirForTemp();
		File file = new File(dir);
		if (!file.exists())
			file.mkdirs();
		return dir;
	}

	/**
	 * 判断SD卡是否可用
	 * @return
	 */
	public static boolean hasSDCard() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}

	public static String getRootFilePath() {
		String str;
		if (hasSDCard()) {
			str = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
		} else {
			str = Environment.getDataDirectory().getAbsolutePath() + "/data/";
		}
		return str;
	}

	public static String getSaveImagesDirForTemp() {
		String str;
		if (hasSDCard()) {
			str = getRootFilePath() + "chongqingliangyou/images/";
		} else {
			str = getRootFilePath() + "com.chongqingliangyou.system/images/";
		}
		return str;
	}
}