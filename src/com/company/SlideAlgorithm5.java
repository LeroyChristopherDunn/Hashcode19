package com.company;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SlideAlgorithm5 implements SlideAlgorithm {

    //horizontal slides sorted by num tags, add vertical slides, order by num tags

    public final PictureInput input;

    public SlideAlgorithm5(PictureInput input) {
        this.input = input;
    }

    public SlideOutput calculate(){

        input.pictures.sort(Comparator.comparingInt(o -> o.tags.size()));

        List<Slide> horizontalSlidesSortedByNumTags = input.pictures.stream()
                .filter(picture -> picture.isHorizontal)
                .map(picture -> new Slide(picture.id))
                .collect(Collectors.toList());

        processVerticalPhotos(horizontalSlidesSortedByNumTags);

        return new SlideOutput(horizontalSlidesSortedByNumTags);
    }

    private void processVerticalPhotos(List<Slide> slides) {

        List<Picture> verticalPictures = input.pictures.stream()
                .filter(picture -> !picture.isHorizontal)
                .collect(Collectors.toList());

        for (int i = 0; i < verticalPictures.size(); i+=2) {
            slides.add(new Slide(verticalPictures.get(i).id, verticalPictures.get(i + 1).id));
        }

    }

}
