package com.company;

import java.util.List;

public class Picture {

    public final int id;
    public final boolean isHorizontal;
    public final List<String> tags;

    public Picture(int id, boolean isHorizontal, List<String> tags) {
        this.id = id;
        this.isHorizontal = isHorizontal;
        this.tags = tags;
    }

    @Override
    public String toString() {

        String out = isHorizontal ? "H" : "V";
        out += " " + tags.size();

        for (String tag : tags) {
            out += " " + tag;
        }
        return out;
    }
}
