package com.allvens.instaBatch.image_cropped_presenter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.allvens.instaBatch.R;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;

public class CropImages_Adapter extends RecyclerView.Adapter<CropImages_Adapter.MyViewHolder>{

    private Context context;
    private ArrayList<ImageParameters_Parcelable> imagesAndParameters;

    public CropImages_Adapter(Context mContext, ArrayList<ImageParameters_Parcelable> imageAndParameters){
        this.context = mContext;
        this.imagesAndParameters = imageAndParameters;
    }

    public void swap(ArrayList<ImageParameters_Parcelable> imagesAndParameters) {
        this.imagesAndParameters = imagesAndParameters;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CropImages_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(context);

        view = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Picasso.get().load(imagesAndParameters.get(position).get_ImagePath())
                .transform(new CropSquare_Transformation(imagesAndParameters.get(position)))
                .into(holder.iv);

        holder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnActionCropActivity(position);
            }
        });
    }

    private void btnActionCropActivity(int position){
        CropImage.activity(imagesAndParameters.get(position).get_ImagePath())
                .setInitialCropWindowRectangle(
                        new Rect(imagesAndParameters.get(position).get_X(), imagesAndParameters.get(position).get_Y(),
                                (imagesAndParameters.get(position).get_X() + imagesAndParameters.get(position).get_Width()),
                                (imagesAndParameters.get(position).get_Y() + imagesAndParameters.get(position).get_Height())))
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAllowCounterRotation(false)
                .setAllowRotation(false)
                .setAllowFlipping(false)
                .start((Activity)context);
    }

    @Override
    public int getItemCount() {
        return imagesAndParameters.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView iv;
        ConstraintLayout imageHolder;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.ivCropPresenter);
            iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
            iv.setPadding(8,8,8,8);
            imageHolder = itemView.findViewById(R.id.cImageItemViewContainer);
        }
    }

}
