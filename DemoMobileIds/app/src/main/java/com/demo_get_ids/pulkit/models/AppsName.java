package com.demo_get_ids.pulkit.models;

import android.graphics.drawable.Drawable;

/**
 * Created by pulkit on 24/11/17.
 */

public class AppsName {

    Drawable icon;
    private String name;
    private double apkSize;

    public AppsName(String appName, Drawable icon, double apkSize) {
        this.name = appName;
        this.icon = icon;
        this.apkSize = apkSize;
    }

    public double getApkSize() {
        return apkSize;
    }

    public void setApkSize(double apkSize) {
        this.apkSize = apkSize;
    }

    public AppsName(String appName) {
        this.name = appName;
    }


    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
