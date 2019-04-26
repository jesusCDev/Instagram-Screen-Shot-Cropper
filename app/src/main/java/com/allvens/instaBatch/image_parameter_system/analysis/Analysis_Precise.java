package com.allvens.instaBatch.image_parameter_system.analysis;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import com.allvens.instaBatch.image_parameter_system.position.*;

public class Analysis_Precise {

    private Bitmap image;
    private int ignoreBarValue = 0;

    public Analysis_Precise(Bitmap image){
        this.image = image;
        // todo might have to do this everytime and just pass it in here
//        this.image.setConfig(RGB_565);

    }

    public void set_IgnoreInstagramBarValue(PositionRelativeToOriginalImage_Tracker positionRelativeToOriginalImage_tracker){
        if(image.getWidth() == positionRelativeToOriginalImage_tracker.get_CurrentRect().get_Width()) ignoreBarValue = (int)(image.getWidth() * 0.05);
    }


    public Rectangle_Parameter get_ExactRectangleDimentions(Rectangle_Parameter rectangle_parameter) {
        rectangle_parameter.set_Top(find_TopPoint(rectangle_parameter));
        rectangle_parameter.set_Bottom(find_BottomPoint(rectangle_parameter));
        rectangle_parameter.set_Left(find_LeftPoint(rectangle_parameter));
        rectangle_parameter.set_Right(find_RightPoint(rectangle_parameter));

        return rectangle_parameter;
    }

    private boolean check_IfColorIsGreyOrAbove(int currentColor){
        return !((Color.red(currentColor) > 220) && (Color.blue(currentColor) > 220) && (Color.green(currentColor) > 220));
    }

    private int find_TopPoint(Rectangle_Parameter rectangle_parameter){
        int topPixel = rectangle_parameter.getTopLeft().get_Y();
        boolean line_InstagramBackgroundColor = true;

        for(int y = (rectangle_parameter.getTopLeft().get_Y() + (int)(rectangle_parameter.get_Height() * 0.3)); y > rectangle_parameter.getTopLeft().get_Y(); y -= 10){
            for(int x = rectangle_parameter.getTopLeft().get_X(); x < (rectangle_parameter.getTopRight().get_X() - ignoreBarValue); x += 5){
                if(check_IfColorIsGreyOrAbove(image.getPixel(x, y))){
                    line_InstagramBackgroundColor = false;
                    break;
                }
            }
            if(line_InstagramBackgroundColor){
                topPixel = y;
                break;
            }
            line_InstagramBackgroundColor = true;
        }

        line_InstagramBackgroundColor = true;

        for(int y = (topPixel + 10); y > rectangle_parameter.getTopLeft().get_Y(); y -= 1){
            for(int x = rectangle_parameter.getTopLeft().get_X(); x < (rectangle_parameter.getTopRight().get_X() - ignoreBarValue); x += 5){
                if(check_IfColorIsGreyOrAbove(image.getPixel(x, y))){
                    line_InstagramBackgroundColor = false;
                    break;
                }
            }
            if(line_InstagramBackgroundColor){
                topPixel = (y + 1);
                break;
            }
            line_InstagramBackgroundColor = true;
        }
        return topPixel;
    }

    private int find_BottomPoint(Rectangle_Parameter rectangle_parameter){
        int bottomPixel = rectangle_parameter.getBottomLeft().get_Y();
        boolean line_InstagramBackgroundColor = true;

        for(int y = (rectangle_parameter.getBottomLeft().get_Y() - (int)(rectangle_parameter.get_Height() * 0.3)); y < rectangle_parameter.getBottomLeft().get_Y(); y += 10){
            for(int x = rectangle_parameter.getTopLeft().get_X(); x < (rectangle_parameter.getTopRight().get_X() - ignoreBarValue); x += 5){
                if(check_IfColorIsGreyOrAbove(image.getPixel(x, y))){
                    line_InstagramBackgroundColor = false;
                    break;
                }
            }
            if(line_InstagramBackgroundColor){
                bottomPixel = y;
                break;
            }
            line_InstagramBackgroundColor = true;
        }

        line_InstagramBackgroundColor = true;

        for(int y = (bottomPixel - 10); y < rectangle_parameter.getBottomLeft().get_Y(); y += 1){
            for(int x = rectangle_parameter.getTopLeft().get_X(); x < (rectangle_parameter.getTopRight().get_X() - ignoreBarValue); x += 5){
                if(check_IfColorIsGreyOrAbove(image.getPixel(x, y))){
                    line_InstagramBackgroundColor = false;
                    break;
                }
            }
            if(line_InstagramBackgroundColor){
                bottomPixel = (y - 1);
                break;
            }
            line_InstagramBackgroundColor = true;
        }
        
        return bottomPixel;
    }

    private int find_LeftPoint(Rectangle_Parameter rectangle_parameter) {
        int leftPixel = rectangle_parameter.getTopLeft().get_X();
        boolean line_InstagramBackgroundColor = true;

        for(int x = (rectangle_parameter.getTopLeft().get_X() + (int)(rectangle_parameter.get_Width() * 0.3)); x > rectangle_parameter.getTopLeft().get_X(); x -= 10){
            for(int y = rectangle_parameter.getTopLeft().get_Y(); y < (rectangle_parameter.getBottomLeft().get_Y()); y += 5){
                if(check_IfColorIsGreyOrAbove(image.getPixel(x, y))){
                    line_InstagramBackgroundColor = false;
                    break;
                }
            }
            if(line_InstagramBackgroundColor){
                leftPixel = x;
                break;
            }
            line_InstagramBackgroundColor = true;
        }

        line_InstagramBackgroundColor = true;

        for(int x = (leftPixel + 10); x > rectangle_parameter.getTopLeft().get_X(); x -= 1){
            for(int y = rectangle_parameter.getTopLeft().get_Y(); y < (rectangle_parameter.getBottomLeft().get_Y()); y += 5){
                if(check_IfColorIsGreyOrAbove(image.getPixel(x, y))){
                    line_InstagramBackgroundColor = false;
                    break;
                }
            }
            if(line_InstagramBackgroundColor){
                leftPixel = (x + 1);
                break;
            }
            line_InstagramBackgroundColor = true;
        }
        return leftPixel;
    }

    private int find_RightPoint(Rectangle_Parameter rectangle_parameter) {
        int rightPixel = rectangle_parameter.getTopRight().get_X();
        boolean line_InstagramBackgroundColor = true;

        for(int x = (rectangle_parameter.getTopRight().get_X() - (int)(rectangle_parameter.get_Width() * 0.3)); x < rectangle_parameter.getTopRight().get_X(); x += 10){
            for(int y = rectangle_parameter.getTopLeft().get_Y(); y < rectangle_parameter.getBottomLeft().get_Y(); y += 5){
                if(check_IfColorIsGreyOrAbove(image.getPixel(x, y))){
                    line_InstagramBackgroundColor = false;
                    break;
                }
            }
            if(line_InstagramBackgroundColor){
                rightPixel = x;
                break;
            }
            line_InstagramBackgroundColor = true;
        }

        line_InstagramBackgroundColor = true;

        for(int x = (rightPixel - 10); x < rectangle_parameter.getTopRight().get_X(); x += 1){
            for(int y = rectangle_parameter.getTopLeft().get_Y(); y < rectangle_parameter.getBottomLeft().get_Y(); y += 5){
                if(check_IfColorIsGreyOrAbove(image.getPixel(x, y))){
                    line_InstagramBackgroundColor = false;
                    break;
                }
            }
            if(line_InstagramBackgroundColor){
                rightPixel = (x - 1);
                break;
            }
            line_InstagramBackgroundColor = true;
        }
        return rightPixel;
    }

    public Rectangle_Parameter get_RectangleParametersOnCluster(Cluster cluster) {
        int left_X = cluster.get_ImageTiles().get(0).getTop_left().get_X();
        int right_X = cluster.get_ImageTiles().get(0).getTop_right().get_X();
        int topY  = cluster.get_ImageTiles().get(0).getTop_left().get_Y();
        int bottomY = cluster.get_ImageTiles().get(0).getBottom_left().get_Y();

        for(Image_Tile tile: cluster.get_ImageTiles()){
            if(left_X > tile.getTop_left().get_X()){
                left_X = tile.getTop_left().get_X();
            }
            if(right_X < tile.getTop_right().get_X()){
                right_X = tile.getTop_right().get_X();
            }

            if(topY  > tile.getTop_left().get_Y()){
                topY  = tile.getTop_left().get_Y();
            }
            if(bottomY < tile.getBottom_left().get_Y()){
                bottomY = tile.getBottom_left().get_Y();
            }
        }

        return new Rectangle_Parameter(new Point(left_X, topY), new Point(right_X, topY),
                new Point(left_X, bottomY), new Point(right_X, bottomY));
    }

    /**
     * Increases area around rect for margin of error
     */
    public Rectangle_Parameter add_PaddingToRectangle(PositionRelativeToOriginalImage_Tracker board_tracker, int colWidth, int rowHeight) {

        Rectangle_Parameter originalImageBoard = board_tracker.get_OriginalRec();
        Rectangle_Parameter childBoard = board_tracker.get_CurrentRect();

        // add to left - col - subtract
        int unused_imageWidthToTheLeft = (childBoard.getTopLeft().get_X());
        if(unused_imageWidthToTheLeft != 0){
            if(unused_imageWidthToTheLeft > colWidth){
                childBoard.set_Left(childBoard.getTopLeft().get_X() - colWidth);
            }else{
                childBoard.set_Left(childBoard.getTopLeft().get_X() - unused_imageWidthToTheLeft);
            }
        }

        // add to right - col - add
        int unused_imageWidthToTheRight = (originalImageBoard.get_Width() - childBoard.getTopRight().get_X());
        if(unused_imageWidthToTheRight != originalImageBoard.get_Width()){
            if(unused_imageWidthToTheRight > colWidth){
                childBoard.set_Right(childBoard.getTopRight().get_X() + colWidth);
            }else{
                childBoard.set_Right(childBoard.getTopRight().get_X() + unused_imageWidthToTheRight);
            }
        }

        // add to top - row - subtract
        int unused_imageHeightToTheTop = (childBoard.getTopLeft().get_Y());
        if(unused_imageHeightToTheTop != 0){
            if(unused_imageHeightToTheTop > rowHeight){
                childBoard.set_Top(childBoard.getTopLeft().get_Y() - rowHeight);
            }else{
                childBoard.set_Top(childBoard.getTopLeft().get_Y() - unused_imageHeightToTheTop);
            }
        }

        // add to bottom - row - add
        int unused_imageHeightToTheBottom = (originalImageBoard.get_Height() - childBoard.getBottomLeft().get_Y());
        if(unused_imageHeightToTheBottom != originalImageBoard.get_Height()){
            if(unused_imageHeightToTheBottom > rowHeight){
                childBoard.set_Bottom(childBoard.getBottomLeft().get_Y() + rowHeight);
            }else{
                childBoard.set_Bottom(childBoard.getBottomLeft().get_Y() + unused_imageHeightToTheBottom);
            }
        }

        return childBoard;
    }

    public Rectangle_Parameter add_TopAndBottomLinePadding(Rectangle_Parameter currentRect) {
        currentRect.set_Top(currentRect.getTopLeft().get_Y() + 3);
        currentRect.set_Bottom(currentRect.getBottomLeft().get_Y() - 3);
        return currentRect;
    }
}
