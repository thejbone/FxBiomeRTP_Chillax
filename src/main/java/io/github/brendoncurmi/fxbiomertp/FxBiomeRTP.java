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
import io.github.brendoncurmi.fxbiomertp.api.IFileFactory;
import io.github.brendoncurmi.fxbiomertp.api.config.ConfigManager;
import io.github.brendoncurmi.fxbiomertp.commands.ScansCommand;
import io.github.brendoncurmi.fxbiomertp.impl.FileFactory;
import io.github.brendoncurmi.fxbiomertp.impl.SpiralScan;
import io.github.brendoncurmi.fxbiomertp.commands.elements.BiomeCommandElement;
import io.github.brendoncurmi.fxbiomertp.commands.BiomeRTPCommand;
import io.github.brendoncurmi.fxbiomertp.commands.RTPCommand;
import io.github.brendoncurmi.fxbiomertp.commands.elements.WorldCommandElement;
import io.github.brendoncurmi.fxbiomertp.impl.config.Config;
import io.github.brendoncurmi.fxbiomertp.impl.data.PersistenceData;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.asset.Asset;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.World;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Optional;

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
    private File file;
    private PersistenceData persistenceData;
    private SpiralScan spiralScan;
    private Task task;
    private IFileFactory fileFactory;
    private Config config;

    @Inject
    public FxBiomeRTP(@ConfigDir(sharedRoot = false) Path configDir, Logger logger) {
        FxBiomeRTP.instance = this;
        this.configDir = configDir;
        this.logger = logger;
    }

    @Listener
    public void preInit(GamePreInitializationEvent event) throws IllegalAccessException {
        file = Paths.get(this.configDir.toString(), "scans.ser").toFile();

        fileFactory = new FileFactory();
        persistenceData = file.exists() ? (PersistenceData) fileFactory.deserialize(file.getAbsolutePath()) : new PersistenceData();

        try {
            Files.createDirectories(this.configDir);
            file.createNewFile();
        } catch (IOException ex) {
            logger.error("Error loading '" + configDir.toString() + "' directory", ex);
        }


        // Configs
        try {
            Files.createDirectories(this.configDir);
        } catch (IOException ex) {
            logger.error("Error loading '" + configDir.toString() + "' directory", ex);
        }

        try {
            Path path = Paths.get(this.configDir.toString(), ID + ".conf");

            if (!Files.exists(path)) {
                Optional<Asset> asset = Sponge.getAssetManager().getAsset(this, "default.conf");
                if (asset.isPresent()) asset.get().copyToFile(path);
            }

            // Load main config
            ConfigManager configManager = new ConfigManager(path);
            config = configManager.getNode().getValue(Config.type);
        } catch (IOException | ObjectMappingException ex) {
            logger.error("Config file could not be loaded", ex);
        }

        BiomeUtils.initBiomes();

        Sponge.getCommandManager().register(instance, CommandSpec.builder()
                .permission(CMD_PERM + "biomertp")
                .description(Text.of("Teleports the player to a random biome"))
                .arguments(
                        new BiomeCommandElement(Text.of("biome")),
                        GenericArguments.optional(GenericArguments.requiringPermission(GenericArguments.player(Text.of("target")), ADMIN_PERM + "biomertp"))
                )
                .executor(new BiomeRTPCommand())
                .build(), "biomertp");

        Sponge.getCommandManager().register(instance, CommandSpec.builder()
                .permission(CMD_PERM + "rtp")
                .description(Text.of("Teleports the player to a random location"))
                .arguments(
                        GenericArguments.optional(GenericArguments.requiringPermission(GenericArguments.player(Text.of("target")), ADMIN_PERM + "rtp"))
                )
                .executor(new RTPCommand())
                .build(), "rtp");

        Sponge.getCommandManager().register(instance, CommandSpec.builder()
                .permission(CMD_PERM + "scans")
                .description(Text.of("Handles the world scans"))
                .child(CommandSpec.builder()
                        .permission(CMD_PERM + "scans.list")
                        .description(Text.of("Lists world scans"))
                        .executor(new ScansCommand.ListWorlds())
                        .build(), "list"
                )
                .child(CommandSpec.builder()
                        .permission(CMD_PERM + "scans.scan")
                        .description(Text.of("Starts scanning the world"))
                        .arguments(
                                new WorldCommandElement(Text.of("world"))
                        )
                        .executor(new ScansCommand.ScanWorld())
                        .build(), "scan"
                )
                .child(CommandSpec.builder()
                        .permission(CMD_PERM + "scans.remove")
                        .description(Text.of("Removes world scans"))
                        .arguments(
                                new WorldCommandElement(Text.of("world"))
                        )
                        .executor(new ScansCommand.RemoveWorld())
                        .build(), "remove"
                )
                .build(), "scans");
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        Collection<World> worlds = Sponge.getServer().getWorlds();
        if (worlds.size() > 0) spiralScan = new SpiralScan();
        else logger.error("Cannot run scan as cannot find any worlds");
    }

    @Listener
    public void onServerStop(GameStoppingEvent event) {
        if (task != null) task.cancel();
        fileFactory.serialize(persistenceData, file.getAbsolutePath());
    }

    public static FxBiomeRTP getInstance() {
        return FxBiomeRTP.instance;
    }

    public Logger getLogger() {
        return logger;
    }

    public PersistenceData getPersistenceData() {
        return persistenceData;
    }

    public SpiralScan getSpiralScan() {
        return spiralScan;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Config getConfig() {
        return config;
    }
}
