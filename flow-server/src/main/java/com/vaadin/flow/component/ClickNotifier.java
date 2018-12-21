/*
 * Copyright 2000-2018 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.flow.component;

import java.io.Serializable;
import java.security.InvalidParameterException;

import com.vaadin.flow.shared.Registration;

/**
 * Mixin interface for components that support adding click listeners to the
 * their root elements.
 *
 * @param <T>
 *            the type of the component returned at the
 *            {@link ClickEvent#getSource()}
 *
 * @author Vaadin Ltd
 * @since 1.0
 */
public interface ClickNotifier<T extends Component> extends Serializable {
    /**
     * Adds a click listener to this component.
     *
     * @param listener
     *            the listener to add, not <code>null</code>
     * @return a handle that can be used for removing the listener
     */
    default Registration addClickListener(
            ComponentEventListener<ClickEvent<T>> listener) {
        if (this instanceof Component) {
            return ComponentUtil.addListener((Component) this, ClickEvent.class,
                    (ComponentEventListener) listener);
        } else {
            throw new IllegalStateException(String.format(
                    "The class '%s' doesn't extend '%s'. "
                            + "Make your implementation for the method '%s'.",
                    getClass().getName(), Component.class.getSimpleName(),
                    "addClickListener"));
        }
    }

    /**
     * Adds a click listener to this component with a shortcut which invokes
     * the click listener.
     *
     * @param listener
     *            the listener to add, not <code>null</code>
     * @param shortcut
     *            the shortcut to add, not <code>null</code> and must have
     *            <code>sources</code>.
     * @return a handle that can be used for removing the listener
     */
    default Registration addClickListener(
            ComponentEventListener<ClickEvent<T>> listener,
            Shortcut shortcut) {

        if (shortcut == null) {
            throw new InvalidParameterException(String.format(
                    "Parameter %s cannot be null.",
                    Shortcut.class.getSimpleName()));
        }

        if (!(this instanceof Component)) {
            throw new IllegalStateException(String.format(
                    "The class '%s' doesn't extend '%s'. "
                            + "Make your implementation for the method '%s'.",
                    getClass().getName(), Component.class.getSimpleName(),
                    "addClickListener"));
        }

        if (shortcut.getSources().isEmpty()) {
            throw new InvalidParameterException(String.format(
                    "Parameter %s does not have any sources configured. Cannot" +
                            "register a %s for a click without sources.",
                    Shortcut.class.getSimpleName(),
                    Shortcut.class.getSimpleName()
            ));
        }

        final Registration clickRegistration = ComponentUtil.addListener(
                (Component) this, ClickEvent.class,
                (ComponentEventListener) listener);

        final Registration shortcutRegistration = ShortcutUtil.addShortcut(
                (Component) this, shortcut,
                event -> listener.onComponentEvent(new ClickEvent<>(
                        (Component) this
                )));

        return () -> {
            clickRegistration.remove();
            shortcutRegistration.remove();
        };
    }
}
