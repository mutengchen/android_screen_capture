package com.example.nishuihan_helper.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MainService extends Service {
    private  String TAG  = MainService.class.getSimpleName();
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    // 广播接收器
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // 处理接收到的广播
            if ("com.example.nishuihan_helper.SHOW_TOAST".equals(intent.getAction())) {
                Log.d(TAG, "onReceive: ");
                //线程中断10s
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                showToast("Hello from ADB!");
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.nishuihan_helper.SHOW_TOAST");
        registerReceiver(receiver, filter);


    }

    // 自定义方法用于显示 Toast 消息
    public String showToast(String message) {
         Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        return "showToast";
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
