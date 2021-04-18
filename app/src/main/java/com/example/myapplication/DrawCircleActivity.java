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
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class DrawCircleActivity extends AppCompatActivity {
    private Button backBtn;
    private Button nextBtn;
    private ImageView img;
    private float radius = SelectParametersActivity.getImgRad();
    Bitmap bmp = RotateActivity.getBmp();
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
        Mat finalMat = new Mat();
        Imgproc.cvtColor(mat, greyMat, Imgproc.COLOR_BGR2Lab);
        Imgproc.cvtColor(mat, finalMat, Imgproc.COLOR_BGR2RGB);

//        Mat cg = new Mat(new Size(greyMat.rows(), greyMat.cols()), CV_64FC1);
//        Mat Exg = new Mat(new Size(mat.rows(), mat.cols()), CV_64FC1);
//        Mat F = new Mat(new Size(mat.rows(), mat.cols()), CV_64FC1);
//        Mat finalMat = new Mat(new Size(mat.rows(), mat.cols()), CV_8UC3);

        List<Mat> channels = new ArrayList(3);
        Core.split(mat,channels);

        double rMax = Core.minMaxLoc(channels.get(0)).maxVal;
        double gMax = Core.minMaxLoc(channels.get(1)).maxVal;
        double bMax = Core.minMaxLoc(channels.get(2)).maxVal;

        for (int j=0;j<greyMat.cols();j++){
            for (int i=0;i<greyMat.rows();i++){
                double s = greyMat.get(i, j)[0] + greyMat.get(i, j)[1] + greyMat.get(i, j)[2];
                double x, y, z, cg, Exg;
                if ((int)s != 0){
                    x = greyMat.get(i, j)[0] / s;
                    y = greyMat.get(i, j)[1] / s;
                    z = greyMat.get(i, j)[2] / s;
                }
                else{
                    x = greyMat.get(i, j)[0];
                    y = greyMat.get(i, j)[1];
                    z = greyMat.get(i, j)[2];
                }
                if ((int)z != 0){
                    cg = x*y/z;
                }
                else{
                    cg = x*y;
                }

                double r = mat.get(i, j)[0]/rMax;
                double g = mat.get(i, j)[1]/gMax;
                double b = mat.get(i, j)[2]/bMax;
                s = r + g + b;
                r = r/s;
                g = g/s;
                b = b/s;
                Exg = (2*g)-r-b;
                double t = 150.0;
                if (Exg>=t){
                    Exg = 255.0;
                }
                else{
                    Exg = 0.0;
                }
                if (Exg - cg >= 0){
                    finalMat.put(i, j, 255.0, 255.0, 255.0);
                }
                else{
                    finalMat.put(i, j, 0.0, 0.0, 0.0);
                }
            }
        }

        Bitmap finalBmp = Bitmap.createBitmap(greyMat.cols(), greyMat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(finalMat, finalBmp);
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
