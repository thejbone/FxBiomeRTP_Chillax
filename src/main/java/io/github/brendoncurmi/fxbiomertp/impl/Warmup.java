package io.github.brendoncurmi.fxbiomertp.impl;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.Listener;

import java.util.HashMap;

public class Warmup {
    private HashMap<Player, Long> cache;
    private int delay;

    public Warmup(int delay) {
        this.delay = delay;
        this.cache = new HashMap<>();
    }

    public void addPlayer(Player player) {
        cache.put(player, now() + delay);
    }
    public void removePlayer(Player player) {
        cache.remove(player);
    }

    public long getPlayerDelay(Player player) {  return (cache.get(player) - now()); }

    public String getPlayerDelayFormatted(Player player){
        long delay = getPlayerDelay(player);
        return (delay % 60) + " secs";
    }

    public boolean isValid(Player player) {
        return cache.containsKey(player);
    }

    public int getDelay() {
        return delay;
    }

    private long now() {
        return System.currentTimeMillis() / 1000L;
    }
}
