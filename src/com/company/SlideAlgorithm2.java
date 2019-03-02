package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SlideAlgorithm2 implements SlideAlgorithm {

    //sort by horizontal ids, then vertical ids

    public final PictureInput input;

    public SlideAlgorithm2(PictureInput input) {
        this.input = input;
    }

    public SlideOutput calculate(){

        ArrayList<Slide> slides = new ArrayList<>();

        processHorizontalPhotos(slides);

        processVerticalPhotos(slides);

        return new SlideOutput(slides);
    }

    private void processVerticalPhotos(ArrayList<Slide> slides) {

        List<Picture> verticalPictures = input.pictures.stream()
                .filter(picture -> !picture.isHorizontal)
                .collect(Collectors.toList());

        for (int i = 0; i < verticalPictures.size(); i+=2) {
            slides.add(new Slide(verticalPictures.get(i).id, verticalPictures.get(i + 1).id));
        }

    }

    private void processHorizontalPhotos(ArrayList<Slide> slides) {

        List<Slide> horizontalPictureSlides = input.pictures.stream()
                .filter(picture -> picture.isHorizontal)
                .map(picture -> new Slide(picture.id))
                .collect(Collectors.toList());

        slides.addAll(horizontalPictureSlides);
    }
}
