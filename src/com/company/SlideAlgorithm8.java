package com.company;

import java.util.*;
import java.util.stream.Collectors;

public class SlideAlgorithm8 implements SlideAlgorithm {

    //order by tags aphabetical, inner min max sorting by num tags

    public final PictureInput input;

    public SlideAlgorithm8(PictureInput input) {
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

            //sort horizontal min max num slides
            List<Integer> innerHorizontalSortedNumTags = tagMap.get(tag)
                    .stream()
                    .map(picId -> input.pictures.get(picId))
                    .filter(picture -> picture.isHorizontal)
                    .sorted((o1, o2) -> Integer.compare(o1.tags.size(), o2.tags.size()))
                    .map(picture -> picture.id)
                    .collect(Collectors.toList());

            List<Integer> innerHorizontalSortedNumTagsMinMax = new ArrayList<>();
            while (innerHorizontalSortedNumTags.size() > 0){
                innerHorizontalSortedNumTagsMinMax.add(innerHorizontalSortedNumTags.remove(0));
                if(innerHorizontalSortedNumTags.isEmpty()) break;
                innerHorizontalSortedNumTagsMinMax.add(innerHorizontalSortedNumTags.remove(innerHorizontalSortedNumTags.size()-1));
            }


            List<Integer> innerVerticalSortedNumTags = tagMap.get(tag)
                    .stream()
                    .map(picId -> input.pictures.get(picId))
                    .filter(picture -> !picture.isHorizontal)
                    .sorted((o1, o2) -> Integer.compare(o1.tags.size(), o2.tags.size()))
                    .map(picture -> picture.id)
                    .collect(Collectors.toList());

            List<Integer> innerVerticalSortedNumTagsMinMax = new ArrayList<>();
            while (innerVerticalSortedNumTags.size() > 0){
                innerVerticalSortedNumTagsMinMax.add(innerVerticalSortedNumTags.remove(0));
                if(innerVerticalSortedNumTags.isEmpty()) break;
                innerVerticalSortedNumTagsMinMax.add(innerVerticalSortedNumTags.remove(innerVerticalSortedNumTags.size()-1));
            }


            for (Integer pictureID : innerHorizontalSortedNumTagsMinMax) {
                if(!horizontalPicIdsCheck.contains(pictureID)) {
                    horizontalPicIds.add(pictureID);
                    horizontalPicIdsCheck.add(pictureID);
                }
            }

            for (Integer pictureID : innerVerticalSortedNumTagsMinMax) {
                if(!verticalPicIdsCheck.contains(pictureID)) {
                    verticalPicIds.add(pictureID);
                    verticalPicIdsCheck.add(pictureID);
                }
            }
        }

        List<Slide> horizontalSlides = processHorizontalSlides(horizontalPicIds);
        List<Slide> verticalSlides = processVerticalPhotos(verticalPicIds);


        List<Slide> out = new ArrayList<>();
        int minSize = Math.min(horizontalSlides.size(), verticalSlides.size());
        for (int i = 0; i < minSize; i++) {
            out.add(horizontalSlides.get(i));
            out.add(verticalSlides.get(i));
        }

        if(horizontalSlides.size() > minSize)
            out.addAll(horizontalSlides.subList(minSize, horizontalSlides.size()));
        else
            out.addAll(verticalSlides.subList(minSize, verticalSlides.size()));

        return new SlideOutput(out);
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
