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

public class WorldData implements Serializable {
    private static final long serialVersionUID = 7690517287644795439L;

    /**
     * Stores the biome type and their biome data.
     */
    private HashMap<String, BiomeData> scannedBiomes = new HashMap<>();

    public HashMap<String, BiomeData> getScannedBiomes() {
        return scannedBiomes;
    }

    public void setScannedBiomes(HashMap<String, BiomeData> scannedBiomes) {
        this.scannedBiomes = scannedBiomes;
    }

    public boolean empty() {
        return scannedBiomes.isEmpty();
    }

    public boolean hasBiome(String biome) {
        return scannedBiomes.containsKey(biome.toLowerCase());
    }

    public BiomeData getBiomeData(String biome) {
        biome = biome.toLowerCase();
        if (!hasBiome(biome)) scannedBiomes.put(biome, new BiomeData());
        return scannedBiomes.get(biome);
    }
}
