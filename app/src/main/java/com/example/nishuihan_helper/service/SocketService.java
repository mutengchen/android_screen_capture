package com.example.nishuihan_helper.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.rabtman.wsmanager.WsManager;
import com.rabtman.wsmanager.listener.WsStatusListener;
import com.xuhao.didi.core.iocore.interfaces.IPulseSendable;
import com.xuhao.didi.core.iocore.interfaces.ISendable;
import com.xuhao.didi.core.pojo.OriginalData;
import com.xuhao.didi.socket.client.sdk.OkSocket;
import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;
import com.xuhao.didi.socket.client.sdk.client.action.SocketActionAdapter;
import com.xuhao.didi.socket.client.sdk.client.connection.IConnectionManager;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import okio.ByteString;

public class SocketService extends Service {
    private String TAG = SocketService.class.getSimpleName();
    WsManager wsManager;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //连接参数设置(IP,端口号),这也是一个连接的唯一标识,不同连接,该参数中的两个值至少有其一不一样
//        initSocket();
        initws();
        startAccessibilityService();

    }
    //启动无障碍服务器
    private void startAccessibilityService(){
        Intent intent = new Intent();
        intent.setAction("android.settings.ACCESSIBILITY_SETTINGS");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    public void capture(){
        //TODO 截取当前屏幕

    }
    private void initws(){
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .pingInterval(15, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
        wsManager = new WsManager.Builder(this)
                .wsUrl("ws://192.168.2.95:5000/ws")
                .needReconnect(true)
                .client(okHttpClient)
                .build();
        wsManager.setWsStatusListener(new WsStatusListener() {
            @Override
            public void onOpen(Response response) {
                super.onOpen(response);
                Log.d(TAG, "onOpen: ");
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        wsManager.sendMessage("start_capture");
                    }
                },0,10000);

            }

            @Override
            public void onMessage(String text) {
                super.onMessage(text);
                if(text.equals("capture")){
                    //TODO 开始启动截图ocr识别
                    Log.d(TAG, "onMessage: 开始截图ocr识别");

                }
            }

            @Override
            public void onMessage(ByteString bytes) {
                super.onMessage(bytes);
            }

            @Override
            public void onReconnect() {
                super.onReconnect();
                Log.d(TAG, "onReconnect: ");
            }

            @Override
            public void onClosing(int code, String reason) {
                super.onClosing(code, reason);
                Log.d(TAG, "onClosing: "+reason);
            }

            @Override
            public void onClosed(int code, String reason) {
                super.onClosed(code, reason);
            }

            @Override
            public void onFailure(Throwable t, Response response) {
                super.onFailure(t, response);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });

        wsManager.startConnect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        wsManager.stopConnect();
    }
    //    private void initSocket(){
//        ConnectionInfo info = new ConnectionInfo("192.168.2.95", 20000);//连接参数设置(IP,端口号)
//        mManager = OkSocket.open(info);//调用OkSocket,开启这次连接的通道,拿到通道Manager
//        mManager.registerReceiver(socketActionAdapter);//注册Socket行为监听器
//        mManager.connect();
//    }

//    IConnectionManager mManager;
//    SocketActionAdapter socketActionAdapter = new SocketActionAdapter() {
//        @Override
//        public void onSocketIOThreadStart(String action) {
//            super.onSocketIOThreadStart(action);
//        }
//
//        @Override
//        public void onSocketIOThreadShutdown(String action, Exception e) {
//            super.onSocketIOThreadShutdown(action, e);
//        }
//
//        @Override
//        public void onSocketDisconnection(ConnectionInfo info, String action, Exception e) {
//            super.onSocketDisconnection(info, action, e);
//            //断开连接
//            Log.d(TAG, "断开服务器连接");
//        }
//
//        @Override
//        public void onSocketConnectionSuccess(ConnectionInfo info, String action) {
//            super.onSocketConnectionSuccess(info, action);
//            Log.d(TAG, "客户端连接服务器成功,开启心跳");
//
//            //连接成功,开启心跳
//            OkSocket.open(info)
//                    .getPulseManager()
//                    .setPulseSendable(new IPulseSendable() {
//                        @Override
//                        public byte[] parse() {
//                            byte[] body = "ping".getBytes(Charset.defaultCharset()); // 心跳数据
//                            ByteBuffer bb = ByteBuffer.allocate(4 + body.length);
//                            bb.order(ByteOrder.BIG_ENDIAN);
//                            bb.putInt(body.length);
//                            bb.put(body);
//                            return bb.array();
//                        }
//                    })
//                    .pulse();//开始心跳,开始心跳后,心跳管理器会自动进行心跳触发
//        }
//
//        @Override
//        public void onSocketConnectionFailed(ConnectionInfo info, String action, Exception e) {
//            super.onSocketConnectionFailed(info, action, e);
//            Log.d(TAG, "客户端连接服务器失败");
//        }
//
//        @Override
//        public void onSocketReadResponse(ConnectionInfo info, String action, OriginalData data) {
//            String str = new String(data.getBodyBytes(), Charset.forName("utf-8"));
//            Log.d(TAG, "客户端读取数据回调" + str);
//            if (mManager != null && str.equals("ack")) {//是否是心跳返回包,需要解析服务器返回的数据才可知道
//                Log.d(TAG, "客户端喂狗");//喂狗操作
//                mManager.getPulseManager().feed();
//            }
//            super.onSocketReadResponse(info, action, data);
//        }
//
//        @Override
//        public void onSocketWriteResponse(ConnectionInfo info, String action, ISendable data) {
//            Log.d(TAG, "客户端发送数据回调");
//            super.onSocketWriteResponse(info, action, data);
//        }
//
//        @Override
//        public void onPulseSend(ConnectionInfo info, IPulseSendable data) {
//            Log.d(TAG, "客户端发送心跳包");
//            super.onPulseSend(info, data);
//        }
//    };
}
