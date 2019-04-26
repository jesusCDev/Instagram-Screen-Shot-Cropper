package com.allvens.instaBatch.image_parameter_system.analysis;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import com.allvens.instaBatch.image_parameter_system.position.Cluster;
import com.allvens.instaBatch.image_parameter_system.position.Image_Tile;
import com.allvens.instaBatch.image_parameter_system.position.Point;
import com.allvens.instaBatch.image_parameter_system.position.Rectangle_Parameter;

import java.util.ArrayList;

public class Analysis_General {

    private int NUM_ROWS_BOARD;
    private int NUM_COLS_BOARD;

    public Analysis_General(int rows, int cols){
        NUM_ROWS_BOARD = rows;
        NUM_COLS_BOARD = cols;
    }

    public Cluster get_BiggestCluster(Image_Tile[] tiles) {
        int tileCounter = 0;
        Image_Tile currentTile;
        ArrayList<Cluster> clusters = new ArrayList<>();

        int clusterID = 0;
        int clusterIDTracker = 0;

        int topClusterID = 0;
        int leftClusterID = 0;

        for(int row = 0; row < NUM_ROWS_BOARD; row++) {
            for (int col = 0; col < NUM_COLS_BOARD; col++) {
                currentTile = tiles[tileCounter];
                if(!currentTile.get_IsWhiteSpace()){

                    // check top tile
                    if(row != 0) topClusterID = check_TileCluster(tiles[(tileCounter - NUM_COLS_BOARD)]);
                    // check before tile
                    if(col != 0) leftClusterID = check_TileCluster(tiles[(tileCounter - 1)]);

                    // one or both clusters have an ID
                    if(topClusterID != 0 || leftClusterID != 0){
                        // add to none zero cluster
                        if((topClusterID == 0) || (leftClusterID == 0)){
                            if(topClusterID != 0){
                                clusterID = topClusterID;
                            }else{
                                clusterID = leftClusterID;
                            }
                        }
                        // add to cluster or absorb
                        else{
                            if(topClusterID == leftClusterID){
                                clusterID = topClusterID;
                            }else{
                                // absorbing clusters
                                for(Image_Tile tile: getCluster_WithID(clusters, leftClusterID).get_ImageTiles()){
                                    tiles[tile.getIndex_OfArray()].setClusterID(topClusterID);
                                }
                                clusters = absorb_Cluster(getCluster_WithID(clusters, topClusterID), getCluster_WithID(clusters, leftClusterID), clusters);
                                clusterID = topClusterID;
                            }
                        }
                    }

                    if(clusterID == 0){
                        currentTile.setClusterID(clusterIDTracker);
                        clusters.add(create_NewCluster((clusterIDTracker), currentTile));
                        clusterIDTracker++;
                    }else{
                        currentTile.setClusterID(clusterID);
                        getCluster_WithID(clusters, clusterID).add_Tile(currentTile);
                    }

                    clusterID = 0;
                    topClusterID = 0;
                    leftClusterID = 0;
                }
                tileCounter++;
            }
        }

        // might have to redo this
        Cluster biggestCluster = getCluster_WithID(clusters, 0);
        for(Cluster cluster: clusters){
            if(biggestCluster.get_ImageTiles().size() < cluster.get_ImageTiles().size()){
                biggestCluster = cluster;
            }
        }

        if(biggestCluster == null || (((int)((double)(NUM_ROWS_BOARD * NUM_COLS_BOARD) * 0.25) > biggestCluster.get_ImageTiles().size()))){
            return getGeneralSquareInPicture(tiles);
        }


        return biggestCluster;
    }

    private Cluster getGeneralSquareInPicture(Image_Tile[] tiles){

        int startingRow = (int)(NUM_ROWS_BOARD * 0.2);
        int numberRowsForGeneralImage = (int)(NUM_ROWS_BOARD * 0.3);

        Cluster cluster = new Cluster();
        cluster.set_GeneralSquareFlag(true);
        int tileCounter = startingRow * NUM_COLS_BOARD;
        for(int row = startingRow; row < NUM_ROWS_BOARD; row++) {
            for (int col = 0; col < NUM_COLS_BOARD; col++) {
                cluster.add_Tile(tiles[tileCounter]);
                tileCounter++;
            }
            if((numberRowsForGeneralImage + startingRow) < row){
                break;
            }
        }

        return cluster;
    }

    private Cluster getCluster_WithID(ArrayList<Cluster> clusters, int clusterID){
        for(Cluster cluster: clusters){
            if(cluster.getClusterID() == clusterID) return cluster;
        }
        return null;
    }

    private ArrayList<Cluster> absorb_Cluster(Cluster master, Cluster servant, ArrayList<Cluster> clusters){
        master.add_Tiles(servant.get_ImageTiles());
        clusters.remove(servant);
        return clusters;
    }

    private Cluster create_NewCluster(int clusterID, Image_Tile tile){
        return new Cluster(clusterID, tile);
    }

    private int check_TileCluster(Image_Tile tile) {
        if(tile.isPartOfCluster()){
            return tile.getClusterID();
        }else{
            return 0;
        }
    }

    public Image_Tile[] create_Board(Rectangle_Parameter imageSection) {
        Image_Tile[] board = new Image_Tile[NUM_COLS_BOARD * NUM_ROWS_BOARD];

        int colsWidth = (imageSection.get_Width()/ NUM_COLS_BOARD);
        int rowsHeight = (imageSection.get_Height()/ NUM_ROWS_BOARD);

        int widthCounter = 0;
        int heightCounter = 0;

        int tileCounter = 0;
        int extraWidth = 0;
        int extraHeight = 0;

        for(int row = 0; row < NUM_ROWS_BOARD; row++){
            if(row == (NUM_ROWS_BOARD - 1)){
                extraHeight = (imageSection.get_Height() - (heightCounter + rowsHeight));
            }
            for(int col = 0; col < NUM_COLS_BOARD; col++){
                if(col == (NUM_COLS_BOARD - 1)){
                    extraWidth = (imageSection.get_Width() - (widthCounter + colsWidth));
                }

                board[tileCounter] = new Image_Tile(tileCounter,
                        new Point(imageSection.getTopLeft().get_X() + widthCounter, imageSection.getTopLeft().get_Y() + heightCounter),
                        new Point((imageSection.getTopLeft().get_X() + widthCounter + colsWidth + extraWidth), imageSection.getTopLeft().get_Y() + heightCounter),
                        new Point(imageSection.getTopLeft().get_X() + widthCounter, (imageSection.getTopLeft().get_Y() + heightCounter + rowsHeight + extraHeight)),
                        new Point((imageSection.getTopLeft().get_X() + widthCounter + colsWidth + extraWidth), (imageSection.getTopLeft().get_Y() + heightCounter + rowsHeight + extraHeight)));

                widthCounter += colsWidth;
                tileCounter += 1;
            }

            extraWidth = 0;
            extraHeight = 0;
            widthCounter = 0;
            heightCounter += rowsHeight;

        }
        return board;
    }

    public Image_Tile[] analysis_CheckTilesForWhiteSpace(Bitmap image, Image_Tile[] board) {
        Bitmap subImage;
        for(Image_Tile tile: board){
            subImage = Bitmap.createBitmap(image, tile.getTop_left().get_X(), tile.getTop_left().get_Y(), tile.getWidth(), tile.getHeight());
            tile.set_IsWhiteSpace(check_TileColor(subImage));
        }
        return board;
    }

    private boolean check_TileColor(Bitmap subImage){
        int pixelsChecked = 0;
        int mostlyBackground = 0;

        for(int y = 0; y < subImage.getHeight(); y += 2){
            for(int x = 0; x < subImage.getWidth(); x += 2){
                if(check_IfColorIsGreyOrAbove(subImage.getPixel(x,y))){
                    mostlyBackground++;
                }
                pixelsChecked++;
            }
        }
        return ((pixelsChecked/2) < mostlyBackground);
    }

    private boolean check_IfColorIsGreyOrAbove(int currentColor){
        return ((Color.red(currentColor) > 220) && (Color.blue(currentColor) > 220) && (Color.green(currentColor) > 220));
    }
}
