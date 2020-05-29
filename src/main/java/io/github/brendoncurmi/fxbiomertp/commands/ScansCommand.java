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

package io.github.brendoncurmi.fxbiomertp.commands;

import io.github.brendoncurmi.fxbiomertp.FxBiomeRTP;
import io.github.brendoncurmi.fxbiomertp.impl.data.WorldData;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.World;
import org.spongepowered.plugin.meta.util.NonnullByDefault;

import javax.annotation.Nullable;
import java.util.HashMap;

@NonnullByDefault
public class ScansCommand {

    @Nullable
    private static World getWorld(String name) {
        for (World world : Sponge.getServer().getWorlds())
            if (world.getName().equalsIgnoreCase(name))
                return world;
        return null;
    }

    public static class ListWorlds implements CommandExecutor {
        @Override
        public CommandResult execute(CommandSource src, CommandContext args) {
            HashMap<String, WorldData> scanned = FxBiomeRTP.getInstance().getPersistenceData().getScannedWorlds();
            if (!scanned.isEmpty()) {
                src.sendMessage(Text.of(TextColors.GREEN, "List of scanned worlds:"));
                for (String name : scanned.keySet())
                    src.sendMessage(Text.of(TextColors.AQUA, "  " + name));
            } else src.sendMessage(Text.of(TextColors.GREEN, "There are no scanned worlds"));
            return CommandResult.success();
        }
    }

    public static class ScanWorld implements CommandExecutor {
        @Override
        public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
            FxBiomeRTP inst = FxBiomeRTP.getInstance();
            String worldName = args.requireOne("world");
            World world = getWorld(worldName);
            if (world == null)
                throw new CommandException(Text.of(TextColors.RED, "Cannot find world '" + worldName + "'"));

            src.sendMessage(Text.of(TextColors.GREEN, "Started scanning world '" + worldName + "'..."));

            if (inst.getTask() != null) inst.getTask().cancel();

            inst.getSpiralScan().setWorld(world);

            if (inst.getPersistenceData().hasScannedWorld(worldName))
                inst.getPersistenceData().removeWorld(worldName);

            inst.setTask(Task.builder().execute(() -> inst.getSpiralScan().startScan())
                    .async().name("FxBiomeRTP Scanner").submit(inst));
            return CommandResult.success();
        }
    }

    public static class RemoveWorld implements CommandExecutor {
        @Override
        public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
            String worldName = args.requireOne("world");
            World world = getWorld(worldName);
            if (world == null)
                throw new CommandException(Text.of(TextColors.RED, "Cannot find world '" + worldName + "'"));

            if (!FxBiomeRTP.getInstance().getPersistenceData().hasScannedWorld(worldName))
                throw new CommandException(Text.of(TextColors.RED, "Cannot remove scans for unscanned world '" + worldName + "'"));

            FxBiomeRTP.getInstance().getPersistenceData().removeWorld(worldName);
            src.sendMessage(Text.of(TextColors.GREEN, "Scans for world '" + worldName + "' have been removed"));
            return CommandResult.success();
        }
    }
}
