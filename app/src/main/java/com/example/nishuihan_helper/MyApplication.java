package com.example.nishuihan_helper;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.nishuihan_helper.service.MediaService;

public class MyApplication extends Application {
    public static final String SCREEN_CAPTURE_CHANNEL_ID = "Screen Capture ID";
    public static final String SCREEN_CAPTURE_CHANNEL_NAME = "Screen Capture";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        createScreenCaptureNotificationChannel();
    }
    private void createScreenCaptureNotificationChannel() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // 创建通知渠道
        NotificationChannel screenCaptureChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            screenCaptureChannel = new NotificationChannel(
                    SCREEN_CAPTURE_CHANNEL_ID,
                    SCREEN_CAPTURE_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_LOW
            );
        }
        // 设置通知渠道给通知管理器
        if (notificationManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(screenCaptureChannel);
            }
        }
    }
}
