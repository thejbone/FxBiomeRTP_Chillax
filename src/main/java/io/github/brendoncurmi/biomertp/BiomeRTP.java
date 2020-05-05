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

package io.github.brendoncurmi.biomertp;

import com.google.inject.Inject;
import io.github.brendoncurmi.biomertp.commands.RTPCommand;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

import java.nio.file.Path;

@Plugin(id = BiomeRTP.ID,
        name = BiomeRTP.NAME,
        version = BiomeRTP.VERSION,
        authors = {"FusionDev"},
        description = BiomeRTP.DESCRIPTION,
        dependencies = {
                @Dependency(id = "spongeapi", version = "7.1.0")
        })
public class BiomeRTP extends PluginInfo {

    private static BiomeRTP instance;

    private Path configDir;
    private Logger logger;

    @Inject
    public BiomeRTP(@ConfigDir(sharedRoot = false) Path configDir, Logger logger, PluginContainer pluginContainer) {
        BiomeRTP.instance = this;
        this.configDir = configDir;
        this.logger = logger;
    }

    @Listener
    public void preInit(GamePreInitializationEvent event) {
        Sponge.getCommandManager().register(instance, CommandSpec.builder()
                .description(Text.of("Teleports the player to a random location"))
                .permission(CMD_PERM + "rtp")
                .arguments(
                        GenericArguments.optional(GenericArguments.player(Text.of("target")))
                )
                .executor(new RTPCommand())
                .build(), "rtp");
    }

    public static BiomeRTP getInstance() {
        return BiomeRTP.instance;
    }
}
