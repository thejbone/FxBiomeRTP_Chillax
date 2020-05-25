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

import io.github.brendoncurmi.fxbiomertp.api.MathUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BiomeData implements Serializable {
    private static final long serialVersionUID = -7032943812492347010L;

    /**
     * Lists all the spawnable coordinates for this biome type.
     */
    private List<int[]> coords = new ArrayList<>();

    public void addCoord(int x, int y) {
        coords.add(new int[]{x, y});
    }

    public int[] getRandomCoord() {
        if (coords.size() == 0) throw new IllegalArgumentException("Coords is empty");
        return coords.get(MathUtils.getRandomNumberInRange(0, coords.size() - 1));
    }
}
