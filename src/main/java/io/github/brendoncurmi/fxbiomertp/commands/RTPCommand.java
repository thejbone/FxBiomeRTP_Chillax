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
import io.github.brendoncurmi.fxbiomertp.api.MathUtils;
import io.github.brendoncurmi.fxbiomertp.impl.Cooldown;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextFormat;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.WorldBorder;
import org.spongepowered.plugin.meta.util.NonnullByDefault;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.Location;

import java.util.Optional;

@NonnullByDefault
public class RTPCommand implements CommandExecutor {
    /**
     * The maximum number of blocks along each axis the player can teleport to.
     */
    private static final int MAX_BLOCKS = 1000000;
    private static final double DISTANCE = FxBiomeRTP.getInstance().getConfig().getRtpRadius();

    private static final ICooldown COOLDOWN = new Cooldown(FxBiomeRTP.getInstance().getConfig().getRtpCooldown());

    private static final Text PREFIX = Text.of(TextColors.GOLD,"[",TextColors.GREEN,"RTP",TextColors.GOLD,"] ");

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Optional<Player> target = args.getOne("target");
        if (!(src instanceof Player) && !target.isPresent()) {
            src.sendMessage(Text.of(PREFIX,TextColors.RED, "Using the command from this source requires specifying a target"));
            return CommandResult.empty();
        }
        Player player = target.orElseGet(() -> (Player) src);
        Location<World> worldLoc;

        if (src instanceof Player
                && FxBiomeRTP.getInstance().getConfig().getRtpCooldown() > 0
                && !src.hasPermission(PluginInfo.COOLDOWN_PERM + "rtp")) {
            if (!COOLDOWN.isValid((Player) src)) {
                worldLoc = generateLocation(0, 500);
                tpRandom(worldLoc, (Player) src, target, player, true);
                throw new CommandException(Text.of(PREFIX, TextColors.YELLOW, "You cannot normal RTP for " + COOLDOWN.getPlayerDelayFormatted((Player) src), ". You have been teleported to the COOLDOWN RTP zone."));
            }
            COOLDOWN.addPlayer((Player) src);
        }

        double getDiameter = DISTANCE > 0 ? DISTANCE : 10000.00;
        worldLoc = generateLocation(0, getDiameter);
        tpRandom(worldLoc, (Player) src, target, player, false);

        return CommandResult.success();
    }

    public Location<World> generateLocation(int recursionEnd, double diameter){
        World world = Sponge.getServer().getWorld("world").get();
        int x = MathUtils.getRandomNumberInRange(0, Math.min(MAX_BLOCKS, (int) ((diameter - 1) / 2)));
        if (MathUtils.getRandomNumberInRange(0, 1) == 0) x = -x;
        int z = MathUtils.getRandomNumberInRange(0, Math.min(MAX_BLOCKS, (int) ((diameter - 1) / 2)));
        if (MathUtils.getRandomNumberInRange(0, 1) == 0) z = -z;

        Location<World> worldLoc = new Location<>(world, x, 90, z);
        Location<World> waterMaybe = new Location<>(world, worldLoc.getBlockX(), 61, worldLoc.getBlockZ());
        if((waterMaybe.getBlock().getType().equals(BlockTypes.WATER) || waterMaybe.getBlock().getType().equals(BlockTypes.FLOWING_WATER)) && recursionEnd < 5) {
            worldLoc = generateLocation(recursionEnd++, diameter);
        }

        return worldLoc;
    }

    public void tpRandom(Location<World> worldLoc, Player src, Optional<Player> target, Player player, boolean cooldown){
        Optional<Location<World>> optionalTargetLoc = Sponge.getTeleportHelper().getSafeLocation(worldLoc, 30, 5);
        if (optionalTargetLoc.isPresent()) {
            Location<World> targetLoc = optionalTargetLoc.get();
            player.setLocation(targetLoc);
            if (!cooldown)
                player.sendMessage(Text.of(PREFIX,TextColors.GREEN, "You have been randomly teleported!"));
            if (target.isPresent() && (!(src instanceof Player) || ((Player) src).getUniqueId() != target.get().getUniqueId())) {
                if(!cooldown)
                    src.sendMessage(Text.of(PREFIX,TextColors.GREEN, target.get().getName() + " has been randomly teleported!"));
            }
            player.sendMessage(Text.of(PREFIX, TextColors.YELLOW, "You arrived at " + player.getLocation().getX() + ", " + player.getLocation().getY() + ", " + player.getLocation().getZ(), " in the world."));
        }
    }
}
