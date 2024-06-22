package com.example.mini_proect;

import android.graphics.Bitmap;

public class AdminViewItem {
    private Bitmap image;
    private String predictedClass;

    public AdminViewItem(Bitmap image, String predictedClass) {
        this.image = image;
        this.predictedClass = predictedClass;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getPredictedClass() {
        return predictedClass;
    }
}
