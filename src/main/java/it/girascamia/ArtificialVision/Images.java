package it.girascamia.ArtificialVision;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

public class Images {
    private static final byte[] PNG_MAGIC  = new byte[]{(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A};
    private static final byte[] JPEG_MAGIC = new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF};
    private static final byte[] GIF_MAGIC  = new byte[]{0x47, 0x49, 0x46, 0x38}; // "GIF8"
    private static final byte[] WEBP_MAGIC = new byte[]{0x52, 0x49, 0x46, 0x46}; // "RIFF"

    public static boolean isImageByMagicNumber(Path path) {
        // Считываем первые 8 байт файла
        byte[] header = new byte[8];
        try (InputStream is = Files.newInputStream(path)) {
            int bytesRead = is.read(header);
            if (bytesRead < 3) return false; // Файл слишком мал для сигнатуры
        } catch (IOException e) {
            return false; // Нет доступа или ошибка чтения
        }

        return isPng(header) || isJpeg(header) || isGif(header) || isWebp(header);
    }

    private static boolean isPng(byte[] header) {
        return Arrays.equals(Arrays.copyOf(header, 8), PNG_MAGIC);
    }

    private static boolean isJpeg(byte[] header) {
        return header[0] == JPEG_MAGIC[0] &&
                header[1] == JPEG_MAGIC[1] &&
                header[2] == JPEG_MAGIC[2];
    }

    private static boolean isGif(byte[] header) {
        return header[0] == GIF_MAGIC[0] &&
                header[1] == GIF_MAGIC[1] &&
                header[2] == GIF_MAGIC[2] &&
                header[3] == GIF_MAGIC[3];
    }

    private static boolean isWebp(byte[] header) {
        // WEBP начинается с "RIFF" (байты 0..3)
        return header[0] == WEBP_MAGIC[0] &&
                header[1] == WEBP_MAGIC[1] &&
                header[2] == WEBP_MAGIC[2] &&
                header[3] == WEBP_MAGIC[3];
    }
}
