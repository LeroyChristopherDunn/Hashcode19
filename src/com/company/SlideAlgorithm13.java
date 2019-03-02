package com.company;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SlideAlgorithm13 implements SlideAlgorithm {

    public static final int SUB_LIST_SIZE = 5;

    public final PictureInput input;

    public SlideAlgorithm13(PictureInput input) {
        this.input = input;
    }

    public SlideOutput calculate(){

        input.pictures.sort(Comparator.comparingInt(o -> o.tags.size()));

        List<Slide2> horizontalSlidesSortedByNumTags = input.pictures.stream()
                .filter(picture -> picture.isHorizontal)
                .map(Slide2::new)
                .collect(Collectors.toList());

        List<Slide2> verticalSlidesSortedByNumTags = processVerticalPhotos();

        horizontalSlidesSortedByNumTags.addAll(verticalSlidesSortedByNumTags);

        List<List<Slide2>> subSlides = getSubSlides(horizontalSlidesSortedByNumTags, SUB_LIST_SIZE);

        List<Slide> completeSlides = calculateCompleteSlides(subSlides);

        return new SlideOutput(completeSlides);
    }

    private Slide2 toSlide2(Slide slide) {

        if (slide.pictureIDs.size() == 1)
            return new Slide2(input.pictures.get(slide.pictureIDs.get(0)));
        else
            return new Slide2(
                    input.pictures.get(slide.pictureIDs.get(0)),
                    input.pictures.get(slide.pictureIDs.get(1))
            );
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


    private List<Slide> calculateCompleteSlides(List<List<Slide2>> sortedSubslides) {

        ArrayList<SlideBoundary> slideBoundaries = new ArrayList<>();

        int count = 0;
        for (List<Slide2> sec : sortedSubslides) {
            slideBoundaries.add(new SlideBoundary(count, sec.get(0), sec.get(sec.size()-1)));
            count++;
        }

        List<SlideBoundary> optimalOrder = new SlideBoundaryAlgorithm1(slideBoundaries).calculateOptimalOrder();
        ArrayList<Slide2> completedSlides = new ArrayList<>();
        for (SlideBoundary slideBoundary : optimalOrder) {
            completedSlides.addAll(sortedSubslides.get(slideBoundary.index));
        }

        return completedSlides.stream()
                .map(Slide2::toSlide)
                .collect(Collectors.toList());
    }

    private List<List<Slide2>> getSubSlides(List<Slide2> slides, int secSize) {

        List<List<Slide2>> subLists = new ArrayList<>();
        int index = 0;

        do {
            subLists.add(slides.subList(index, Math.min(index + secSize, slides.size())));
            index += secSize;
        }while (index < slides.size());

        return subLists;
    }

}
