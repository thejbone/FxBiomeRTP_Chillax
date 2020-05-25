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

package io.github.brendoncurmi.fxbiomertp.impl.data;

import java.io.Serializable;
import java.util.HashMap;

public class PersistenceData implements Serializable {
    private static final long serialVersionUID = -4356793588541650937L;

    /**
     * Stores the name-object for the worlds.
     */
    private HashMap<String, WorldData> scannedWorlds = new HashMap<>();

    public HashMap<String, WorldData> getScannedWorlds() {
        return scannedWorlds;
    }

    public boolean hasScannedWorld(String worldName) {
        return scannedWorlds.containsKey(worldName);
    }

    public void createWorld(String worldName) {
        scannedWorlds.put(worldName, new WorldData());
    }

    public void removeWorld(String worldName) {
        scannedWorlds.remove(worldName);
    }

    public WorldData getWorldData(String worldName) {
        if (!hasScannedWorld(worldName)) createWorld(worldName);
        return scannedWorlds.get(worldName);
    }
}
