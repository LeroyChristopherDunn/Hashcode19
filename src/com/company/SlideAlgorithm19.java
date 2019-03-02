package com.company;

import java.util.*;
import java.util.stream.Collectors;

public class SlideAlgorithm19 implements SlideAlgorithm {

    public static final int WINDOW_SIZE = 5000;

    public final PictureInput input;

    public SlideAlgorithm19(PictureInput input) {
        this.input = input;
    }

    public SlideOutput calculate(){

        ArrayList<Slide2> allSlides = createAllSlides();

        HashMap<String, Set<Slide2>> tagMap = createTagMap(allSlides);

        List<Slide> slideShow = calculateSlideShow(new TreeSet<>(allSlides), tagMap);

        return new SlideOutput(slideShow);
    }

    private HashMap<String, Set<Slide2>> createTagMap(ArrayList<Slide2> slides) {

        HashMap<String, Set<Slide2>> tagMap = new HashMap<>();

        for (Slide2 slide : slides) {
            for (String tag : slide.tags) {

                if (tagMap.get(tag) == null)
                    tagMap.put(tag, new HashSet<>());

                tagMap.get(tag).add(slide);
            }
        }

        return tagMap;
    }

    private ArrayList<Slide2> createAllSlides() {

        List<Slide2> horizontalSlidesSortedByNumTags = input.pictures.stream()
                .filter(picture -> picture.isHorizontal)
                .map(Slide2::new)
                .collect(Collectors.toList());

        List<Slide2> verticalSlidesSortedByNumTags = processVerticalPhotos();

        ArrayList<Slide2> allSlides = new ArrayList<>();
        allSlides.addAll(horizontalSlidesSortedByNumTags);
        allSlides.addAll(verticalSlidesSortedByNumTags);
        Collections.shuffle(allSlides, new Random(0));

        return allSlides;
    }

    private List<Slide2> processVerticalPhotos() {

        List<Slide2> verticalSlides = new ArrayList<>();

        List<Picture> verticalPictures = input.pictures.stream()
                .filter(picture -> !picture.isHorizontal)
                .sorted(Comparator.comparingInt(o -> o.tags.size()))
                .collect(Collectors.toList());

        while (verticalPictures.size() > 1){

            Picture firstPic = verticalPictures.remove(0);
            Picture lastPic = verticalPictures.remove(verticalPictures.size()-1);
            verticalSlides.add(new Slide2(firstPic, lastPic));
        }

        return verticalSlides;
    }

    private List<Slide> calculateSlideShow(TreeSet<Slide2> allSlides, HashMap<String, Set<Slide2>> tagMap) {

        List<Slide2> slideShow = new ArrayList<>();
        Slide2 firstSlide = allSlides.pollFirst();
        for (String tag : firstSlide.tags)
            tagMap.get(tag).remove(firstSlide);
        slideShow.add(firstSlide);

        while (!allSlides.isEmpty()){

            Slide2 lastAddedSlide = slideShow.get(slideShow.size() - 1);

            int tagLoopCount = 0;
            Slide2 bestSlide = allSlides.first();
            int bestTransitionScore = 0;

            //tag search
            tagLoop:
            for (String tag : lastAddedSlide.tags) {
                for (Slide2 currSlide : tagMap.get(tag)) {

                    if(tagLoopCount > WINDOW_SIZE) break tagLoop;
                    tagLoopCount++;

                    int currScore = Slide2.transitionValue(Arrays.asList(lastAddedSlide, currSlide));
                    if(currScore >= bestTransitionScore){
                        bestTransitionScore = currScore;
                        bestSlide = currSlide;
                    }
                }
            }

            slideShow.add(bestSlide);
            allSlides.remove(bestSlide);
            for (String tag : bestSlide.tags)
                tagMap.get(tag).remove(bestSlide);

            //progress
            if(slideShow.size() % 50 == 0) {
                double percentageComplete = 1.0 * slideShow.size() / (slideShow.size() + allSlides.size());
                System.out.print("\rinfo: Progress " + percentageComplete);
            }
        }

        System.out.println();

        return slideShow.stream()
                .map(Slide2::toSlide)
                .collect(Collectors.toList());
    }

}