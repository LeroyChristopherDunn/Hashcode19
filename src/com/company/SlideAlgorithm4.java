package com.company;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SlideAlgorithm4 implements SlideAlgorithm {

    //horizontal slides sorted by num tags

    public final PictureInput input;

    public SlideAlgorithm4(PictureInput input) {
        this.input = input;
    }

    public SlideOutput calculate(){

        input.pictures.sort(Comparator.comparingInt(o -> o.tags.size()));

        List<Slide> horizontalSlidesSortedByNumTags = input.pictures.stream()
                .filter(picture -> picture.isHorizontal)
                .map(picture -> new Slide(picture.id))
                .collect(Collectors.toList());

        return new SlideOutput(horizontalSlidesSortedByNumTags);
    }

}
