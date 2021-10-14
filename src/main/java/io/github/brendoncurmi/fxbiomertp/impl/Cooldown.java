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

import io.github.brendoncurmi.fxbiomertp.api.ICooldown;
import org.spongepowered.api.entity.living.player.Player;

import java.util.HashMap;

public class Cooldown implements ICooldown {

    private HashMap<Player, Long> cache;
    private int delay;

    public Cooldown(int delay) {
        this.delay = delay;
        this.cache = new HashMap<>();
    }

    @Override
    public void addPlayer(Player player) {
        cache.put(player, now() + delay);
    }

    public long getPlayerDelay(Player player) {  return (cache.get(player) - now()); }

    public String getPlayerDelayFormatted(Player player){
        long delay = getPlayerDelay(player);
        return (delay / 60) + " mins and " + (delay % 60) + " secs";
    }

    @Override
    public boolean isValid(Player player) {
        return !cache.containsKey(player) || cache.get(player) < now();
    }

    @Override
    public int getDelay() {
        return delay;
    }

    private long now() {
        return System.currentTimeMillis() / 1000L;
    }
}
