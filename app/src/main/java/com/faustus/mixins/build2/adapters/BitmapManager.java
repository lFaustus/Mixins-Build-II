package com.faustus.mixins.build2.adapters;

import android.content.Context;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * Created by Fluxi on 8/6/2015.
 */
public class BitmapManager
{
    private static File mcacheDirectory;
    private static ImageLoader mImageLoader;
    private ImageLoaderConfiguration mImageLoaderConfiguration;
    private DisplayImageOptions mImageOptions;
    private Context context;
    private static final String CACHE_DIRECTORY;

    static
    {
        mImageLoader = ImageLoader.getInstance();
        CACHE_DIRECTORY = "/TheBarCache/";
    }
    public BitmapManager(Context context)
    {
        this.context = context;
        mcacheDirectory = StorageUtils.getOwnCacheDirectory(context, CACHE_DIRECTORY);
        mImageLoaderConfiguration = new ImageLoaderConfiguration.Builder(context)
                .diskCache(new UnlimitedDiskCache(mcacheDirectory))
                //.diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                //.imageDownloader(new ExtendedImageDownloader())
                .build();

        mImageLoader.init(mImageLoaderConfiguration);

        mImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }

    public void LoadImage(ImageView mImageView,String Uri)
    {
        mImageLoader.displayImage(Uri,new ImageViewAware(mImageView),mImageOptions);
    }

}
