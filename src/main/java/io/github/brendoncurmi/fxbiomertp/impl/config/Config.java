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

package io.github.brendoncurmi.fxbiomertp.impl.config;

import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class Config extends AbstractConfig {
    @SuppressWarnings("UnstableApiUsage")
    public final static TypeToken<Config> type = TypeToken.of(Config.class);

    @Inject
    @Setting("rtp-cooldown")
    private int rtpCooldown = 0;
    @Inject
    @Setting("rtp-radius")
    private int rtpRadius = 0;
    @Inject
    @Setting("biomertp-cooldown")
    private int biomeRtpCooldown = 0;
    @Inject
    @Setting("rtp-world")
    private String world = "world";


    @Override
    public int getRtpCooldown() {
        return rtpCooldown;
    }
    public String getWorld() {
        return world;
    }

    public int getRtpRadius() {
        return rtpRadius;
    }

    @Override
    public int getBiomeRtpCooldown() {
        return biomeRtpCooldown;
    }
}
