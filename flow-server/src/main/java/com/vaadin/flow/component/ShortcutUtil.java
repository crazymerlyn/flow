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

import java.util.Objects;

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
     * <p>
     * In order to prevent the children from swallowing a potential shortcut,
     * use {@link Shortcut#withSources(Component...)} to register components
     * in which the shortcut can be invoked.
     *
     * @param component     <code>Component</code> which catches and, potentially,
     *                      handles the shortcut
     * @param shortcut      Shortcut settings
     * @param listener      Handler for the shortcut event
     * @return {@link Registration} for unregistering the shortcut
     */
    public static Registration addShortcut(Component component,
                                           Shortcut shortcut,
                                           ShortcutListener listener) {
        return ComponentUtil.addListener(
                component,
                KeyDownEvent.class,
                event -> listener.onShortcut(
                        new ShortcutEvent(shortcut.getConfiguration(), event
                        )
                ),
                domListenerRegistration ->
                        ShortcutUtil.primeDomEventRegistration(
                                shortcut, domListenerRegistration));
    }

    public static Registration addGlobalShortcut(UI ui, Shortcut shortcut,
                                                 ShortcutListener listener) {
        Objects.requireNonNull(ui, "Parameter ui must not be null.");
        Objects.requireNonNull(shortcut, "Parameter shortcut must not" +
                "be null.");
        Objects.requireNonNull(listener, "Parameter listener must not" +
                "be null.");

        return addElementShortcut(ui.getElement(), shortcut, listener);
    }


    /**
     * Add a keyboard shortcut to an Element.
     *
     * @param element   Listen for events on this Element and its children.
     * @param shortcut  Shortcut to be bound to the given element
     * @param listener  Shortcut listener
     * @return {@link Registration} for unregistering shortcut.
     */
    private static Registration addElementShortcut(Element element,
                                                   Shortcut shortcut,
                                                   ShortcutListener listener) {
        DomListenerRegistration registration = element.addEventListener(
                "keydown",
                event -> listener.onShortcut(
                        new ShortcutEvent(shortcut.getConfiguration())));

        primeDomEventRegistration(shortcut, registration);

        return registration;
    }

    private static void primeDomEventRegistration(
            Shortcut shortcut, DomListenerRegistration registration) {
        registration.setFilter(shortcut.filterText());
        if (shortcut.getPreventDefault()) {
            registration.addEventData("event.preventDefault()");
        }
        if (shortcut.getStopPropagation()) {
            registration.addEventData("event.stopPropagation()");
        }
    }
}
