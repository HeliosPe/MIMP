package it.girascamia.ArtificialVision.Drawer;

import net.minestom.server.instance.block.Block;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public enum Palette {
    GRAYSCALE_SMOOTH(Map.ofEntries(
                    Map.entry(255., Block.SNOW_BLOCK),
                    Map.entry(204., Block.WHITE_CONCRETE_POWDER),
                    Map.entry(187., Block.CALCITE),
                    Map.entry(170., Block.WHITE_CONCRETE),
                    Map.entry(119., Block.LIGHT_GRAY_CONCRETE_POWDER),
                    Map.entry(85.,  Block.STONE),
                    Map.entry(68.,  Block.GRAY_CONCRETE_POWDER),
                    Map.entry(51.,  Block.GRAY_CONCRETE),
                    Map.entry(0.,   Block.BLACK_CONCRETE)
            )
    ),
    BINARY(Map.ofEntries(
            Map.entry(255., Block.SNOW_BLOCK),
            Map.entry(0., Block.BLACK_CONCRETE)
            )
    ),

    AI_PALETTE(Map.ofEntries(
            Map.entry(255., Block.SNOW_BLOCK),
            Map.entry(248., Block.WHITE_WOOL),
            Map.entry(240., Block.WHITE_CONCRETE_POWDER),
            Map.entry(232., Block.QUARTZ_PILLAR),
            Map.entry(224., Block.QUARTZ_BLOCK),
            Map.entry(216., Block.QUARTZ_BRICKS),
            Map.entry(208., Block.SMOOTH_QUARTZ),
            Map.entry(200., Block.BIRCH_LOG),
            Map.entry(192., Block.BONE_BLOCK),
            Map.entry(184., Block.WHITE_CONCRETE),
            Map.entry(176., Block.CALCITE),
            Map.entry(168., Block.DIORITE),
            Map.entry(160., Block.POLISHED_DIORITE),
            Map.entry(152., Block.LIGHT_GRAY_WOOL),
            Map.entry(144., Block.LIGHT_GRAY_CONCRETE),
            Map.entry(136., Block.LIGHT_GRAY_CONCRETE_POWDER),
            Map.entry(128., Block.TUFF),
            Map.entry(120., Block.GRAVEL),
            Map.entry(112., Block.ANDESITE),
            Map.entry(104., Block.POLISHED_ANDESITE),
            Map.entry(96., Block.STONE),
            Map.entry(88., Block.STONE_BRICKS),
            Map.entry(80., Block.CHISELED_STONE_BRICKS),
            Map.entry(72., Block.COBBLESTONE),
            Map.entry(64., Block.GRAY_CONCRETE_POWDER),
            Map.entry(56., Block.GRAY_WOOL),
            Map.entry(48., Block.GRAY_CONCRETE),
            Map.entry(40., Block.BASALT),
            Map.entry(35., Block.POLISHED_BASALT),
            Map.entry(30., Block.SMOOTH_BASALT),
            Map.entry(25., Block.DEEPSLATE),
            Map.entry(20., Block.DEEPSLATE_BRICKS),
            Map.entry(15., Block.DEEPSLATE_TILES),
            Map.entry(10., Block.BLACKSTONE),
            Map.entry(6., Block.POLISHED_BLACKSTONE),
            Map.entry(3., Block.COAL_BLOCK),
            Map.entry(0., Block.BLACK_CONCRETE)
    )
    );

    private final TreeMap<Double, Block> paletteMap;

    Palette(Map<Double, Block> inputMap) {
        this.paletteMap = new TreeMap<>(inputMap);
    }

    public TreeMap<Double, Block> getPaletteMap() {
        return paletteMap;
    }

    public ArrayList<Double> getBlockValues() {
        return new ArrayList<>(this.paletteMap.keySet());
    }
}
