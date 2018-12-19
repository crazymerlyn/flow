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

import com.vaadin.flow.component.Shortcut.ShortcutConfiguration;

public class ShortcutEvent {
    private ShortcutConfiguration configuration;
    private ComponentEvent<? extends Component> componentEvent;


    /**
     * Creates a new {@code ShortcutEvent} which ties {@link ShortcutConfiguration}
     * together with a {@link ComponentEvent} which is responsible for generating
     * this event.
     * @param baseEvent     Event which caused the shortcut to be invoked
     * @param configuration ShortcutConfiguration detailing the exact shortcut
     *                      invoked
     */
    public ShortcutEvent(ComponentEvent<? extends Component> baseEvent,
                         ShortcutConfiguration configuration) {
        this.componentEvent = baseEvent;
        this.configuration = configuration;
    }

    /**
     * A shortcut for {@code ShortcutEvent.getEvent.getSource()}.
     * @return  The component from which the shortcut originates from
     */
    public Component source() {
        return this.componentEvent.getSource();
    }

    public ShortcutConfiguration getConfiguration() {
        return configuration;
    }

    public ComponentEvent<? extends Component> getEvent() {
        return componentEvent;
    }
}
