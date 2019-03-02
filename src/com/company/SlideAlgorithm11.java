package com.company;

import com.sun.istack.internal.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class SlideAlgorithm11 implements SlideAlgorithm {

    public static final int SUB_LIST_SIZE = 6;

    public final PictureInput input;

    public SlideAlgorithm11(PictureInput input) {
        this.input = input;
    }

    public SlideOutput calculate(){

        input.pictures.sort(Comparator.comparingInt(o -> o.tags.size()));

        List<List<Picture>> subPictureLists = getSubPictureLists();

        List<Slide> completeSlides = new ArrayList<>();

        ExecutorService executorService = Executors.newWorkStealingPool();
        ArrayList<Future<List<Slide>>> futures = new ArrayList<>();
        for (List<Picture> subPictureList : subPictureLists) {
            Future<List<Slide>> future = executorService.submit(() -> getCalculateSlides(subPictureList));
            futures.add(future);
        }

        int count = 0;
        for (Future<List<Slide>> future : futures) {
            try {
                completeSlides.addAll(future.get());
            } catch (Exception e) {
                e.printStackTrace();
            }

            //todo sort slide boundaries

            System.out.println(String.format("sublist percentage %.3f", 1.0*count/subPictureLists.size()));
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
