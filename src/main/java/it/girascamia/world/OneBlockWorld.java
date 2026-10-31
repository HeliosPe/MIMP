package it.girascamia.world;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.instance.*;
import net.minestom.server.instance.block.Block;
import net.minestom.server.world.DimensionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.dragonestia.msb3.api.world.World;
import ru.dragonestia.msb3.api.world.WorldFactory;

import java.util.concurrent.CompletableFuture;
@Slf4j
public class OneBlockWorld  {

    private static boolean used = false;
    @Getter private static World world;

    private OneBlockWorld() {}

    public static void init(GameMode gameMode) {
        var dimension = MinecraftServer.getDimensionTypeRegistry().register("msb3:full_bright", DimensionType.builder()
                .ambientLight(2f)
                .fixedTime(10L)
                .build());

        var factory = WorldFactory.custom(dimension, new IChunkLoader() {
            @Override
            public @NotNull Chunk loadChunk(@NotNull Instance instance, int chunkX, int chunkZ) {

                var chunk = new DynamicChunk(instance, chunkX, chunkZ);

                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        for (int y = 0; y < 16; y++) {
                            //TODO world generation experiments
                            //chunk.setBlock(x, (int) (10 * Math.sin((double) (chunkX * 16 + x) / 10)), z, Block.STONE);
                            //chunk.setBlock(x, (int)(Math.sqrt(100 - Math.pow((double) (chunkX * 16 + x) / 10, 2) - Math.pow((double) (chunkZ * 16 + z) / 10, 2))), z, Block.STONE);
                            //log.info(((chunkX * 16 + x) / 5) + "\t" + (6 * (int) Math.sin((double) (chunkX * 16 + x) / 5)));
                        }

                    }
                }
                if (chunkX == 0 && chunkZ == 0) {
                   //chunk.setBlock(0, 0, 0, Block.STONE);
                }
                return chunk;
            }

            @Override
            public void saveChunk(@NotNull Chunk chunk) {}
        });

        init(gameMode, factory.createWorldSync(), new Pos(0, 1, 0));
    }

    public static synchronized void init(GameMode gameMode, World world, Pos spawnPos) {
        if (used) return;
        used = true;

        OneBlockWorld.world = world;

        MinecraftServer.getGlobalEventHandler().addListener(AsyncPlayerConfigurationEvent.class, event -> {
            var player = event.getPlayer();
            player.setRespawnPoint(spawnPos);
            player.setGameMode(gameMode);
            event.setSpawningInstance(world.getInstance());
        });
    }
}
