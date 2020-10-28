package com.basulvyou.system.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.basulvyou.system.R;
import com.basulvyou.system.ui.activity.GalleyImageActivity;
import com.basulvyou.system.util.AsynImageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;

/**
 * 分享图片Fragment
 */
public class ImageFragment extends Fragment {

    private static final String IMAGE_URL = "image";
    PhotoView image;
    private String imageUrl;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private static Context context;
    public ImageFragment() {
        // Required empty public constructor
    }

    public static ImageFragment newInstance(String param1,Context ctx) {
    	context = ctx;
    	ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putString(IMAGE_URL, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imageUrl = getArguments().getString(IMAGE_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        image = (PhotoView) view.findViewById(R.id.image);
        imageLoader.displayImage(imageUrl, image, AsynImageUtil.getImageOptions());
        image.setOnPhotoTapListener(new OnPhotoTapListener() {  
            
            @Override  
            public void onPhotoTap(View arg0, float arg1, float arg2) {  
                ((GalleyImageActivity) context).finish();
            }  
        });  
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

//    public interface OnFragmentInteractionListener {
//        void onFragmentInteraction(Uri uri);
//    }
}
