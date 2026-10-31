package it.girascamia;

import it.girascamia.ArtificialVision.Drawer.Palette;
import it.girascamia.ArtificialVision.Image;
import it.girascamia.ArtificialVision.Drawer.AbstractDrawer;
import it.girascamia.ArtificialVision.Drawer.DisplayBlockDrawer;
import it.girascamia.ArtificialVision.Images;
import it.girascamia.commands.EnableDitheringCommand;
import it.girascamia.world.OneBlockWorld;
import lombok.extern.slf4j.Slf4j;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.GameMode;
import net.minestom.server.event.player.PlayerChatEvent;
import ru.dragonestia.msb3.api.boot.ServerBootstrap;
import ru.dragonestia.msb3.api.boot.ServerInitializer;
import ru.dragonestia.msb3.api.module.*;
import ru.dragonestia.msb3.api.player.MsbPlayer;
import team.unnamed.creative.ResourcePack;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
public class Main extends ServerInitializer {
    int i = 0;
    List<Image> images = new ArrayList<>();
    public static void main(String[] args) {
        // Здесь загружаем нашу конфигурацию msb3 и говорим, чтоб он запускался на порту 25565
        ServerBootstrap.start("0.0.0.0", 25565, new Main());
    }

    @Override
    public void onLoad() {
        // Запускается при самом старте приложения, когда никакая конфигурация сервера еще не подгружена
    }

    @Override
    public void onDefaultModulesLoaded() {

        MotdModule.init("logo.png", "<gradient:#ff0059:#e06806><bold>msb3 server</bold></gradient>");
        OneBlockWorld.init(GameMode.SPECTATOR);
        PrometheusMetricsModule.init(new InetSocketAddress("0.0.0.0", 7500));
        PrometheusMetricsModule.init(new InetSocketAddress("0.0.0.0", 7500));

        try (Stream<Path> paths = Files.walk(GlobalConfigVariables.imagesDir)) {
            paths.filter(Files::isRegularFile)
                    .filter(Images::isImageByMagicNumber)
                    .forEach(path -> {
                        try {
                            images.add(new Image(path.toString()));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        MinecraftServer.getGlobalEventHandler().addListener(PlayerChatEvent.class, event -> {
            var player = (MsbPlayer) event.getPlayer();
            player.setView(-178, -17);
            AbstractDrawer drawer = new DisplayBlockDrawer();
            drawer.deleteGraphic();
            if(event.getRawMessage().contains("reload")){
                drawer.drawImage(images.get((i - 1) % images.size()), Palette.GRAYSCALE_SMOOTH, GlobalConfigVariables.dithering);
            }
            else if(event.getRawMessage().contains("binary")){
                drawer.drawImage(images.get((i - 1) % images.size()), Palette.BINARY, GlobalConfigVariables.dithering);
            }
            else if(event.getRawMessage().contains("dithering")){
                GlobalConfigVariables.dithering = !(GlobalConfigVariables.dithering);
                player.sendMessage("Set dithering to:" + GlobalConfigVariables.dithering);
                drawer.drawImage(images.get((i - 1) % images.size()), Palette.GRAYSCALE_SMOOTH, GlobalConfigVariables.dithering);
            }
            else{
            drawer.drawImage(images.get(i % images.size()), Palette.GRAYSCALE_SMOOTH, GlobalConfigVariables.dithering);
            i++;
            player.addDebugRenderLayer("test");
            event.setCancelled(true);
            }
        });

        MinecraftServer.getCommandManager().register(new EnableDitheringCommand("enableDithering", "dithering"));
    }

    @Override
    public void onInitializeResources(ResourcePack resourcePack) {
        // Запускается на стадии подготовки всех ресурсов, которые необходимо скомпилировать и добавить в ресурс-пак.
        // Именно на данном этапе необходимо регистрировать глифы.
    }

    @Override
    public void onResourcePackCompiled(ResourcePack resourcePack) {
        // Запускается после того как все пакеты ресурсов были успешно скомпилированы и готовы к последующей упаковке
        // и отправке клиенту игрока.

        // Ниже строка отвечает как раз за отправку ресурс-пака игроку.
        ResourcePackRepositoryModule.init("0.0.0.0", 7270);
        // То есть на порту 7270 запускается web-сервер, с которого будет скачиваться ресурс-пак при входе на сервер
    }

    @Override
    public void onServerStarted() {
        // Сервер полностью инициализирован и на сервер полностью готов к игре
    }
}