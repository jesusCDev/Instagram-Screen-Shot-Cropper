package com.allvens.instaBatch.image_parameter_system.position;

public class Image_Tile {

    private Point top_left;
    private Point top_right;
    private Point bottom_left;
    private Point bottom_right;

    private int height;
    private int width;
    private int clusterID;

    private boolean partOfCluster = false;
    private boolean isWhiteSpace;

    private int index_OfArray;

    public Image_Tile(int index_OfArray, Point top_left, Point top_right, Point bottom_left, Point bottom_right){
        this.index_OfArray = index_OfArray;

        this.top_left = top_left;
        this.top_right = top_right;
        this.bottom_left = bottom_left;
        this.bottom_right = bottom_right;

        this.width = (top_right.get_X() - top_left.get_X());
        this.height = (bottom_left.get_Y() - top_left.get_Y());
    }

    public int getIndex_OfArray(){
        return index_OfArray;
    }

    public int getClusterID() {
        return clusterID;
    }

    public void setClusterID(int clusterID) {
        partOfCluster = true;
        this.clusterID = clusterID;
    }

    public Point getTop_left() {
        return top_left;
    }

    public void setTop_left(Point top_left) {
        this.top_left = top_left;
    }

    public Point getBottom_left() {
        return bottom_left;
    }

    public void setBottom_left(Point bottom_left) {
        this.bottom_left = bottom_left;
    }

    public Point getTop_right() {
        return top_right;
    }

    public void setTop_right(Point top_right) {
        this.top_right = top_right;
    }

    public Point getBottom_right() {
        return bottom_right;
    }

    public void setBottom_right(Point bottom_right) {
        this.bottom_right = bottom_right;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public boolean isPartOfCluster() {
        return partOfCluster;
    }

    public boolean get_IsWhiteSpace(){
        return isWhiteSpace;
    }

    public void set_IsWhiteSpace(boolean isWhiteSpace) {
        this.isWhiteSpace = isWhiteSpace;
    }
}
