package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PictureInput {

    public final List<Picture> pictures;

    public PictureInput(List<Picture> pictures) {
        this.pictures = pictures;
    }

    public static PictureInput readFromFile(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        return readInput(reader);
    }

    private static PictureInput readFromStdIn() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return readInput(reader);
    }

    private static PictureInput readInput(BufferedReader reader) throws IOException {

        int numPictures = Integer.parseInt(reader.readLine());

        List<Picture> pictures = new ArrayList<>();

        for (int i = 0; i < numPictures; i++) {

            String[] row = reader.readLine().split(" ");
            boolean isHorizontal = row[0].equals("H");
            List<String> tags = new ArrayList<>(Arrays.asList(row).subList(2, row.length));
            pictures.add(new Picture(i, isHorizontal, tags));
//            System.out.println("info: read picture " + i);
        }

        return new PictureInput(pictures);
    }

    @Override
    public String toString() {

        StringBuilder out = new StringBuilder("" +  pictures.size() + "\n");

        for (Picture picture : pictures) {
            out.append(picture.toString()).append("\n");
        }

        return out.toString();
    }

    public void printStdOut(){

        System.out.println(toString());
    }
}
