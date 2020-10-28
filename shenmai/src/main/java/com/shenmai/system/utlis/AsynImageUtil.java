package com.shenmai.system.utlis;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by KevinLi on 2016/1/25.
 */
public class AsynImageUtil {
    public static Animation mRotateAnimation = new RotateAnimation(0, 720,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

    public static final Interpolator ANIMATION_INTERPOLATOR = new LinearInterpolator();

    public static final int ROTATION_ANIMATION_DURATION = 1200;

    public static final int ROTATION_ANIMATION_DURATION_SHORT = 1500;

    public static PromotionFirstDisplayListener promotionFirstListener = new PromotionFirstDisplayListener();

    public static DisplayImageOptions getImageOptions(int drawableId){
        return new DisplayImageOptions.Builder()
                .showImageForEmptyUri(drawableId)
                .showImageOnFail(drawableId).imageScaleType(ImageScaleType.EXACTLY)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(100))
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(100))// 图片加载好后渐入的动画时间
                .displayer(new RoundedBitmapDisplayer(1)).build();
    }

    public static DisplayImageOptions getImageOptions() {
        return new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(100))
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(100))// 图片加载好后渐入的动画时间
                .displayer(new RoundedBitmapDisplayer(1)).build();
    }

    public static class AnimateFirstDisplayListener extends
            SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());

        public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

    /**下载置顶图片缓存数据*/
    public static class PromotionFirstDisplayListener extends
            SimpleImageLoadingListener {

        static boolean isSave = false;//是否保存过图片
        private String path;

        public PromotionFirstDisplayListener(){
        }

        public PromotionFirstDisplayListener(String path){
            this.path = path;
        }

        public void setPath(String path){
            this.path = path;
        }

        public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                imageView.setImageBitmap(loadedImage);
                if (!isSave) {
                    if(path == null){
                        isSave = true;
                        return;
                    }
                    File imgFile = new File(path);
                    if (imgFile.exists()) {
                        imgFile.delete();
                    }
                    try {
                        FileOutputStream out = new FileOutputStream(imgFile);
                        loadedImage.compress(Bitmap.CompressFormat.PNG, 90, out);
                        out.flush();
                        out.close();
                        Log.e("adv_img_top", "已经保存");
                        isSave = true;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**下载非置顶图片缓存数据*/
    public static class PromotionNotFirstDisplayListener extends
            SimpleImageLoadingListener {

        private String path;

        public PromotionNotFirstDisplayListener(){
        }

        public PromotionNotFirstDisplayListener(String path){
            this.path = path;
        }

        public void setPath(String path){
            this.path = path;
        }

        public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                imageView.setImageBitmap(loadedImage);
                File imgFile = new File(path);
                if (imgFile.exists()) {
                    imgFile.delete();
                }
                try {
                    FileOutputStream out = new FileOutputStream(imgFile);
                    loadedImage.compress(Bitmap.CompressFormat.PNG, 90, out);
                    out.flush();
                    out.close();
                    Log.e("adv_img_not_top", "已经保存");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
