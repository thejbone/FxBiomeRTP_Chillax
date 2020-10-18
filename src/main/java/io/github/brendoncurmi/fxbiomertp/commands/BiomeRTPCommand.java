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
import io.github.brendoncurmi.fxbiomertp.PluginInfo;
import io.github.brendoncurmi.fxbiomertp.api.ICooldown;
import io.github.brendoncurmi.fxbiomertp.impl.Cooldown;
import io.github.brendoncurmi.fxbiomertp.impl.data.WorldData;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.Sponge;
import org.spongepowered.plugin.meta.util.NonnullByDefault;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.*;

@NonnullByDefault
public class BiomeRTPCommand implements CommandExecutor {

    private static final ICooldown COOLDOWN = new Cooldown(FxBiomeRTP.getInstance().getConfig().getBiomeRtpCooldown());

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Optional<Player> target = args.getOne("target");
        if (!(src instanceof Player) && !target.isPresent()) {
            src.sendMessage(Text.of(TextColors.RED, "Using the command from this source requires specifying a target"));
            return CommandResult.empty();
        }

        Player player = target.orElseGet(() -> (Player) src);

        if (FxBiomeRTP.getInstance().getConfig().getBiomeRtpCooldown() > 0 && !player.hasPermission(PluginInfo.COOLDOWN_PERM + "biomertp")) {
            if (!COOLDOWN.isValid(player))
                throw new CommandException(Text.of(TextColors.RED, "You can only use this command every " + COOLDOWN.getDelay() + "s"));
            COOLDOWN.addPlayer(player);
        }

        if (!FxBiomeRTP.getInstance().getPersistenceData().hasScannedWorld(player.getWorld().getName())) {
            src.sendMessage(Text.of(TextColors.RED, "This world has not been scanned"));
            return CommandResult.empty();
        }

        WorldData worldData = FxBiomeRTP.getInstance().getPersistenceData().getWorldData(player.getWorld().getName());
        String biome = args.requireOne("biome").toString().toLowerCase();
        if (!worldData.hasBiome(biome)) {
            player.sendMessage(Text.of(TextColors.RED, "Cannot find biome '" + biome + "'"));
            return CommandResult.empty();
        }

        int[] coords = worldData.getBiomeData(biome).getRandomCoord();
		Location<World> worldloc = new Location<>(player.getWorld(), coords[0], 90, coords[1]);
		Optional<Location<World>> optionalTargetLoc = Sponge.getTeleportHelper().getSafeLocation(worldloc, 30, 5);
        if (optionalTargetLoc.isPresent()) {
            Location<World> targetloc = optionalTargetLoc.get();
            player.setLocation(targetloc);
            player.sendMessage(Text.of(TextColors.GREEN, "You have been randomly teleported to a(n) " + player.getLocation().getBiome().getName() + " biome!"));
        }
        return CommandResult.success();
    }
}
