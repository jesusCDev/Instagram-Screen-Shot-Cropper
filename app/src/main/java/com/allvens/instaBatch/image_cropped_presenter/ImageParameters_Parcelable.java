package com.allvens.instaBatch.image_cropped_presenter;

import android.graphics.Rect;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import com.allvens.instaBatch.image_parameter_system.position.Rectangle_Parameter;

public class ImageParameters_Parcelable implements Parcelable {

    private int x;
    private int y;
    private int width;
    private int height;

    private Uri imagePath;

    public Uri get_ImagePath(){
        return imagePath;
    }

    public int get_X(){
        return x;
    }

    public int get_Y(){
        return y;
    }

    public int get_Width(){
        return width;
    }

    public int get_Height(){
        return height;
    }

    public ImageParameters_Parcelable(Rectangle_Parameter imageAndPars){
        imagePath = imageAndPars.get_ImagePath();
        x = imageAndPars.getTopLeft().get_X();
        y = imageAndPars.getTopLeft().get_Y();
        width = imageAndPars.get_Width();
        height = imageAndPars.get_Height();
    }

    protected ImageParameters_Parcelable(Parcel in) {
        x = in.readInt();
        y = in.readInt();
        width = in.readInt();
        height = in.readInt();
        imagePath = in.readParcelable(Uri.class.getClassLoader());
    }

    public void setNewRectValues(Rect rect){
        x = rect.left;
        y = rect.top;
        width = rect.right - rect.left;
        height = rect.bottom - rect.top;
    }

    public static final Creator<ImageParameters_Parcelable> CREATOR = new Creator<ImageParameters_Parcelable>() {
        @Override
        public ImageParameters_Parcelable createFromParcel(Parcel in) {
            return new ImageParameters_Parcelable(in);
        }

        @Override
        public ImageParameters_Parcelable[] newArray(int size) {
            return new ImageParameters_Parcelable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(x);
        dest.writeInt(y);
        dest.writeInt(width);
        dest.writeInt(height);
        dest.writeParcelable(imagePath, flags);
    }
}
