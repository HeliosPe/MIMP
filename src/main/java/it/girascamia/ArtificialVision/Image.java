package it.girascamia.ArtificialVision;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Image {
        private double[][] grayscaleMatrix;
        private int width;
        private int height;

        public Image(String imagePath) throws IOException {
            loadImage(imagePath);
        }

        private void loadImage(String imagePath) throws IOException {
            BufferedImage image = ImageIO.read(new File(imagePath));
            if (image == null) {
                throw new IOException("Cannot read image: " + imagePath);
            }

            this.width = image.getWidth();
            this.height = image.getHeight();
            this.grayscaleMatrix = new double[height][width];

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = image.getRGB(x, y);
                    grayscaleMatrix[y][x] = rgbToGrayscaleLuminance(rgb);
                }
            }

            this.grayscaleMatrix = resizeIfNeeded(this.grayscaleMatrix);
        }

        private int rgbToGrayscaleLuminance(int rgb) {
            int r = (rgb >> 16) & 0xFF;
            int g = (rgb >> 8) & 0xFF;
            int b = rgb & 0xFF;
            return (int)(0.299 * r + 0.587 * g + 0.114 * b);
        }

        public double[][] getMatrix() {
            return grayscaleMatrix;
        }

        public double getValue(int row, int col) {
            return grayscaleMatrix[row][col];
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public double getAverageGray() {
            long sum = 0;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    sum += grayscaleMatrix[y][x];
                }
            }
            return (double) sum / (width * height);
        }

        public double getMinGray() {
            double min = 255;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (grayscaleMatrix[y][x] < min) {
                        min = grayscaleMatrix[y][x];
                    }
                }
            }
            return min;
        }

        public double getMaxGray() {
            double max = 0;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (grayscaleMatrix[y][x] > max) {
                        max = grayscaleMatrix[y][x];
                    }
                }
            }
            return max;
        }

        public double[][] normalizeMatrix() {
            double min = getMinGray();
            double max = getMaxGray();
            double[][] normalized = new double[height][width];

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    normalized[y][x] = (int)((grayscaleMatrix[y][x] - min) * 255.0 / (max - min));
                }
            }

            return normalized;
        }

        public void exportToCSV(String csvPath) throws IOException {
            try (PrintWriter writer = new PrintWriter(new FileWriter(csvPath))) {
                for (int y = 0; y < height; y++) {
                    StringBuilder row = new StringBuilder();
                    for (int x = 0; x < width; x++) {
                        if (x > 0) row.append(",");
                        row.append(grayscaleMatrix[y][x]);
                    }
                    writer.println(row.toString());
                }
            }
        }

        public void printStatistics() {
            System.out.println("=== Grayscale Image Statistics ===");
            System.out.println("Dimensions: " + width + " x " + height);
            System.out.println("Total pixels: " + (width * height));
            System.out.printf("Average grayscale: %.2f\n", getAverageGray());
            System.out.println("Minimum grayscale: " + getMinGray());
            System.out.println("Maximum grayscale: " + getMaxGray());
            System.out.println("Dynamic range: " + (getMaxGray() - getMinGray()));
        }

    public static double[][] resizeIfNeeded(double[][] image) {
        if (image == null || image.length == 0 || image[0].length == 0) {
            return image;
        }

        int height = image.length;
        int width = image[0].length;
        int maxDimension = 265;

        // Если обе стороны не превышают 265, возвращаем исходный массив
        if (width <= maxDimension && height <= maxDimension) {
            return image;
        }

        // Вычисляем коэффициент масштабирования, чтобы уложить изображение в maxDimension
        double scale = Math.min((double) maxDimension / width, (double) maxDimension / height);

        int newWidth = (int) Math.round(width * scale);
        int newHeight = (int) Math.round(height * scale);

        // Гарантируем, что размеры будут хотя бы 1х1
        newWidth = Math.max(1, newWidth);
        newHeight = Math.max(1, newHeight);

        return resizeBilinear(image, newWidth, newHeight);
    }

    /**
     * Билинейная интерполяция для двумерного массива double[][]
     */
    private static double[][] resizeBilinear(double[][] source, int targetWidth, int targetHeight) {
        int srcHeight = source.length;
        int srcWidth = source[0].length;

        double[][] result = new double[targetHeight][targetWidth];

        double xRatio = (double) (srcWidth - 1) / targetWidth;
        double yRatio = (double) (srcHeight - 1) / targetHeight;

        for (int i = 0; i < targetHeight; i++) {
            for (int j = 0; j < targetWidth; j++) {
                double x = j * xRatio;
                double y = i * yRatio;

                int x1 = (int) x;
                int y1 = (int) y;
                int x2 = Math.min(x1 + 1, srcWidth - 1);
                int y2 = Math.min(y1 + 1, srcHeight - 1);

                double xDiff = x - x1;
                double yDiff = y - y1;

                // Билинейное взвешивание 4 соседних пикселей
                double p1 = source[y1][x1];
                double p2 = source[y1][x2];
                double p3 = source[y2][x1];
                double p4 = source[y2][x2];

                double interpolatedValue = p1 * (1 - xDiff) * (1 - yDiff) +
                        p2 * xDiff * (1 - yDiff) +
                        p3 * (1 - xDiff) * yDiff +
                        p4 * xDiff * yDiff;

                result[i][j] = interpolatedValue;
            }
        }

        return result;
    }
}
