package com.vaadin.flow.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Shortcut {

    private boolean preventDefault = true;

    private boolean preventBubbling = true;

    private HashSet<Component> sources;

    private ShortcutConfiguration configuration = new ShortcutConfiguration();

    public static Shortcut of(char key, KeyModifier... modifiers) {

        Key[] keys = new Key[modifiers.length + 1];
        System.arraycopy(modifiers, 0, keys, 0, modifiers.length);
        keys[modifiers.length] = Key.of(("" + key).toUpperCase());

        return new Shortcut(keys);
    }

    public static Shortcut of(Key... keys) {
        return new Shortcut(keys);
    }

    public Shortcut withKey(char key) {
        return withKey(Key.of(("" + key).toUpperCase()));
    }

    public Shortcut withKey(Key key) {
        configuration.addKey(key);
        configuration.updateKeyIdentifier();
        return this;
    }

    public Shortcut withAllowDefault() {
        preventDefault = false;
        return this;
    }

    public Shortcut withAllowBubbling() {
        preventBubbling = false;
        return this;
    }

    public Shortcut withSources(Component... components) {
        this.sources = new HashSet<>(Arrays.asList(components));
        return this;
    }

    private Shortcut(Key... keys) {
        Arrays.stream(keys).forEach(configuration::addKey);
        configuration.updateKeyIdentifier();
    }

    public Optional<HashSet<Component>> getSources() {
        return Optional.of(sources);
    }

    public ShortcutConfiguration getConfiguration() {
        // TODO: use copy constructor?
        return this.configuration;
    }

    String filterText() {
        String modifierFilter = configuration.modifiers.stream().map(modifier -> "event.getModifierState('" + modifier.getKeys().get(0) + "')").collect(Collectors.joining(" && "));

        return "event.key.toLowerCase() == '" + configuration.keys.iterator().next().getKeys().get(0).toLowerCase() + "'" +
                " && " + (modifierFilter.isEmpty() ? "true" : modifierFilter);
    }

    public boolean getPreventDefault() {
        return preventDefault;
    }

    public boolean getPreventBubbling() {
        return preventBubbling;
    }

    public class ShortcutConfiguration {
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
    }

}
