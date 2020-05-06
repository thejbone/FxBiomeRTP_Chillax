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

import java.io.*;

/**
 * This factory handles the serialization and deserialization of serializable objects.
 */
public class FileFactory {

    /**
     * Serializes the specified serializable object to the specified file.
     *
     * @param serializable the object to serialize.
     * @param path         the path to write the file to.
     */
    public static void serialize(Serializable serializable, String path) {
        try {
            FileOutputStream outputStream = new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(outputStream);
            out.writeObject(serializable);
            out.close();
            outputStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Deserializes the specified file path to the data object.
     *
     * @param path the path to read the file from.
     * @return the deserialized data object.
     */
    public static Serializable deserialize(String path) {
        Serializable serializable = null;
        try {
            FileInputStream inputStream = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(inputStream);
            serializable = (Serializable) in.readObject();
            in.close();
            inputStream.close();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return serializable;
    }
}
