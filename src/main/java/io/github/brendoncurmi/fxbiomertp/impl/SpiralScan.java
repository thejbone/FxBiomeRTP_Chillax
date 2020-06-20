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

package io.github.brendoncurmi.fxbiomertp.impl;

import io.github.brendoncurmi.fxbiomertp.FxBiomeRTP;
import io.github.brendoncurmi.fxbiomertp.api.BiomeUtils;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.WorldBorder;

public class SpiralScan {

    /**
     * The numbers of blocks in a chunk along the x/z axes.
     */
    private static final int CHUNK_SIZE = 16;

    /**
     * The maximum number of chunks to scan if the world border is not specified.
     */
    private static final int MAX_CHUNKS_XY = 1000;

    /**
     * The maximum number of blocks in the X/Y direction to scan if the world border is not specified.
     */
    private static final int MAX_BLOCKS_XY = MAX_CHUNKS_XY * CHUNK_SIZE;

    private World world;
    private Runnable runnable;

    public SpiralScan() {
        FxBiomeRTP inst = FxBiomeRTP.getInstance();
        runnable = location -> {
            // If biome isn't on the list (usually because it's modded), then add it
            if (!BiomeUtils.contains(location.getBiome()))
                BiomeUtils.add(location.getBiome());

            inst.getPersistenceData().getWorldData(world.getName())
                    .getBiomeData(BiomeUtils.getBiomeName(location.getBiome()))
                    .addCoord(location.getBlockX(), location.getBlockZ());
            inst.getLogger().info("Scanned " + location.getX() + "," + location.getZ());

            // 100ms delay between cycles for slowing down execution to not use up too many resources
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            //location.setBlockType(BlockTypes.BONE_BLOCK);
            //System.out.println(location.getChunkPosition());
        };
    }

    public void setWorld(World world) {
        this.world = world;
    }

    /**
     * Starts a spiral scan from origin (0,0) for a length of up to {@value MAX_CHUNKS_XY} chunks, within the world border.
     * This method handles the scan along 2 dimensions, hence why dealing with {@code X} and {@code Y}. Keep in mind that
     * replacing the Y axis with Z axis would probably be what you intend on doing if using this along 3 dimensions.
     */
    public void startScan() {
        WorldBorder border = world.getWorldBorder();
        int xy = Math.min(MAX_BLOCKS_XY, (int) border.getDiameter()) / CHUNK_SIZE;
        xy -= 2;// Remove 2 chunks to have enough distance from the world border
        if (xy < 0) xy = 0;
        startScan(xy, xy, 0, 0);
    }

    /**
     * Starts a spiral scan from origin (0,0) for a length of {@code X} and {@code Y} chunks.
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
