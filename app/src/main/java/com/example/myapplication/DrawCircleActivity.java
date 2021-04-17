package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class DrawCircleActivity extends AppCompatActivity {
    private Button backBtn;
    private Button nextBtn;
    private ImageView img;
    private float radius = SelectParametersActivity.getImgRad();
    Bitmap bmp = RotateActivity.getBmp();
//    private CanopyProcessor cmp = new CanopyProcessor(bmp);
    private BaseLoaderCallback mOpenCVCallBack = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_circle);

        if(!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this, mOpenCVCallBack);
        }else{
            mOpenCVCallBack.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }


        backBtn=findViewById(R.id.backBtn);
        nextBtn=findViewById(R.id.nextBtn);
        img=findViewById(R.id.imageView);


        Mat mat = new Mat();
        Utils.bitmapToMat(bmp, mat);
        Mat greyMat = new Mat();
        Imgproc.cvtColor(mat, greyMat, Imgproc.COLOR_BGR2Lab);



        Bitmap finalBmp = Bitmap.createBitmap(greyMat.cols(), greyMat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(greyMat, finalBmp);
        img.setImageBitmap(finalBmp);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DrawCircleActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DrawCircleActivity.this, PolygonActivity.class);
                startActivity(intent);
            }
        });
    }

    public Bitmap getCircledBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, radius*2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
