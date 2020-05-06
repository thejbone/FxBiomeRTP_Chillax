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

package io.github.brendoncurmi.biomertp;

import io.github.brendoncurmi.biomertp.api.BiomeData;
import org.spongepowered.api.world.biome.BiomeType;
import org.spongepowered.api.world.biome.BiomeTypes;

import java.lang.reflect.Field;
import java.util.HashMap;

public class BiomeUtils {

    /**
     * Stores the biome field name and biome type as a key-value pair.
     */
    private static HashMap<String, BiomeType> biomeMap = new HashMap<>();

    public static BiomeType getBiome(String name) {
        return biomeMap.get(name.toLowerCase());
    }

    /**
     * Initializes the biome map.
     *
     * @throws IllegalAccessException if this {@code Field} object
     *                                is enforcing Java language access control and the underlying
     *                                field is inaccessible.
     */
    public static void initBiomes() throws IllegalAccessException {
        Field[] fields = BiomeTypes.class.getFields();
        for (Field field : fields) {
            String name = field.getName();
            BiomeType type = (BiomeType) field.get(null);
            biomeMap.put(name.toLowerCase(), type);
        }
    }

    /**
     * Stores the biome type and their biome data.
     */
    private static HashMap<BiomeType, BiomeData> scannedBiomes = new HashMap<>();

    public static BiomeData getBiomeData(BiomeType biomeType) {
        if (!scannedBiomes.containsKey(biomeType))
            scannedBiomes.put(biomeType, new BiomeData());
        return scannedBiomes.get(biomeType);
    }
}
