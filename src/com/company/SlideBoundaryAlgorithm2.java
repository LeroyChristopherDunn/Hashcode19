package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SlideBoundaryAlgorithm2 {

    public static final int SECTION_SIZE = 6;

    private final List<SlideBoundary> slideBoundaries;

    public SlideBoundaryAlgorithm2(List<SlideBoundary> slideBoundaries) {
        this.slideBoundaries = slideBoundaries;
    }

    public List<SlideBoundary> calculateOptimalOrder(){

        List<List<SlideBoundary>> subBoundaryLists = getSubBoundaryLists();

        ExecutorService executorService = Executors.newWorkStealingPool();
        ArrayList<Future<List<SlideBoundary>>> futures = new ArrayList<>();
        for (List<SlideBoundary> subBoundary : subBoundaryLists) {
            Future<List<SlideBoundary>> future = executorService.submit(() -> getCalculateBoundaries(subBoundary));
            futures.add(future);
        }

        List<List<SlideBoundary>> sortedSubBoundaries = new ArrayList<>();
        int count = 0;
        for (Future<List<SlideBoundary>> future : futures) {
            try {
                sortedSubBoundaries.add(future.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(String.format("subboundary percentage %.3f", 1.0*count/subBoundaryLists.size()));
            count++;
        }

        ArrayList<SlideBoundary> optimalOrder = new ArrayList<>();
        for (List<SlideBoundary> sec : sortedSubBoundaries) {
            optimalOrder.addAll(sec);
        }
        return optimalOrder;
    }

    private List<List<SlideBoundary>> getSubBoundaryLists() {

        List<List<SlideBoundary>> subLists = new ArrayList<>();
        int index = 0;

        do {
            subLists.add(slideBoundaries.subList(index, Math.min(index + SECTION_SIZE, slideBoundaries.size())));
            index += SECTION_SIZE;
        }while (index < slideBoundaries.size());

        return subLists;
    }

    private List<SlideBoundary> getCalculateBoundaries(List<SlideBoundary> subBoundary) {

        return recursivelyArrangeSlides(new ArrayList<>(), subBoundary);
    }

    private List<SlideBoundary> recursivelyArrangeSlides(List<SlideBoundary> currentOrder, List<SlideBoundary> availBoundaries){

        //if there are no pictures
        if (availBoundaries.isEmpty())
            return currentOrder;

        List<SlideBoundary> bestOrdering = new ArrayList<>();
        int bestOrderingScore = 0;

        for (SlideBoundary slideBoundary : availBoundaries) {

            ArrayList<SlideBoundary> newOrder = new ArrayList<>(currentOrder);
            newOrder.add(slideBoundary);

            ArrayList<SlideBoundary> newAvailBoundaries = new ArrayList<>(availBoundaries);
            newAvailBoundaries.remove(slideBoundary);

            List<SlideBoundary> opOrdering = recursivelyArrangeSlides(newOrder, newAvailBoundaries);
            int opOrderingValue = SlideBoundary.transitionValue(opOrdering);
            if(opOrderingValue > bestOrderingScore){
                bestOrderingScore = opOrderingValue;
                bestOrdering = opOrdering;
            }
        }

        return bestOrdering;
    }

}
