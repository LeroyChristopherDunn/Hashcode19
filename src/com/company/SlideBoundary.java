package com.company;

import java.util.ArrayList;
import java.util.List;

public class SlideBoundary {

    public final int index;
    public final Slide2 start;
    public final Slide2 end;

    public SlideBoundary(int index, Slide2 start, Slide2 end) {
        this.index = index;
        this.start = start;
        this.end = end;
    }

    public static int transitionValue(List<SlideBoundary> slideBoundaries){

        List<Slide2> slides = new ArrayList<>();

        for (SlideBoundary slideBoundary : slideBoundaries) {
            slides.add(slideBoundary.start);
            slides.add(slideBoundary.end);
        }

        return Slide2.transitionValue(slides);
    }
}
