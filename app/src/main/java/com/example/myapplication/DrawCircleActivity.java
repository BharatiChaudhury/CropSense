package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class DrawCircleActivity extends AppCompatActivity {
    private Button backBtn;
    private Button nextBtn;
    private ImageView img;

    private SeekBar seekBar;
    private ProgressBar progressBar;

    private float radius = SelectParametersActivity.getImgRad();
    Bitmap bmp = getCircledBitmap(RotateActivity.getBmp());

    private Mat ExgF;
    private Mat cgF;
    private int progressStatus = 0;
    private Handler handler;

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

        handler = new Handler();
        backBtn=findViewById(R.id.backBtn);
        nextBtn=findViewById(R.id.nextBtn);
        img=findViewById(R.id.imageView);
        seekBar=findViewById(R.id.seekBar2);
        progressBar = findViewById(R.id.progress);

        final Mat mat = new Mat();
        Utils.bitmapToMat(bmp, mat);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Mat mat = new Mat();
                Utils.bitmapToMat(bmp, mat);
                final Mat greyMat = new Mat();
                final Mat finalMat = new Mat();
                Imgproc.cvtColor(mat, greyMat, Imgproc.COLOR_BGR2Lab);
                Imgproc.cvtColor(mat, finalMat, Imgproc.COLOR_BGR2RGB);

                final Mat Exg = new Mat(mat.rows(), mat.cols(), CvType.CV_8UC1, new Scalar(0));
                final Mat cg = new Mat(mat.rows(), mat.cols(), CvType.CV_8UC1, new Scalar(0));

                List<Mat> channels = new ArrayList(3);
                Core.split(mat,channels);

                double rMax = Core.minMaxLoc(channels.get(0)).maxVal;
                double gMax = Core.minMaxLoc(channels.get(1)).maxVal;
                double bMax = Core.minMaxLoc(channels.get(2)).maxVal;

                for (int j=0;j<greyMat.cols();j++){
                    for (int i=0;i<greyMat.rows();i++){
                        progressStatus++;
                        double s = greyMat.get(i, j)[0] + greyMat.get(i, j)[1] + greyMat.get(i, j)[2];
                        double x, y, z;
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
                            cg.put(i, j, x*y/z);
                        }
                        else{
                            cg.put(i, j, x*y);
                        }

                        double r = mat.get(i, j)[0]/rMax;
                        double g = mat.get(i, j)[1]/gMax;
                        double b = mat.get(i, j)[2]/bMax;
                        s = r + g + b;
                        r = r/s;
                        g = g/s;
                        b = b/s;
                        Exg.put(i, j, (2*g)-r-b);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress(progressStatus*100/(greyMat.cols()*greyMat.rows()));
                            }
                        });
                    }
                }

                getFinalMat(Exg, cg, mat, finalMat, 0.21);
                ExgF = Exg;
                cgF = cg;
                Bitmap finalBmp = Bitmap.createBitmap(greyMat.cols(), greyMat.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(finalMat, finalBmp);
                img.setImageBitmap(finalBmp);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }).start();

        img.setImageBitmap(bmp);
        final double[] progress = new double[1];

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progress[0] = i/50.0;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                final Mat finalMat = new Mat();
                Imgproc.cvtColor(mat, finalMat, Imgproc.COLOR_BGR2RGB);
                getFinalMat(ExgF, cgF, mat, finalMat, 0.21+progress[0]);
                Bitmap finalBmp = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(finalMat, finalBmp);
                img.setImageBitmap(finalBmp);
            }
        });

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

    public void getFinalMat(Mat Exg, Mat cg, Mat mat, Mat finalMat, double t){
        Mat ExgF = new Mat(Exg.rows(), Exg.cols(), CvType.CV_8UC1, new Scalar(0));
        Imgproc.threshold(Exg, ExgF, t, 255.0, Imgproc.THRESH_BINARY);
        Mat f = new Mat(Exg.rows(), Exg.cols(), CvType.CV_8UC1, new Scalar(0));
        Core.subtract(ExgF, cg, f);
        Core.copyTo(mat, finalMat, f);
        Imgproc.cvtColor(finalMat, finalMat, Imgproc.COLOR_BGR2GRAY);
        Imgproc.threshold(finalMat, finalMat, 1, 255.0, Imgproc.THRESH_BINARY);
    }

    public double getPercentCover(Mat mat){
        double totalPixels = mat.rows()*mat.cols();
        double coverPixels = Core.countNonZero(mat);
        return (coverPixels/totalPixels);
    }

    public Bitmap getCircledBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle((float) (bitmap.getWidth()/2.0), (float) (bitmap.getHeight()/2.0), radius*2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
