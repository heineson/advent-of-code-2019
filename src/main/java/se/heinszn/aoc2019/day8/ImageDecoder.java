package se.heinszn.aoc2019.day8;

import lombok.Value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ImageDecoder {
    private final Image image;

    public ImageDecoder(int width, int height, String encodedData) {
        this.image = Image.fromString(width, height, encodedData);
    }

    public Image getImage() {
        return image;
    }

    public String printImage() {
        int[] canvas = new int[image.height * image.width];
        Arrays.fill(canvas, 2);

        List<List<Integer>> layers = this.image.getLayers();
        Collections.reverse(layers);

        layers.forEach(l -> {
            for (int i = 0; i < l.size(); i++) {
                canvas[i] = l.get(i) == 2 ? canvas[i] : l.get(i);
            }
        });

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < canvas.length; i++) {
            sb.append(canvas[i] == 0 ? " " : "X");
            if (i % image.width == image.width - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    @Value
    static class Image {
        private int[] pixelColors;
        private int height;
        private int width;

        public static Image fromString(int width, int height, String encodedData) {
            int[] pixelColors = new int[encodedData.length()];
            char[] chars = encodedData.toCharArray();
            for (int i = 0; i < encodedData.length(); i++) {
                pixelColors[i] = Character.digit(chars[i], 10);
            }
            return new Image(pixelColors, height, width);
        }

        public List<List<Integer>> getLayers() {
            int layerLength = height * width;
            int currentIndex = 0;
            List<List<Integer>> layers = new ArrayList<>();

            while (currentIndex < pixelColors.length) {
                int[] layer = Arrays.copyOfRange(pixelColors, currentIndex, currentIndex + layerLength);
                layers.add(Arrays.stream(layer).boxed().collect(Collectors.toList()));
                currentIndex += layerLength;
            }

            return layers;
        }
    }
}
