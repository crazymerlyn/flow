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

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.vaadin.flow.dom.DomListenerRegistration;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.shared.Registration;

public class ShortcutUtil {
    /**
     * Registers a {@link Shortcut} handling for the given {@link Component}.
     * When the component handles a shortcut, the given {@link ShortcutListener}
     * will be invoked.
     * <p>
     * The shortcut doesn't have to originate from the component itself - the
     * event event can be raised in the component's children. A common case
     * would be a form with multiple input fields. The form root will handle
     * the shortcut but it originates from one of the input fields.
     *
     * @param component     <code>Component</code> which catches and, potentially,
     *                      handles the shortcut
     * @param shortcut      Shortcut settings
     * @param listener      Handler for the shortcut event
     * @return {@link Registration} for unregistering the shortcut
     */
    public static Registration addShortcut(
            Component component, Shortcut shortcut, ShortcutListener listener) {

        Objects.requireNonNull(component, "Component must not be null");
        Objects.requireNonNull(shortcut, "Shortcut must not be null");
        Objects.requireNonNull(listener, "Listener must not be null");

        return addShortcutToComponent(
                shortcut, listener, component);
    }

    /**
     * Registers a {@link Shortcut} handling for the given {@link Component}.
     * When the component handles a shortcut, the given {@link Runnable}
     * will be invoked.
     * @param component <code>Component</code> which catches and, potentially,
     *                  handles the shortcut
     * @param shortcut  Shortcut settings
     * @param runnable  Handler for the shortcut event
     * @return {@link Registration} for unregistering the shortcut
     */
    public static Registration addShortcut(
            Component component, Shortcut shortcut, Runnable runnable) {
        Objects.requireNonNull(component, "Component must not be null");
        Objects.requireNonNull(shortcut, "Shortcut must not be null");
        Objects.requireNonNull(runnable, "Runnable must not be null");

        return addShortcut(component, shortcut, e -> runnable.run());
    }

    private static Registration addShortcutToComponent(
            Shortcut shortcut, ShortcutListener listener,
            Component component) {
        assert shortcut != null : "Shortcut was null";
        assert listener != null : "ShortcutListener was null";
        assert component != null: "Component was null";

        return addComponentShortcut(component, shortcut, listener);
    }

    /*
     *
     * @param ui
     * @param shortcut
     * @param listener
     * @return

    private static Registration addGlobalShortcut(UI ui, Shortcut shortcut,
                                                 ShortcutListener listener) {
        Objects.requireNonNull(ui, "Parameter ui must not be null.");
        Objects.requireNonNull(shortcut, "Parameter shortcut must not" +
                "be null.");
        Objects.requireNonNull(listener, "Parameter listener must not" +
                "be null.");

        return addElementShortcut(ui.getElement(), shortcut, listener);
    }
     */


    /**
     * Registers a {@link Shortcut} handling for the given {@link Element}.
     * When the element handles a shortcut, the given {@link ShortcutListener}
     * will be invoked.
     *
     * @param element   Listen for events on this Element and its children.
     * @param shortcut  Shortcut to be bound to the given element
     * @param listener  Shortcut listener
     * @return {@link Registration} for unregistering shortcut.
     * @see ShortcutUtil#addShortcut(Component, Shortcut, ShortcutListener)
     */
    public static Registration addShortcut(
            Element element, Shortcut shortcut, ShortcutListener listener) {
        DomListenerRegistration registration = element.addEventListener(
                "keydown",
                event -> listener.onShortcut(
                        new ShortcutEvent(shortcut.getConfiguration())));

        primeDomEventRegistration(shortcut, registration);

        return registration;
    }

    private static Registration addComponentShortcut(
            Component component, Shortcut shortcut, ShortcutListener listener) {
        assert component != null : "Component was null";
        assert shortcut != null : "Shortcut was null";
        assert listener != null : "ShortcutListener was null";
        return ComponentUtil.addListener(
                component,
                KeyDownEvent.class,
                event -> listener.onShortcut(
                        new ShortcutEvent(shortcut.getConfiguration(), event)
                ),
                domListenerRegistration ->
                        ShortcutUtil.primeDomEventRegistration(
                                shortcut, domListenerRegistration));
    }

    private static void primeDomEventRegistration(
            Shortcut shortcut, DomListenerRegistration registration) {
        assert shortcut != null : "Shortcut was null";
        assert registration != null : "Registration was null";

        registration.setFilter(shortcut.getConfiguration().filterText());
        if (shortcut.getPreventDefault()) {
            registration.addEventData("event.preventDefault()");
        }
        if (shortcut.getStopPropagation()) {
            registration.addEventData("event.stopPropagation()");
        }
    }
}
