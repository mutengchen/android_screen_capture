package com.example.nishuihan_helper.service;

import android.accessibilityservice.AccessibilityService;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaScannerConnection;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class MyAccessibilityService extends AccessibilityService {
    private MediaProjectionManager mediaProjectionManager;
    private Intent mediaProjectionPermissionIntent;
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            // 当检测到窗口状态变化时进行处理
            AccessibilityNodeInfo rootNode = getRootInActiveWindow();
            if (rootNode != null) {
                // 在这里根据界面元素的信息判断是否需要触发截图操作
                // 例如，可以通过界面元素的 ID、文本等属性来判断是否是目标界面
                // 如果是目标界面，则触发截图操作
                performScreenshot();
            }
        }
    }

    @Override
    public void onInterrupt() {
        // 当服务中断时调用
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
    }

    private void performScreenshot() {
        // 使用系统截图功能进行截图
        try {
            // 获取屏幕截图
//            Bitmap screenshot = takeScreenshot();
//            // 保存截图到本地存储
//            saveScreenshot(screenshot);
        } catch (Exception e) {
            Log.e("Screenshot", "Failed to take screenshot", e);
        }
    }


//    private Bitmap takeScreenshot() {
//        MediaProjection mediaProjection = mediaProjectionManager.getMediaProjection(Activity.RESULT_OK, data);
//        DisplayMetrics metrics = getResources().getDisplayMetrics();
//        int screenWidth = metrics.widthPixels;
//        int screenHeight = metrics.heightPixels;
//        ImageReader imageReader = ImageReader.newInstance(screenWidth, screenHeight, PixelFormat.RGBA_8888, 2);
//
//        VirtualDisplay virtualDisplay = mediaProjection.createVirtualDisplay("Screenshot",
//                screenWidth, screenHeight, metrics.densityDpi,
//                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
//                imageReader.getSurface(), null, null);
//
//        Image image = imageReader.acquireLatestImage();
//        if (image != null) {
//            Image.Plane[] planes = image.getPlanes();
//            ByteBuffer buffer = planes[0].getBuffer();
//            int pixelStride = planes[0].getPixelStride();
//            int rowStride = planes[0].getRowStride();
//            int rowPadding = rowStride - pixelStride * screenWidth;
//
//            Bitmap bitmap = Bitmap.createBitmap(screenWidth + rowPadding / pixelStride, screenHeight, Bitmap.Config.ARGB_8888);
//            bitmap.copyPixelsFromBuffer(buffer);
//            image.close();
//            return bitmap;
//        }
//
//        return null;
//    }
    private void saveScreenshot(Bitmap screenshot) {
        File screenshotsDir = new File(Environment.getExternalStorageDirectory(), "Screenshots");
        if (!screenshotsDir.exists()) {
            screenshotsDir.mkdirs();
        }

        File screenshotFile = new File(screenshotsDir, "screenshot.png");
        try {
            FileOutputStream outputStream = new FileOutputStream(screenshotFile);
            screenshot.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            MediaStore.Images.Media.insertImage(getContentResolver(), screenshotFile.getAbsolutePath(), screenshotFile.getName(), null);


        } catch (Exception e) {
            Log.e("Screenshot", "Failed to save screenshot", e);
        }
    }
}