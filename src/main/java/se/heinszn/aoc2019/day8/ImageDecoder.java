package se.heinszn.aoc2019.day8;

import lombok.Value;

import java.util.ArrayList;
import java.util.Arrays;
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
