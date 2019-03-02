package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SlideAlgorithm15 implements SlideAlgorithm {

    public static final int WINDOW_SIZE = 100;
    public static final int NUM_PASSES = 10;

    public final PictureInput input;

    public SlideAlgorithm15(PictureInput input) {
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

        List<Slide2> slideshow = calculateSlideShow(allSlides);

        for (int i = 0; i < NUM_PASSES; i++) {
           slideshow = calculateSlideShow(new ArrayList<>(slideshow));
        }

        return new SlideOutput(slideshow.stream().map(Slide2::toSlide).collect(Collectors.toList()));
    }

    private List<Slide2> calculateSlideShow(ArrayList<Slide2> allSlides) {

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

        return slideShow;
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
