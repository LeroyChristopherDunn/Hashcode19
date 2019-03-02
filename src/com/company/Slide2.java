package com.company;

import java.util.*;

public class Slide2 implements Comparable<Slide2>{

    public final List<Picture> pictures;
    public final Set<String> tags;

    private Slide2(List<Picture> pictures) {
        this.pictures = pictures;
        if(pictures.size() > 2)
            throw new IllegalArgumentException("more than two pictures on slide");

        tags = new HashSet<>();
        for (Picture picture : pictures) {
            tags.addAll(picture.tags);
        }
    }

    public Slide2(Picture picture){
        this(Arrays.asList(picture));
    }

    public Slide2(Picture picture, Picture picture1){
        this(Arrays.asList(picture, picture1));
    }

    public static int numUniqueTags(Picture pic1, Picture pic2) {

        int numUniqueTags = 0;

        for (String tag : pic1.tags) {
            if(!pic2.tags.contains(tag))
                numUniqueTags++;
        }

        for (String tag : pic2.tags) {
            if(!pic1.tags.contains(tag))
                numUniqueTags++;
        }

        return numUniqueTags;
    }

    public int numCommonTags(Slide2 slide2) {

        int numCommonTags = 0;
        for (String tag : tags)
            if(slide2.tags.contains(tag)) numCommonTags++;

        return numCommonTags;
    }

    public int numUniqueTags(Slide2 slide2) {

        int numUniqueTags = 0;
        for (String tag : tags)
            if(!slide2.tags.contains(tag)) numUniqueTags++;

        return numUniqueTags;
    }

    public static int transitionValue(List<Slide2> slides){

        int score = 0;

        for (int i = 0; i < slides.size()-1; i++) {

            int commonTags = slides.get(i).numCommonTags(slides.get(i+1));
            int uniqueTags1 = slides.get(i).numUniqueTags(slides.get(i+1));
            int uniqueTags2 = slides.get(i+1).numUniqueTags(slides.get(i));

            score += Math.min(commonTags, Math.min(uniqueTags1, uniqueTags2));
        }

        return score;
    }

    public Slide toSlide() {

        if (pictures.size() == 1)
            return new Slide(pictures.get(0).id);
        else
            return new Slide(pictures.get(0).id, pictures.get(1).id);
    }

    @Override
    public int compareTo(Slide2 o) {

        return Integer.compare(this.hashCode(), o.hashCode());
    }
}
