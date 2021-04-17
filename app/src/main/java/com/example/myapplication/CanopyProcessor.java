package com.example.myapplication;

//import android.graphics.Bitmap;

import android.graphics.Bitmap;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.core.Core.split;

public class CanopyProcessor {
    final private Bitmap imgR;

    public CanopyProcessor(Bitmap img) {
        this.imgR = img;
    }

    public Bitmap getImg() {
        return imgR;
    }

    public static Mat getMat(Bitmap imgR){
        Mat mat = new Mat();
        Utils.bitmapToMat(imgR, mat);
        return mat;
    }

    public Mat getCIELab(){
        Mat mat = new Mat();
        Imgproc.cvtColor(getMat(imgR), mat, Imgproc.COLOR_BGR2Lab);
        return mat;
    }

    public void processImg(){

        // Get x, y, z array
        final Mat img_cie = getCIELab();
        List<Mat> channels = new ArrayList<Mat>(3);
        split(img_cie, channels);
        Log.d("Mine", String.valueOf(img_cie.channels()));
        Log.d("Mine", String.valueOf(img_cie.cols()));

        final double[][] x = new double[img_cie.rows()][img_cie.cols()];
        final double[][] y = new double[img_cie.rows()][img_cie.cols()];
        final double[][] z = new double[img_cie.rows()][img_cie.cols()];

        final Mat s = new Mat();
        Core.add(channels.get(0), channels.get(1), s);
        Core.add(channels.get(2), s, s);
        Log.d("Mine", String.valueOf(s.channels()));


        // Contrasted Ground Shadow
        final double[][] cg = new double[img_cie.rows()][img_cie.cols()];
        final Thread getcg = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int j=0;j<img_cie.cols();j++){
                    for (int i=0;i<img_cie.rows();i++){
                        if (z[i][j] != 0){
                            cg[i][j] = x[i][j]*y[i][j]/z[i][j];
                        }
                        else{
                            cg[i][j] = x[i][j]*y[i][j];
                        }
                        if (cg[i][j]>0){
                            Log.d("Mine", String.valueOf(cg[i][j]));
                        }
                        Log.d("Mine", "Hey");
                    }
                }
            }
        });


        Thread getxyz = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int j=0;j<img_cie.cols();j++){
                    for (int i=0;i<img_cie.rows();i++){
                        if (s.get(i, j)[0] != 0){
                            x[i][j] = img_cie.get(i, j)[0]/s.get(i, j)[0];
                            y[i][j] = img_cie.get(i, j)[1]/s.get(i, j)[0];
                            z[i][j] = img_cie.get(i, j)[2]/s.get(i, j)[0];
                        }
                        else {
                            x[i][j] = img_cie.get(i, j)[0];
                            y[i][j] = img_cie.get(i, j)[1];
                            z[i][j] = img_cie.get(i, j)[2];
                        }
                    }
                }
                getcg.start();
            }
        });
        getxyz.start();
    }

    public Bitmap blackWhite(int th){
        Mat mat = getMat(imgR);
        Mat greyMat = new Mat();
        Imgproc.cvtColor(mat, greyMat, Imgproc.COLOR_RGB2GRAY);
        for (int i=0;i<mat.rows();i++){
            for (int j=0;j<mat.cols();j++){
                if (greyMat.get(i, j)[0] > th){
                    mat.put(i, j, 255.0);
                }
                else{
                    mat.put(i, j, 0.0);
                }
            }
        }
        return getBitmap(mat);
    }

    public static Bitmap getBitmap(Mat mat){
        Bitmap bmp = null;
        try {
            bmp = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.RGB_565);
            Utils.matToBitmap(mat, bmp);
        }
        catch (CvException e){
            Log.d("Exception",e.getMessage());
        }
        return bmp;
    }
}
