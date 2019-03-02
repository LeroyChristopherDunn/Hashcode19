package com.company;

import java.util.*;
import java.util.stream.Collectors;

public class SlideAlgorithm6 implements SlideAlgorithm {

    //order by tags aphabetical

    public final PictureInput input;

    public SlideAlgorithm6(PictureInput input) {
        this.input = input;
    }

    public SlideOutput calculate(){

        //Create tag-picture dictionary

        HashMap<String, List<Integer>> tagMap = new HashMap<>();

        for (Picture picture : input.pictures) {
            for (String tag : picture.tags) {
                if (tagMap.get(tag) == null) {
                    tagMap.put(tag, new ArrayList<>());
                }
                tagMap.get(tag).add(picture.id);
            }
        }

//        ArrayList<String> tagList = new ArrayList<>(tagMap.keySet());
//        sortedTagList.sort((o1, o2) -> o1.compareTo(o2));


        //sort/group horizontal and vert according to tags

        SortedSet<Integer> horizontalPicIdsCheck = new TreeSet<>(Integer::compareTo);
        List<Integer> horizontalPicIds = new ArrayList<>();
        SortedSet<Integer> verticalPicIdsCheck = new TreeSet<>(Integer::compareTo);
        List<Integer> verticalPicIds = new ArrayList<>();

        for (String tag : tagMap.keySet()) {

            for (Integer pictureID : tagMap.get(tag)) {

                if(input.pictures.get(pictureID).isHorizontal) {

                    if(!horizontalPicIdsCheck.contains(pictureID)) {
                        horizontalPicIds.add(pictureID);
                        horizontalPicIdsCheck.add(pictureID);
                    }

                }else{

                    if(!verticalPicIdsCheck.contains(pictureID)) {
                        verticalPicIds.add(pictureID);
                        verticalPicIdsCheck.add(pictureID);
                    }
                }
            }

        }

        List<Slide> horizontalSlides = processHorizontalSlides(horizontalPicIds);
        List<Slide> verticalSlides = processVerticalPhotos(verticalPicIds);

        horizontalSlides.addAll(verticalSlides);
        return new SlideOutput(horizontalSlides);
    }

    private List<Slide> processHorizontalSlides(List<Integer> horizontalPicIds) {
        return horizontalPicIds.stream()
                    .map(Slide::new)
                    .collect(Collectors.toList());
    }

    private List<Slide> processVerticalPhotos(List<Integer> vertSlidesOrdered) {

        ArrayList<Slide> slides = new ArrayList<>();

        for (int i = 0; i < vertSlidesOrdered.size(); i+=2) {
            slides.add(new Slide(vertSlidesOrdered.get(i), vertSlidesOrdered.get(i + 1)));
        }

        return slides;
    }

}
