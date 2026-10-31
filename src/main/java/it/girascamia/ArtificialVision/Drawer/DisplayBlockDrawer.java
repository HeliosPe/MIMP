package it.girascamia.ArtificialVision.Drawer;

import it.girascamia.ArtificialVision.Image;
import it.girascamia.world.OneBlockWorld;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.display.BlockDisplayMeta;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import ru.dragonestia.msb3.api.entity.debug.DebugRendererEntity;

import java.util.*;

public class DisplayBlockDrawer implements AbstractDrawer{
    int maxBaseSize = 25;
    static Instance instance = OneBlockWorld.getWorld().getInstance();
    static List<Entity> polygons = new ArrayList<>();
    static List<Entity> integralPolygons = new ArrayList<>();

    public void drawImage (Image image, Palette palette, boolean dithering) {
        var matrix = deepCopy(image.getMatrix());
        float pixelSize = (float) maxBaseSize / matrix.length;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length ; j++) {
                double realValue = matrix[i][j];
                double estimatedValue = getNearestValue(realValue, palette);
                double error = realValue - estimatedValue;
                Block material = getBlock(estimatedValue, palette);

                if (dithering && i < matrix.length - 1 && j < matrix[i].length - 1 && j > 0){
                    matrix[i][j + 1] += error * ((double) 3 / 8);
                    matrix[i + 1][j - 1] += error * ((double) 1 / 8);
                    matrix[i + 1][j] += error * ((double) 3 / 8);
                    matrix[i + 1][j + 1] += error * ((double) 1 / 8);
                }
                drawPixel(material, pixelSize, new Pos(j * pixelSize - (double) maxBaseSize /2, (matrix.length - i -1) * pixelSize, -maxBaseSize));
            }
        }
    }

    public double getNearestValue(double pixel, Palette palette) {
        return palette.getBlockValues().stream()
                .min(Comparator.comparingDouble(a -> Math.abs(a - pixel)))
                .orElse(0.);
    }


    public Block getBlock(double pixel, Palette palette) {
        return palette.getPaletteMap().get(pixel);
    }

    /*public static void drawImageBlocks (Image image, boolean dithering) {
        var matrix = image.getMatrix();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length ; j++) {
                double realValue = matrix[i][j];
                Block material = getMaterial(realValue);
                int estimatedValue = REVERSE_GRADIENT_MAP.get(material);
                double error = realValue - estimatedValue;

                if (dithering && i < matrix.length - 1 && j < matrix[i].length - 1 && j > 0){
                    System.out.println("ERRORE: " + error);
                    matrix[i][j + 1] += error * ((double) 3 / 8);
                    matrix[i + 1][j - 1] += error * ((double) 1 / 8);
                    matrix[i + 1][j] += error * ((double) 3 / 8);
                    matrix[i + 1][j + 1] += error * ((double) 1 / 8);
                }
                drawPixelBlock(material, new Pos(j, matrix.length - i -1, -maxBaseSize));
            }

        }
    }*/

    public void drawPixel (Block block, float size, Pos pos) {
        var entity = new DebugRendererEntity(EntityType.BLOCK_DISPLAY, "test");
        var meta = (BlockDisplayMeta) entity.getEntityMeta();
        meta.setBlockState(block);
        meta.setScale(new Vec(size));
        entity.setInstance(instance,pos);
        polygons.add(entity);
    }

    /*private static void drawPixelBlock (Block block, Pos pos) {
        instance.setBlock((int) pos.x(), (int) pos.y(), (int) pos.z(), block);
    }*/

    public void deleteGraphic() {
        for (var entity: polygons){
            entity.remove();
        }
        polygons.clear();
        for (var entity: integralPolygons){
            entity.remove();
        }
        integralPolygons.clear();
    }

    public static double[][] deepCopy(double[][] source) {
        if (source == null) return null;

        double[][] copy = new double[source.length][];
        for (int i = 0; i < source.length; i++) {
            if (source[i] != null) {
                copy[i] = source[i].clone(); // Clones each row independently
            }
        }
        return copy;
    }
}
