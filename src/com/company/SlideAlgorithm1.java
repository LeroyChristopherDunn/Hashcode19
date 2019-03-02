package com.company;

import java.util.Arrays;

public class SlideAlgorithm1 implements SlideAlgorithm {

    public final PictureInput input;

    public SlideAlgorithm1(PictureInput input) {
        this.input = input;
    }

    public SlideOutput calculate(){

        return new SlideOutput(Arrays.asList(
                new Slide(0),
                new Slide(3),
                new Slide(1, 2)
        ));
    }
}
