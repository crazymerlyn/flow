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

import com.vaadin.flow.shared.Registration;

public interface HasShortcut extends Serializable {
    default Registration addShortcut(Shortcut shortcut,
                                     ShortcutListener listener) {
        if (this instanceof Component) {
            return ComponentUtil.addListener(
                    (Component) this,
                    KeyDownEvent.class,
                    event -> listener.onShortcut(
                            new ShortcutEvent(event,
                                    shortcut.getConfiguration())
                    ),
                    domListenerRegistration -> {
                        domListenerRegistration.setFilter(shortcut.filterText());
                        if (shortcut.getPreventDefault()) {
                            domListenerRegistration
                                    .addEventData("event.preventDefault()");
                        }
                        if (shortcut.getPreventBubbling()) {
                            domListenerRegistration
                                    .addEventData("event.stopPropagation()");
                        }
                    });
        }
         else {
            throw new IllegalStateException(String.format(
                    "The class '%s' doesn't extend '%s'. "
                            + "Make your implementation for the method '%s'.",
                    getClass().getName(), Component.class.getSimpleName(),
                    "addShortcut"));
        }
    }

    default Registration addShortcut(Shortcut shortcut, Runnable runnable) {
        return addShortcut(shortcut, event -> runnable.run());
    }
}
