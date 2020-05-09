/*
 * MIT License
 *
 * Copyright (c) 2020 Brendon Curmi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.brendoncurmi.fxbiomertp;

import com.google.inject.Inject;
import io.github.brendoncurmi.fxbiomertp.api.BiomeUtils;
import io.github.brendoncurmi.fxbiomertp.api.FileFactory;
import io.github.brendoncurmi.fxbiomertp.api.SpiralScan;
import io.github.brendoncurmi.fxbiomertp.commands.BiomeRTPCommand;
import io.github.brendoncurmi.fxbiomertp.commands.RTPCommand;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

@Plugin(id = FxBiomeRTP.ID,
        name = FxBiomeRTP.NAME,
        version = FxBiomeRTP.VERSION,
        authors = {"FusionDev"},
        description = FxBiomeRTP.DESCRIPTION,
        dependencies = {
                @Dependency(id = "spongeapi", version = "7.1.0")
        })
public class FxBiomeRTP extends PluginInfo {

    private static FxBiomeRTP instance;

    private Path configDir;
    private Logger logger;
    private PluginContainer pluginContainer;
    private File file;
    private BiomeUtils biomeUtils;
    private SpiralScan spiralScan;
    private Task task;

    @Inject
    public FxBiomeRTP(@ConfigDir(sharedRoot = false) Path configDir, Logger logger, PluginContainer pluginContainer) {
        FxBiomeRTP.instance = this;
        this.configDir = configDir;
        this.logger = logger;
        this.pluginContainer = pluginContainer;
    }

    @Listener
    public void preInit(GamePreInitializationEvent event) throws IllegalAccessException {
        file = Paths.get(this.configDir.toString(), ID + ".ser").toFile();

        biomeUtils = file.exists() ? (BiomeUtils) FileFactory.deserialize(file.getAbsolutePath()) : new BiomeUtils();

        try {
            Files.createDirectories(this.configDir);
            file.createNewFile();
        } catch (IOException ex) {
            logger.error("Error loading '" + configDir.toString() + "' directory", ex);
        }

        Sponge.getCommandManager().register(instance, CommandSpec.builder()
                .description(Text.of("Teleports the player to a random biome"))
                .permission(CMD_PERM + "biomertp")
                .arguments(
                        GenericArguments.string(Text.of("biome")),
                        GenericArguments.optional(GenericArguments.player(Text.of("target")))
                )
                .executor(new BiomeRTPCommand())
                .build(), "biomertp");

        Sponge.getCommandManager().register(instance, CommandSpec.builder()
                .description(Text.of("Teleports the player to a random location"))
                .permission(CMD_PERM + "rtp")
                .arguments(
                        GenericArguments.optional(GenericArguments.player(Text.of("target")))
                )
                .executor(new RTPCommand())
                .build(), "rtp");

        BiomeUtils.initBiomes();
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        Collection<World> worlds = Sponge.getServer().getWorlds();
        if (worlds.size() > 0) {
            spiralScan = new SpiralScan(worlds.iterator().next(), location -> {
                biomeUtils.getBiomeData(BiomeUtils.getBiomeName(location.getBiome())).addCoord(location.getBlockX(), location.getBlockZ());
                logger.info("Scanned " + location.getX() + "," + location.getZ());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                //location.setBlockType(BlockTypes.BONE_BLOCK);
                //System.out.println(location.getChunkPosition());
            });

            if (biomeUtils.empty()) {
                task = Task.builder().execute(() -> {
                    getSpiralScan().startScan();
                }).async().name("FxBiomeRTP Scanner").submit(pluginContainer);
            }
        } else logger.error("Cannot run scan as cannot find any worlds");
    }

    @Listener
    public void onServerStop(GameStoppingEvent event) {
        if (task != null) task.cancel();
        FileFactory.serialize(biomeUtils, file.getAbsolutePath());
    }

    public static FxBiomeRTP getInstance() {
        return FxBiomeRTP.instance;
    }

    public BiomeUtils getBiomeUtils() {
        return biomeUtils;
    }

    public SpiralScan getSpiralScan() {
        return spiralScan;
    }
}
