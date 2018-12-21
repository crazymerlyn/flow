package com.vaadin.flow.component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Defines a shortcut.
 * <p>
 *     The <code>Shortcut</code> contains both the definitions on the triggers
 *     that can cause a shortcut event to be raised (currently only key events),
 *     and configuration of how the event should behave on the dom. It also
 *     contains configuration information about the environmental context of the
 *     shortcut; Which components can generate the shortcut and what is the
 *     context where the shortcut is present.
 * </p>
 * <p>
 *     <code>Shortcut</code> offers a fluent API for constructing a <code>
 *     Shortcut</code> object. When a shortcut event is detected, {@link
 *     ShortcutConfiguration} will be provided with the event data. If the user
 *     has registered the same callback for multiple shortcuts, {@link
 *     ShortcutConfiguration#equals(Object)} can be used to check for a matching
 *     shortcut case.
 * </p>
 *
 * @author Vaadin Ltd.
 * @since
 */
public class Shortcut implements Serializable {

    private boolean preventDefault = true;

    private boolean stopPropagation = true;

    private Set<Component> sources;

    private ShortcutConfiguration configuration = new ShortcutConfiguration();

    private Shortcut(Key... keys) {
        Arrays.stream(keys).forEach(configuration::addKey);
        configuration.updateKeyIdentifier();
    }

    /**
     * Create a <code>Shortcut</code> instance using a char and optional
     * modifier keys.
     *
     * @param key           Shortcut key, e.g. 'c' or 'C';
     * @param modifiers     Modifier keys for constructing a key combination,
     *                      e.g. "alt+u"
     * @return The constructed <code>Shortcut</code> instance
     */
    public static Shortcut of(char key, KeyModifier... modifiers) {
        Key[] keys = new Key[modifiers.length + 1];
        System.arraycopy(modifiers, 0, keys, 0, modifiers.length);
        keys[modifiers.length] = charToKey(key);

        return new Shortcut(keys);
    }

    /**
     * Create a <code>Shortcut</code> instance using any {@link Key}
     * combination. Note that by custom, most shortcuts consist of one
     * alphanumeric character, and one or two modifier keys.
     * @param keys  Keys used to define the shortcut combination
     * @return The constructed <code>Shortcut</code> instance
     * @see KeyModifier
     */
    public static Shortcut of(Key... keys) {
        return new Shortcut(keys);
    }

    /**
     * Fluent way to add a char-based key.
     * @param key   Shortcut key, e.g. 'c' or 'C';
     * @return this shortcut
     */
    public Shortcut withKey(char key) {
        return withKey(charToKey(key));
    }

    /**
     * Fluent way to add a {@link Key}-based key.
     * @param key   Shortcut key
     * @return this shortcut
     */
    public Shortcut withKey(Key key) {
        configuration.addKey(key);
        configuration.updateKeyIdentifier();
        return this;
    }

    /**
     * Enable defaults behavior for the underlying event
     * @return this shortcut
     */
    public Shortcut withAllowDefault() {
        preventDefault = false;
        return this;
    }

    /**
     * Enable event bubbling.
     * @return this shortcut
     */
    public Shortcut withAllowPropagation() {
        stopPropagation = false;
        return this;
    }

    /**
     * Designated a collection of {@link Component Components} which are
     * considered "prime event sources". These components will share the event
     * propagation and default-handling settings with the main component.
     * @param components    Components which are part of the shortcut event
     *                      generation context.
     * @return this shortcut
     */
    public Shortcut withSources(Component... components) {
        this.sources = new HashSet<>(Arrays.asList(components));
        return this;
    }

    /**
     * A {@link Set} of {@link Component Components} considered as event
     * sources. If no sources are configured, an empty set will be returned.
     * @return Set of source components
     */
    public Set<Component> getSources() {
        return sources == null ? Collections.emptySet()
                : new HashSet<>(sources);
    }

    public ShortcutConfiguration getConfiguration() {
        // TODO: use copy constructor?
        return this.configuration;
    }

    public boolean getPreventDefault() {
        return preventDefault;
    }

    public boolean getStopPropagation() {
        return stopPropagation;
    }

    private static Key charToKey(char c) {
        return Key.of(("" + c).toLowerCase());
    }

    /**
     * <code>ShortcutConfiguration</code> is the static result of creating a
     * {@link Shortcut}. The configuration contains only the information which
     * can be used to identify a shortcut. All dom-event related configuration
     * is stored in <code>Shortcut</code>.
     */
    public class ShortcutConfiguration implements Serializable {
        private Set<Key> modifiers;
        private Set<Key> keys;
        // TODO: gestures here
        private String keySequenceId;

        private ShortcutConfiguration() {
            this.modifiers = new HashSet<>(2);
            this.keys = new HashSet<>(1);
        }

        private void addKey(Key key) {
            if (Key.isModifier(key)) {
                this.modifiers.add(key);
            } else {
                this.keys.add(key);
            }
        }

        private void updateKeyIdentifier() {
            Collection<Key> scKeys = new ArrayList<>(keys);
            scKeys.addAll(modifiers);
            keySequenceId = scKeys.stream().map(Key::getKeys)
                    .flatMap(List::stream).sorted(String::compareToIgnoreCase)
                    .collect(Collectors.joining());
        }

        @Override
        public int hashCode() {
            return keySequenceId == null ? -1 : keySequenceId.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof ShortcutConfiguration) {
                return this.keySequenceId.equals(((ShortcutConfiguration)obj).keySequenceId);
            }
            return false;
        }

        String filterText() {
            String modifierFilter = modifiers.stream().map(modifier -> "event.getModifierState('" + modifier.getKeys().get(0) + "')").collect(Collectors.joining(" && "));

            return "event.key.toLowerCase() == '" + keys.iterator().next().getKeys().get(0).toLowerCase() + "'" +
                    " && " + (modifierFilter.isEmpty() ? "true" : modifierFilter);
        }
    }

}
