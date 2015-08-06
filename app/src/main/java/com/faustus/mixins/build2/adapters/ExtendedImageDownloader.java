package com.faustus.mixins.build2.adapters;

import com.nostra13.universalimageloader.core.download.ImageDownloader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Fluxi on 8/6/2015.
 */
public class ExtendedImageDownloader implements ImageDownloader
{
    @Override
    public InputStream getStream(String imageUri, Object extra) throws IOException
    {
        InputStream is = new FileInputStream(imageUri);
        return is;
    }
}
