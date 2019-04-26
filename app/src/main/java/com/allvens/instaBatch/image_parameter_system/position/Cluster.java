package com.allvens.instaBatch.image_parameter_system.position;

import java.util.ArrayList;

public class Cluster {

    private int clusterID;
    private ArrayList<Image_Tile> tiles = new ArrayList<>();
    private boolean generalSquareFlag = false;

    public Cluster(int clusterID, Image_Tile tile) {
        this.clusterID = clusterID;
        tiles.add(tile);
    }

    public Cluster(){}

    public void add_Tile(Image_Tile currentTile) {
        tiles.add(currentTile);
    }

    public ArrayList<Image_Tile> get_ImageTiles(){
        return tiles;
    }

    public void add_Tiles(ArrayList<Image_Tile> imageTiles) {
        tiles.addAll(imageTiles);
    }

    public int getClusterID() {
        return clusterID;
    }

    public void set_GeneralSquareFlag(boolean generalSquareFlag) {
        this.generalSquareFlag = generalSquareFlag;
    }

    public boolean get_GeneralSquareFlag() {
        return generalSquareFlag;
    }
}
