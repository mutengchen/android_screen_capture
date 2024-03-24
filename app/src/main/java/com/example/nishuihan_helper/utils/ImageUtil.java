package com.example.nishuihan_helper.utils;

import android.graphics.Bitmap;
import android.media.Image;

import java.nio.ByteBuffer;

public class ImageUtil {
    public static Bitmap imageToBitmap(Image image) {
        int width = image.getWidth();
        int height = image.getHeight();
        Image.Plane[] planes = image.getPlanes();
        ByteBuffer buffer = planes[0].getBuffer();
        // 两个像素的距离
        int pixelStride = planes[0].getPixelStride();
        // 整行的距离
        int rowStride = planes[0].getRowStride();
        int rowPadding = rowStride - pixelStride * width;
        Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(buffer);
        image.close();
        return bitmap;
    }
}
