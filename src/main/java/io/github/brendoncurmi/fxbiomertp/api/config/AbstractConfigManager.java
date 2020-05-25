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

package io.github.brendoncurmi.fxbiomertp.api.config;

import io.github.brendoncurmi.fxbiomertp.FxBiomeRTP;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.IOException;
import java.nio.file.Path;

public abstract class AbstractConfigManager<T extends ConfigurationNode, L extends ConfigurationLoader<T>> {
    private final L loader;
    private T node;

    /**
     * Constructor to load the config file at the specified file. Config version is not checked or updated.
     *
     * @param path the path of the config file.
     * @throws IOException if an I/O exception occurs.
     */
    protected AbstractConfigManager(Path path) throws IOException {
        this(path, false);
    }

    /**
     * Constructor to load the config file at the specified file. If <code>checkVersion</code> is true, the file
     * version is checked and updated with new entries if it is old.
     *
     * @param path         the path of the config file.
     * @param checkVersion whether the version should be checked and config updated.
     * @throws IOException if an I/O exception occurs.
     */
    protected AbstractConfigManager(Path path, boolean checkVersion) throws IOException {
        loader = getLoader(path);
        load(checkVersion);
    }

    /**
     * Loads and saves the config file from the loader, assigning it to this class' node. If <code>checkVersion</code>
     * is true, the file version is checked and updated with new entries if it is old.
     *
     * @param checkVersion whether the version should be checked and config updated.
     * @throws IOException if an I/O exception occurs.
     */
    protected void load(boolean checkVersion) throws IOException {
        node = loader.load();
        if (checkVersion) {
            int version = Integer.parseInt(FxBiomeRTP.VERSION.substring(FxBiomeRTP.VERSION.indexOf(".") + 1));
            if (node.getNode("version").getInt() < version) {
                configUpdater(version);
            }
        }
        save();
    }

    /**
     * Updates the config file from the default config.
     * @param newVersion the new version of the config file.
     * @throws IOException if an I/O exception occurs.
     */
    protected void configUpdater(int newVersion) throws IOException {
        node.mergeValuesFrom(getDefaultsFrom(loader));
        node.getNode("version").setValue(newVersion);
    }

    /**
     * Saves the class' node through the loader.
     *
     * @throws IOException if an I/O exception occurs.
     */
    protected void save() throws IOException {
        loader.save(node);
    }

    /**
     * Gets the loader of the config.
     *
     * @return the config loader.
     */
    public L getLoader() {
        return loader;
    }

    /**
     * Gets the class' node.
     *
     * @return the node.
     */
    public T getNode() {
        return node;
    }

    /**
     * Builds and gets the loader for the config at the specified file.
     *
     * @param path the path of the config file.
     * @return the built loader.
     */
    protected abstract L getLoader(Path path);

    /**
     * The default node to merge values from.
     *
     * @param loader the config loader.
     * @return the default node to merge from.
     * @throws IOException if an I/O exception occurs.
     */
    protected abstract T getDefaultsFrom(L loader) throws IOException;
}
