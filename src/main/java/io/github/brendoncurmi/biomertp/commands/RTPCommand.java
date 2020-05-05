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

package io.github.brendoncurmi.biomertp.commands;

import io.github.brendoncurmi.biomertp.api.MathUtils;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.plugin.meta.util.NonnullByDefault;

import java.util.Optional;

@NonnullByDefault
public class RTPCommand implements CommandExecutor {
    private static final int MAX_BLOCKS = 1000000;
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        Optional<Player> target = args.getOne("target");
        if (!(src instanceof Player) && !target.isPresent()) {
            src.sendMessage(Text.of(TextColors.RED, "Using the command from this source requires specifying a target!"));
            return CommandResult.empty();
        }
        Player player = target.orElseGet(() -> (Player) src);
        int x = MathUtils.getRandomNumberInRange(0, MAX_BLOCKS);
        int z = MathUtils.getRandomNumberInRange(0, MAX_BLOCKS);
        player.setLocation(new Location<>(player.getWorld(), x, player.getWorld().getHighestYAt(x, z), z));
        player.sendMessage(Text.of(TextColors.GREEN, "You have been randomly teleported!"));
        return CommandResult.success();
    }
}
