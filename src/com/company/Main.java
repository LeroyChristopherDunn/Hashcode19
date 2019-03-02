package com.company;

import java.io.IOException;

public class Main {

    //todo update input and output files here
    static final String[] INPUT_FILE_PATHS = {
            "./inputs/a.txt",
            "./inputs/b.txt",
            "./inputs/c.txt",
            "./inputs/d.txt",
            "./inputs/e.txt",
    };
    static final String[] OUTPUT_FILE_PATHS = {
            "./outputs/a.txt",
            "./outputs/b.txt",
            "./outputs/c.txt",
            "./outputs/d.txt",
            "./outputs/e.txt",
    };

    //todo update algorithm here
    private static SlideAlgorithm createSlideAlgorithm(PictureInput pictureInput) {
        return new SlideAlgorithm21(pictureInput);
    }

    public static void main(String[] args) throws IOException {

        for (int i = 0; i < INPUT_FILE_PATHS.length; i++) {

            System.out.println("info: processing input file " + i);

            PictureInput pictureInput = PictureInput.readFromFile(INPUT_FILE_PATHS[i]);
//        pictureInput.printStdOut();
            SlideOutput slideOutput = createSlideAlgorithm(pictureInput).calculate();
            slideOutput.writeToFile(OUTPUT_FILE_PATHS[i]);
//            slideOutput.writeStdOut();
        }
    }
}
