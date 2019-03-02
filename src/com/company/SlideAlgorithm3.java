package com.company;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SlideAlgorithm3 implements SlideAlgorithm {

    //sort pics by num tags. filter horizontal. alternatate min max tags

    public final PictureInput input;

    public SlideAlgorithm3(PictureInput input) {
        this.input = input;
    }

    public SlideOutput calculate(){

        input.pictures.sort(Comparator.comparingInt(o -> o.tags.size()));

        List<Picture> horizontalPicturesSortedByNumTags = input.pictures.stream()
                .filter(picture -> picture.isHorizontal)
                .collect(Collectors.toList());

        ArrayList<Slide> slides = new ArrayList<>();

        while (horizontalPicturesSortedByNumTags.size() > 0){

            slides.add(new Slide(horizontalPicturesSortedByNumTags.remove(0).id));
            if(horizontalPicturesSortedByNumTags.isEmpty()) break;
            slides.add(new Slide(horizontalPicturesSortedByNumTags.remove(horizontalPicturesSortedByNumTags.size()-1).id));
        }

        return new SlideOutput(slides);
    }

}
