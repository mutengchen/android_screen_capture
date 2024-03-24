package com.example.nishuihan_helper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    Button test;
    TextView textView;
    String resultaa = "result:";
    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent != null && Intent.ACTION_VIEW.equals(intent.getAction())) {
            // 获取 URI
            Uri uri = intent.getData();
            if (uri != null) {
                // 获取 URI 中的参数
                String param1 = uri.getQueryParameter("param1");
                String param2 = uri.getQueryParameter("param2");
                // 这里你可以根据参数进行相应的逻辑处理
                if (param1 != null) {
                    // 执行相应的逻辑
                    Log.d(TAG, "onNewIntent: "+param1);
                    Log.d(TAG, "onNewIntent: "+param2);
                }
            }
        }
    }
    private MediaProjectionManager mediaProjectionManager;
    private Intent mediaProjectionPermissionIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);

        //让用户可以在根目录下创建文件夹
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            if (!Environment.isExternalStorageManager()) {
//                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
//                intent.addCategory("android.intent.category.DEFAULT");
//                intent.setData(Uri.parse("package:" + getPackageName()));
//                startActivity(intent);
//            }
//        }
        textView = findViewById(R.id.result);
        test = findViewById(R.id.test);
        if (mediaProjectionPermissionIntent == null) {
            mediaProjectionPermissionIntent = mediaProjectionManager.createScreenCaptureIntent();
            startActivityForResult(mediaProjectionPermissionIntent, 1);
        }
//        test.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                File nsh_folder = new File(Environment.getExternalStorageDirectory() + "/Screenshots/nsh");
//                //遍历nsh_folder 里面的图片
//                if (!nsh_folder.exists()) {
//                    nsh_folder.mkdirs();
//                }
//                for(File file : nsh_folder.listFiles()){
//                    //file 转成bitmap,截取子区域（200,200）,保存到nsh_folder
//                    //开始时间
//                    long startTime = System.currentTimeMillis();
//                    try {
//                        // 创建输入流并从文件中获取数据
//                        Log.d(TAG, "onClick: "+file.getAbsolutePath());
//                        InputStream inputStream = new FileInputStream(file);
//                        // 通过BitmapFactory工厂类加载位图对象
//                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                        resultaa += "加载图片完成:"+(System.currentTimeMillis()-startTime)+"ms\n";
//                        startTime = System.currentTimeMillis();
//                        //截取子区域
//                        Bitmap subBitmap = Bitmap.createBitmap(bitmap, 0, 0, 200, 200);
//                        //保存nsh_result 文件夹里面
//                        File nsh_result_folder = new File(Environment.getExternalStorageDirectory() + "/Screenshots/nsh_result/"+ UUID.randomUUID()+".png");
//                        FileOutputStream fos = new FileOutputStream(nsh_result_folder);
//                        subBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
//                        resultaa += "截取子区域，并压缩保存,保存图片完成:"+(System.currentTimeMillis()-startTime)+"ms\n";
//                        fos.flush();
//                        fos.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    textView.setText(resultaa);
//                }
//            }
//        });

        //获取intent的参数
        Intent intent = getIntent();
        if (intent != null && Intent.ACTION_VIEW.equals(intent.getAction())) {
            // 获取 URI
            Uri uri = intent.getData();
            if (uri != null) {
                // 获取 URI 中的参数
                String param1 = uri.getQueryParameter("param1");
                String param2 = uri.getQueryParameter("param2");
                // 这里你可以根据参数进行相应的逻辑处理
                if (param1 != null) {
                    Log.d(TAG, "param1: " + param1);
                    Log.d(TAG, "param2: " + param2);
                }
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}