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

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.WorldBorder;

public class SpiralScan {

    /**
     * The numbers of blocks in a chunk along the x/z axes.
     */
    private static final int CHUNK_SIZE = 16;

    private static final int MAX_CHUNKS_XY = 10000;
    private static final int MAX_BLOCKS_XY = MAX_CHUNKS_XY / CHUNK_SIZE;

    private World world;
    private Runnable runnable;

    public SpiralScan(Runnable runnable) {
        this.runnable = runnable;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    /**
     * Starts a spiral scan from origin (0, 0) for a length of up to {@value MAX_CHUNKS_XY} chunks, within world border.
     * This method handles the scan along 2 dimensions, hence why dealing with {@code X} and {@code Y}. Keep in mind that
     * replacing the Y axis with Z axis would probably be what you intend on doing if using this along 3 dimensions.
     */
    public void startScan() {
        WorldBorder border = world.getWorldBorder();
        int xy = Math.min(MAX_BLOCKS_XY, (int) ((border.getDiameter() - 1) / (CHUNK_SIZE * 2)));
        startScan(xy, xy, 0, 0);
    }

    /**
     * Starts a spiral scan from origin (0, 0) for a length of {@code X} and {@code Y} chunks.
     * This method handles the scan along 2 dimensions, hence why dealing with {@code X} and {@code Y}. Keep in mind that
     * replacing the Y axis with Z axis would probably be what you intend on doing if using this along 3 dimensions.
     *
     * @param X the maximum number of x chunks that will be scanned.
     * @param Y the maximum number of y chunks that will be scanned.
     */
    public void startScan(final int X, final int Y) {
        startScan(X, Y, 0, 0);
    }

    /**
     * Starts a spiral scan from the {@code dX} and {@code dY} locations for a length of {@code X} and {@code Y} chunks.
     * This method handles the scan along 2 dimensions, hence why dealing with {@code X} and {@code Y}. Keep in mind that
     * replacing the Y axis with Z axis would probably be what you intend on doing if using this along 3 dimensions.
     *
     * @param X  the maximum number of x chunks that will be scanned.
     * @param Y  the maximum number of y chunks that will be scanned.
     * @param dX the starting location along x axis.
     * @param dY the starting location along y axis.
     */
    public void startScan(final int X, final int Y, final int dX, final int dY) {
        int x = 0, y = 0, dx = 0, dy = -1;
        int t = Math.max(X, Y);
        int maxI = t * t;
        for (int i = 0; i < maxI; i++) {
            if ((-X / 2 <= x) && (x <= X / 2) && (-Y / 2 <= y) && (y <= Y / 2)) {
                int blockX = ((x * CHUNK_SIZE) + (CHUNK_SIZE / 2)) + dX;
                int blockY = 88;
                int blockZ = ((y * CHUNK_SIZE) + (CHUNK_SIZE / 2)) + dY;
                Location<World> blockLoc = new Location<>(world, blockX, blockY, blockZ);
                runnable.run(blockLoc);
            }

            if ((x == y) || ((x < 0) && (x == -y)) || ((x > 0) && (x == 1 - y))) {
                t = dx;
                dx = -dy;
                dy = t;
            }
            x += dx;
            y += dy;
        }
    }

    public interface Runnable {
        void run(Location<World> location);
    }
}
