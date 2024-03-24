package com.example.nishuihan_helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.example.nishuihan_helper.databinding.AbcBinding;
import com.example.nishuihan_helper.service.MediaService;
import com.example.nishuihan_helper.utils.ImageUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;

public class ScreenshotActivity extends Activity {
    private static final int REQUEST_MEDIA_PROJECTION = 1;

    private MediaProjectionManager mediaProjectionManager;
    private MediaProjection mediaProjection;
    private VirtualDisplay virtualDisplay;
    AbcBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = AbcBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);

        binding.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("~~~", "Requesting confirmation");
                Intent serviceIntent = new Intent(ScreenshotActivity.this, MediaService.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(serviceIntent);
                }

                Log.d("~~~", "Requesting confirmation");
                startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION);

            }
        });

        binding.btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("~~~", "Stop screen capture");
                stopScreenCapture();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MEDIA_PROJECTION) {
            if (resultCode != RESULT_OK) {
                Log.d("~~~", "User cancelled");
                return;
            }
            Log.d("~~~", "Starting screen capture");
            mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);
            virtualDisplay = mediaProjection.createVirtualDisplay(
                    "ScreenCapture",
                    ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight(), ScreenUtils.getScreenDensityDpi(),
                    DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                    imageReader.getSurface(), null, null
            );
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {

                    Image image = imageReader.acquireLatestImage();
                    if (image != null) {
                        Log.d("~~~", "get image: " + image);
                        binding.iv.setImageBitmap(ImageUtil.imageToBitmap(image));
                        image.close();
                    } else {
                        Log.d("~~~", "image == null");
                    }
                    stopScreenCapture();
                }
            }, 3000);
        }
    }

    private void stopScreenCapture() {
        Log.d("~~~", "stopScreenCapture, virtualDisplay = " + virtualDisplay);
        if (virtualDisplay != null) {
            virtualDisplay.release();
            virtualDisplay = null;
        }
    }

    private ImageReader  imageReader= ImageReader.newInstance(
            ScreenUtils.getScreenWidth(),
            ScreenUtils.getScreenHeight(),
            PixelFormat.RGBA_8888,
            1
            );
}
