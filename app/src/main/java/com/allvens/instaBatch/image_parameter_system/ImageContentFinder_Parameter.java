package com.allvens.instaBatch.image_parameter_system;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import com.allvens.instaBatch.image_parameter_system.analysis.Analysis_General;
import com.allvens.instaBatch.image_parameter_system.analysis.Analysis_Precise;
import com.allvens.instaBatch.image_parameter_system.position.Cluster;
import com.allvens.instaBatch.image_parameter_system.position.Image_Tile;
import com.allvens.instaBatch.image_parameter_system.position.PositionRelativeToOriginalImage_Tracker;
import com.allvens.instaBatch.image_parameter_system.position.Rectangle_Parameter;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;

public class ImageContentFinder_Parameter {

    private Context context;
    private boolean addPadding;

    public ImageContentFinder_Parameter(Context context, Boolean addPadding){
        this.context = context;
        this.addPadding = addPadding;
    }

    private void pop(String message){
        Log.d("FixingWhiteSpaceSearch", message);
    }

    public Rectangle_Parameter get_ImageCropParameters(Uri path){
        Bitmap bitmap = get_Bitmap(path);

        if(bitmap != null){
            PositionRelativeToOriginalImage_Tracker positionRelativeToOriginalImage_tracker = new PositionRelativeToOriginalImage_Tracker(bitmap.getWidth(), bitmap.getHeight());
            Analysis_Precise analysis_precise = new Analysis_Precise(bitmap);

            // Find general image area
            Cluster general_ClusterOfImageTiles = find_GeneralImageLocation(bitmap, positionRelativeToOriginalImage_tracker.get_CurrentRect());
            positionRelativeToOriginalImage_tracker.set_PositionRecord(analysis_precise.get_RectangleParametersOnCluster(general_ClusterOfImageTiles));

            if((!general_ClusterOfImageTiles.get_GeneralSquareFlag())){
                // Add padding to found area
                positionRelativeToOriginalImage_tracker.set_PositionRecord(analysis_precise.add_PaddingToRectangle(positionRelativeToOriginalImage_tracker,
                        general_ClusterOfImageTiles.get_ImageTiles().get(0).getWidth(), general_ClusterOfImageTiles.get_ImageTiles().get(0).getHeight()));

                // Find exact image
                analysis_precise.set_IgnoreInstagramBarValue(positionRelativeToOriginalImage_tracker);
                positionRelativeToOriginalImage_tracker.set_PositionRecord(analysis_precise.get_ExactRectangleDimentions(positionRelativeToOriginalImage_tracker.get_CurrentRect()));

                // add padding to image
                if(addPadding) positionRelativeToOriginalImage_tracker.set_PositionRecord(analysis_precise.add_TopAndBottomLinePadding(positionRelativeToOriginalImage_tracker.get_CurrentRect()));
            }

            positionRelativeToOriginalImage_tracker.get_CurrentRect().set_ImagePath(path);
            return positionRelativeToOriginalImage_tracker.get_CurrentRect();

        }else{
            return null;
        }
    }

    private Bitmap get_Bitmap(Uri imagePath){
        try{
            return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(imagePath));
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }

    private Cluster find_GeneralImageLocation(Bitmap image, Rectangle_Parameter imageSection){
        int BOARD_ROWS = 15;
        int BOARD_COLS = 20;

        Analysis_General analysis_general = new Analysis_General(BOARD_ROWS, BOARD_COLS);
        Image_Tile[] tileBoard = analysis_general.create_Board(imageSection);
        Image_Tile[] tileBoard_DefinedWhiteSpaces = analysis_general.analysis_CheckTilesForWhiteSpace(image, tileBoard);
        return analysis_general.get_BiggestCluster(tileBoard_DefinedWhiteSpaces);
    }
}
