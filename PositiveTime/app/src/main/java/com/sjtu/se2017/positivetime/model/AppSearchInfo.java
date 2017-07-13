package com.sjtu.se2017.positivetime.model;

import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2017/7/12.
 */

public class AppSearchInfo {
    private Drawable image;
    private String appName;
    private String packageName;
    private double weight;
    private int installNum;
    private int categoryId;

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getInstallNum() {
        return installNum;
    }

    public void setInstallNum(int installNum) {
        this.installNum = installNum;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

}
