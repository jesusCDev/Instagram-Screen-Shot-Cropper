package com.allvens.instaBatch.image_parameter_system.position;

import android.net.Uri;

public class Rectangle_Parameter {

    private Point topLeft;
    private Point topRight;
    private Point bottomLeft;
    private Point bottomRight;

    private Uri imagePath;
    private Uri newPath;

    public Rectangle_Parameter(Point topLeft, Point topRight, Point bottomLeft, Point bottomRight) {
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomLeft = bottomLeft;
        this.bottomRight = bottomRight;
    }

    public int get_Width(){
        return (topRight.get_X() - topLeft.get_X());
    }

    public int get_Height(){
        return (bottomLeft.get_Y() - topLeft.get_Y());
    }

    public Point getTopLeft() {
        return topLeft;
    }

    public void setTopLeft(Point topLeft) {
        this.topLeft = topLeft;
    }

    public Point getTopRight() {
        return topRight;
    }

    public void setTopRight(Point topRight) {
        this.topRight = topRight;
    }

    public Point getBottomLeft() {
        return bottomLeft;
    }

    public void setBottomLeft(Point bottomLeft) {
        this.bottomLeft = bottomLeft;
    }

    public Point getBottomRight() {
        return bottomRight;
    }

    public void setBottomRight(Point bottomRight) {
        this.bottomRight = bottomRight;
    }

    public void set_Top(int y) {
        topLeft.set_Y(y);
        topRight.set_Y(y);
    }
    public void set_Bottom(int y) {
        bottomLeft.set_Y(y);
        bottomRight.set_Y(y);
    }
    public void set_Left(int x) {
        topLeft.set_X(x);
        bottomLeft.set_X(x);
    }
    public void set_Right(int x) {
        topRight.set_X(x);
        bottomRight.set_X(x);
    }

    public void set_NewPath(Uri newPath){
        this.newPath = newPath;
    }

    public Uri get_NewPath(){
        return newPath;
    }

    public void set_ImagePath(Uri imagePath) {
        this.imagePath = imagePath;
    }

    public Uri get_ImagePath(){
        return imagePath;
    }
}
