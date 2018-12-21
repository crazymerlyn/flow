/*
 * Copyright 2000-2018 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package com.vaadin.flow.component;

import java.io.Serializable;
import java.util.Objects;

import com.vaadin.flow.shared.Registration;

/**
 * A mixin interface for support catching shortcuts events that originate
 * from themselves or their child components.
 *
 * @author Vaadin Ltd.
 * @since
 */
public interface ShortcutNotifier extends Serializable {

    /**
     * Add a {@link ShortcutListener} to this component, which will receive
     * callbacks for the given {@link Shortcut}.
     * @param shortcut  Shortcut for which to receive events. Must not be
     *                  <code>null</code>.
     * @param listener  The listener to register. Must not be <code>null</code>.
     * @return {@link Registration} which can be used to remove the listener.
     */
    default Registration addShortcutListener(Shortcut shortcut,
                                             ShortcutListener listener) {
        Objects.requireNonNull(shortcut, "Parameter shortcut must not" +
                "be null.");
        if (this instanceof Component) {
            return ShortcutUtil.addShortcut((Component) this, shortcut,
                    listener);
        }
         else {
            throw new IllegalStateException(String.format(
                    "The class '%s' doesn't extend '%s'. "
                            + "Make your implementation for the method '%s'.",
                    getClass().getName(), Component.class.getSimpleName(),
                    "addShortcutListener(Shortcut, ShortcutListener)"));
        }
    }

    /**
     * Add a {@link Runnable} to be executed when the given {@link Shortcut} is
     * triggered.
     * @param shortcut  Shortcut for which to receive events. Must not be
     *                  <code>null</code>.
     * @param runnable  A callback which is executed when the shortcut is
     *                  triggered. Must not be <code>null</code>.
     * @return {@link Registration} which can be used to remove the listener.
     */
    default Registration addShortcutListener(Shortcut shortcut,
                                             Runnable runnable) {
        Objects.requireNonNull(shortcut, "Parameter shortcut must not" +
                "be null.");
        if (this instanceof Component) {
            return ShortcutUtil.addShortcut(
                    (Component) this, shortcut, runnable);
        }
        else {
            throw new IllegalStateException(String.format(
                    "The class '%s' doesn't extend '%s'. "
                            + "Make your implementation for the method '%s'.",
                    getClass().getName(), Component.class.getSimpleName(),
                    "addShortcutListener(Shortcut, Runnable)"));
        }
    }
}
