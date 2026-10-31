package it.girascamia.commands;

import it.girascamia.GlobalConfigVariables;
import net.minestom.server.command.builder.Command;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnableDitheringCommand extends Command {
    public EnableDitheringCommand(@NotNull String name, @Nullable String... aliases) {
        super(name, aliases);
        setDefaultExecutor((sender, context) -> {
            GlobalConfigVariables.dithering = !(GlobalConfigVariables.dithering);
            sender.sendMessage("Set dithering to:" + GlobalConfigVariables.dithering);
        });
    }
}