package com.company;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class SlideOutput {

    private final List<Slide> slides;

    public SlideOutput(List<Slide> slides) {
        this.slides = slides;
    }

    public void writeToFile(String filePath) throws IOException {

        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        writeOut(writer);
        System.out.println("info: successfully written to file: " + filePath);
    }

    public void writeStdOut() throws IOException {

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
        writeOut(writer);
    }

    private void writeOut(BufferedWriter writer) throws IOException {
        String outputString = toString();
        writer.write(outputString);
        writer.flush();
        writer.close();
    }

    @Override
    public String toString() {

        StringBuilder out = new StringBuilder();

        out.append(slides.size()).append("\n");

        for (Slide slide : slides) {

            String slideString = "";
            for (Integer pictureID : slide.pictureIDs) {
                slideString += "" + pictureID + " ";
            }

            slideString = slideString.trim();

            out.append(slideString).append("\n");
        }

        return out.toString();
    }
}
