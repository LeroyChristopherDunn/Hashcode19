package com.company;

import com.sun.istack.internal.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class SlideAlgorithm9 implements SlideAlgorithm {

    //fixed depth dynamic programming

    public static final int SUB_LIST_SIZE = 6;

    //order by tags aphabetical, inner min max sorting by num tags

    public final PictureInput input;

    public SlideAlgorithm9(PictureInput input) {
        this.input = input;
    }

    public SlideOutput calculate(){

        List<List<Picture>> subPictureLists = getSubPictureLists();

        List<Slide> completeSlides = new ArrayList<>();

        int count = 0;
        for (List<Picture> subPictureList : subPictureLists) {
            List<Slide> slides = getCalculateSlides(subPictureList);
            //todo sort slide boundaries
            completeSlides.addAll(slides);
            System.out.println("sublist percentage " + 1.0*count/subPictureLists.size());
            count++;
        }

        return new SlideOutput(completeSlides);
    }

    private List<List<Picture>> getSubPictureLists() {

        List<List<Picture>> subLists = new ArrayList<>();
        int index = 0;

        do {
            subLists.add(input.pictures.subList(index, Math.min(index + SUB_LIST_SIZE, input.pictures.size())));
            index += SUB_LIST_SIZE;
        }while (index < input.pictures.size());

        return subLists;
    }

    private List<Slide> getCalculateSlides(List<Picture> pictures) {

        return recursivelyArrangeSlides(new ArrayList<>(), pictures, null).stream()
                .map(Slide2::toSlide)
                .collect(Collectors.toList());
    }

    private List<Slide2> recursivelyArrangeSlides(List<Slide2> currentSlides, List<Picture> availPictures, @Nullable Picture tempVertPicture){

//        System.out.println("curr slides size: " + currentSlides.size());
//        System.out.println("avail pics size: " + availPictures.size());

        //if there are no pictures
        if (availPictures.isEmpty())
            return currentSlides;

        //if there is only a single vertical picture
        if(tempVertPicture == null && availPictures.size() == 1 && !availPictures.get(0).isHorizontal)
            return currentSlides;


        List<Slide2> bestSlides = new ArrayList<>();
        int bestSlidesScore = 0;

        for (Picture availPicture : availPictures) {

            if (availPicture.isHorizontal){

                if(tempVertPicture != null) continue;

                ArrayList<Slide2> newSlides = new ArrayList<>(currentSlides);
                newSlides.add(new Slide2(availPicture));

                ArrayList<Picture> newAvailPictures = new ArrayList<>(availPictures);
                newAvailPictures.remove(availPicture);

                List<Slide2> opSlides = recursivelyArrangeSlides(newSlides, newAvailPictures, null);
                int opSlidesValue = Slide2.transitionValue(opSlides);
                if(opSlidesValue > bestSlidesScore){
                    bestSlidesScore = opSlidesValue;
                    bestSlides = opSlides;
                }

            }else {

                if(tempVertPicture != null){

                    ArrayList<Slide2> newSlides = new ArrayList<>(currentSlides);
                    newSlides.add(new Slide2(tempVertPicture, availPicture));

                    ArrayList<Picture> newAvailPictures = new ArrayList<>(availPictures);
                    newAvailPictures.remove(availPicture);

                    List<Slide2> opSlides = recursivelyArrangeSlides(newSlides, newAvailPictures, null);
                    int opSlidesValue = Slide2.transitionValue(opSlides);
                    if(opSlidesValue > bestSlidesScore){
                        bestSlidesScore = opSlidesValue;
                        bestSlides = opSlides;
                    }

                }else{

                    ArrayList<Slide2> newSlides = new ArrayList<>(currentSlides);

                    ArrayList<Picture> newAvailPictures = new ArrayList<>(availPictures);
                    newAvailPictures.remove(availPicture);

                    List<Slide2> opSlides = recursivelyArrangeSlides(newSlides, newAvailPictures, availPicture);
                    int opSlidesValue = Slide2.transitionValue(opSlides);
                    if(opSlidesValue > bestSlidesScore){
                        bestSlidesScore = opSlidesValue;
                        bestSlides = opSlides;
                    }

                }

            }

        }

        return bestSlides;
    }

}
