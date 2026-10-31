package it.girascamia.ArtificialVision.Drawer;

import it.girascamia.ArtificialVision.Image;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.block.Block;

public interface AbstractDrawer {
    void drawImage(Image image, Palette palette, boolean dithering);
    void drawPixel(Block block, float size, Pos pos);
    void deleteGraphic();
}
