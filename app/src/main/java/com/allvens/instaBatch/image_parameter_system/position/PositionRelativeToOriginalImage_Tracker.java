package com.allvens.instaBatch.image_parameter_system.position;

public class PositionRelativeToOriginalImage_Tracker {

    private Rectangle_Parameter originalRec;
    private Rectangle_Parameter currentRec;

    public PositionRelativeToOriginalImage_Tracker(int originalImageWidth, int originalImageHeight){
        originalRec = new Rectangle_Parameter(new Point(0, 0), new Point(originalImageWidth, 0),
                new Point(0, originalImageHeight), new Point(originalImageWidth, originalImageHeight));
        currentRec = originalRec;
    }

    public Rectangle_Parameter get_CurrentRect(){
        return currentRec;
    }

    public void set_PositionRecord(Rectangle_Parameter newRec){
        currentRec = newRec;
    }

    public Rectangle_Parameter get_OriginalRec(){
        return originalRec;
    }
}
