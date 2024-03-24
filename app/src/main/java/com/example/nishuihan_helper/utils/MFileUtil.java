package com.example.nishuihan_helper.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MFileUtil {
    public static void saveBitmapToFile(Context context,Bitmap bitmap) {
        // 创建文件夹
        File folder = new File(context.getFilesDir(), "screenshots");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // 创建文件名
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = sdf.format(new Date());
        String fileName = "screenshot_" + timestamp + ".png";

        // 创建文件路径
        File file = new File(folder, fileName);

        try {
            // 创建文件输出流
            FileOutputStream fos = new FileOutputStream(file);

            // 将 Bitmap 写入文件
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

            // 关闭流
            fos.close();

            // 提示用户保存成功
            Toast.makeText(context, "截图已保存：" + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
