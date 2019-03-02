package com.company;

import java.util.Arrays;
import java.util.List;

public class Slide {

    public final List<Integer> pictureIDs;

    private Slide(List<Integer> pictureIDs) {
        this.pictureIDs = pictureIDs;
        if(pictureIDs.size() > 2)
            throw new IllegalArgumentException("more than two pictures on slide");
    }

    public Slide(int pictureID1){
        this(Arrays.asList(pictureID1));
    }

    public Slide(int pictureID1, int pictureID2){
        this(Arrays.asList(pictureID1, pictureID2));
    }
}
