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

package io.github.brendoncurmi.fxbiomertp.api;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class TeleportHelper {

    /**
     * The number of blocks high (along the Y axis) to initially teleport the player.
     */
    private static final int HIGH_Y = 900;

    /**
     * Safely teleports the specified player to the surface of the specified x and z coordinates of the specified world.
     * @param player the player to teleport.
     * @param world the world to teleport the player to.
     * @param x the x coord to teleport the player to.
     * @param z the z coord to teleport the player to.
     */
    public static void teleportPlayer(Player player, World world, double x, double z) {
        // Teleport the player to the location but send them very high in the sky so the chunks load, then it's
        // possible to get the highest Y at the location and send the player to that Y (+2 to be outside blocks).
        player.setLocation(new Location<>(world, x, HIGH_Y, z));
        player.setLocation(new Location<>(world, x, player.getWorld().getHighestYAt((int) x, (int) z) + 2, z));
    }
}
