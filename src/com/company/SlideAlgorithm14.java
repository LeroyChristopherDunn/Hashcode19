package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SlideAlgorithm14 implements SlideAlgorithm {

    public static final int WINDOW_SIZE = 500;

    public final PictureInput input;

    public SlideAlgorithm14(PictureInput input) {
        this.input = input;
    }

    public SlideOutput calculate(){

        input.pictures.sort(Comparator.comparingInt(o -> o.tags.size()));

        List<Slide2> horizontalSlidesSortedByNumTags = input.pictures.stream()
                .filter(picture -> picture.isHorizontal)
                .map(Slide2::new)
                .collect(Collectors.toList());

        List<Slide2> verticalSlidesSortedByNumTags = processVerticalPhotos();

        ArrayList<Slide2> allSlides = new ArrayList<>();
        allSlides.addAll(horizontalSlidesSortedByNumTags);
        allSlides.addAll(verticalSlidesSortedByNumTags);

        List<Slide> slideshow = calculateSlideShow(allSlides);

        return new SlideOutput(slideshow);
    }

    private List<Slide> calculateSlideShow(ArrayList<Slide2> allSlides) {

        List<Slide2> slideShow = new ArrayList<>();
        slideShow.add(allSlides.remove(0));

        while (!allSlides.isEmpty()){

            int stopIndex = Math.min(WINDOW_SIZE, allSlides.size());

            Slide2 lastAddedSlide = slideShow.get(slideShow.size() - 1);

            int bestSlideIndex = 0;
            int bestTransitionScore = 0;
            for (int j = 0; j < stopIndex; j++) {

                Slide2 currSlide = allSlides.get(j);
                int currScore = Slide2.transitionValue(Arrays.asList(lastAddedSlide, currSlide));
                if(currScore >= bestTransitionScore){
                    bestSlideIndex = j;
                    bestTransitionScore = currScore;
                }
            }

            slideShow.add(allSlides.remove(bestSlideIndex));
        }

        return slideShow.stream()
                .map(Slide2::toSlide)
                .collect(Collectors.toList());
    }

    private List<Slide2> processVerticalPhotos() {

        List<Slide2> verticalSlides = new ArrayList<>();

        List<Picture> verticalPictures = input.pictures.stream()
                .filter(picture -> !picture.isHorizontal)
                .collect(Collectors.toList());

        for (int i = 0; i < verticalPictures.size(); i+=2) {
            verticalSlides.add(new Slide2(verticalPictures.get(i), verticalPictures.get(i + 1)));
        }

        return verticalSlides;
    }

}
