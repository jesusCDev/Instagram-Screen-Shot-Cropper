package com.allvens.instaBatch.image_parameter_system.position;

public class Point {

    private int x;
    private int y;

    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void set_X(int x){
        this.x = x;
    }

    public void set_Y(int y){
        this.y = y;
    }

    public int get_X(){
        return x;
    }
    public int get_Y(){
        return y;
    }
}
