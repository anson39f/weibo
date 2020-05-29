package com.eminem.weibo.widget;

import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.eminem.weibo.R;

import de.hdodenhof.circleimageview.CircleImageView;


public class ImageLoader {
    private ImageLoader() {
    }

    public static void loadImage(RequestManager loader, ImageView view, String url) {
        loadImage(loader, view, url, R.drawable.def_image);
    }

    public static void loadImage(RequestManager loader, ImageView view, String url, int placeholder) {
        loadImage(loader, view, url, placeholder, placeholder);
    }

    public static void loadImage(RequestManager loader, ImageView view, String url, int placeholder, int error) {
        boolean isCenterCrop = false;
        if (view instanceof CircleImageView)
            isCenterCrop = true;
        loadImage(loader, view, url, placeholder, error, isCenterCrop);
    }

    public static void loadImage(RequestManager loader, ImageView view, String url, int placeholder, int error, boolean isCenterCrop) {
        if (TextUtils.isEmpty(url)) {
            view.setImageResource(placeholder);
        } else {
            RequestOptions options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(placeholder).error(error);
            if (isCenterCrop) {
                options.centerCrop();
            }
            loader.load(url).apply(options).into(view);
        }
    }


    public static void loadImage(RequestManager loader, ImageView view, Uri url) {
        loadImage(loader, view, url, R.drawable.def_image);
    }

    public static void loadImage(RequestManager loader, ImageView view, Uri url, int placeholder) {
        loadImage(loader, view, url, placeholder, placeholder);
    }

    public static void loadImage(RequestManager loader, ImageView view, Uri url, int placeholder, int error) {
        boolean isCenterCrop = false;
        if (view instanceof CircleImageView)
            isCenterCrop = true;
        loadImage(loader, view, url, placeholder, error, isCenterCrop);
    }

    public static void loadImage(RequestManager loader, ImageView view, Uri url, int placeholder, int error, boolean isCenterCrop) {
        if (null == url) {
            view.setImageResource(placeholder);
        } else {
            RequestOptions options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(placeholder).error(error);
            if (isCenterCrop) {
                options.centerCrop();
            }
            loader.load(url).apply(options).into(view);
        }
    }


    public static void loadImage(RequestManager loader, ImageView view, int res) {
        loadImage(loader, view, res, R.drawable.def_image);
    }

    public static void loadImage(RequestManager loader, ImageView view, int res, int placeholder) {
        loadImage(loader, view, res, placeholder, placeholder);
    }

    public static void loadImage(RequestManager loader, ImageView view, int res, int placeholder, int error) {
        boolean isCenterCrop = false;
        if (view instanceof CircleImageView)
            isCenterCrop = true;
        loadImage(loader, view, res, placeholder, error, isCenterCrop);
    }

    public static void loadImage(RequestManager loader, ImageView view, int res, int placeholder, int error, boolean isCenterCrop) {
        if (res <= 0) {
            view.setImageResource(placeholder);
        } else {

            RequestOptions options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(placeholder).error(error);
            if (isCenterCrop) {
                options.centerCrop();
            }
            loader.load(res).apply(options).into(view);
        }
    }

}
