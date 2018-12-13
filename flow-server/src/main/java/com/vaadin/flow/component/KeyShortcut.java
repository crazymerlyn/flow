package com.vaadin.flow.component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class KeyShortcut {

    // TODO: Shall we name this KeySequence instead? Its use case is for shortcuts, but itself it's a sequence, unordered sequence.

    public static KeyShortcut of(char key, KeyModifier... modifiers) {

        Key[] keys = new Key[modifiers.length + 1];
        System.arraycopy(modifiers, 0, keys, 0, modifiers.length);
        keys[modifiers.length] = Key.of("" + key);

        return new KeyShortcut(keys);
    }

    private Set<Key> modifiers;

    private Set<Key> keys;

    private KeyShortcut(Key... keys) {

        this.modifiers = new HashSet<Key>(2);
        this.keys = new HashSet<Key>(1);

        Arrays.stream(keys).forEach(key -> {
            if (Key.isModifier(key)) {
                this.modifiers.add(key);
            } else {
                this.keys.add(key);
            }
        });
    }

    String filterText() {
        String modifierFilter = StreamSupport.stream(modifiers.spliterator(), false).map(modifier -> "event.getModifierState('" + modifier.getKeys().get(0) + "')").collect(Collectors.joining(" && "));

        String filter = "event.key.toLowerCase() == '" + keys.iterator().next().getKeys().get(0).toLowerCase() + "'" +
                " && " + (modifierFilter.isEmpty() ? "true" : modifierFilter);

        return filter;
    }

}
