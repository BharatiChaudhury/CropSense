package com.example.myapplication;

import java.util.List;

public class upload {
    private float Dist;
    private float Degree;
    private float Radius;
    private List latLngList;
    private String CropType;
    private String GrowthStage;

    public String imageName;
    public String imageUrl;


    public upload(String name,String url){
        this.imageName=name;
        this.imageUrl=url;
    }
    public String getImageName(){
        return imageName;
    }

    public String getImageUrl(){
        return imageUrl;
    }

}
