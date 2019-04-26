package com.allvens.instaBatch.image_cropped_presenter;


import android.graphics.Bitmap;
import android.util.Log;
import com.allvens.instaBatch.assets.Constants;
import com.squareup.picasso.Transformation;

class CropSquare_Transformation implements Transformation {

    private ImageParameters_Parcelable image_parameters_parcelable;

    public CropSquare_Transformation(ImageParameters_Parcelable image_parameters_parcelable){
        this.image_parameters_parcelable = image_parameters_parcelable;
    }

    @Override public Bitmap transform(Bitmap source) {
        int x = image_parameters_parcelable.get_X();
        int y = image_parameters_parcelable.get_Y();
        Bitmap result = Bitmap.createBitmap(source, x, y, image_parameters_parcelable.get_Width(), image_parameters_parcelable.get_Height());
        if (result != source) {
            source.recycle();
        }
        return result;
    }

    @Override public String key() { return "Cropped: /n" +
            "X: " + image_parameters_parcelable.get_X() +
            "Y: " + image_parameters_parcelable.get_Y() +
            "Width: " + image_parameters_parcelable.get_Width() +
            "Height: " + image_parameters_parcelable.get_Height(); }
}