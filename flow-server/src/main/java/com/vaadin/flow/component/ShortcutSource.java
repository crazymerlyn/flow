package com.vaadin.flow.component;

import com.vaadin.flow.shared.Registration;

import java.util.*;
import java.util.stream.Collectors;

public class ShortcutSource {

    private Shortcut shortcut;

    private Set<Component> sources;

    private Map<Component, Set<Registration>> source2Registrations;

    private boolean removeOnDetach = false;

    private ShortcutSource() {}

    ShortcutSource(Shortcut shortcut, Component... components) {
        this.shortcut = shortcut;
        this.sources = new HashSet<>(Arrays.asList(components));
        this.source2Registrations = new HashMap<>();
    }

    public ShortcutSource removeOnDetach() {
        removeOnDetach = true;

        sources.forEach(source -> source.addDetachListener(event -> {
            getRegistrationsWithSource(event.getSource()).forEach(Registration::remove);

            // TODO: whether to remove them from source2Registrations
        }));

        return this;
    }

    public ShortcutSource removeOnComponentDetach(Component detachComponent) {
        removeOnDetach = true;

        detachComponent.addDetachListener(event -> {
            getAllRegistrations().forEach(Registration::remove);

            // TODO: whether to clear source2Registrations
        });

        return this;
    }

    public Registration addCallback(Runnable callback) {
        return this.addListener(event -> callback.run());
    }

    public Registration addListener(ShortcutListener listener) {

        Set<Registration> registrations = sources.stream()
                .map(source -> {
                    Registration registration = ShortcutUtil.addShortcut(source, shortcut, listener);

                    if (removeOnDetach) {
                        addRegistrationWithSource(source, registration);
                    }

                    return registration;
                })
                .collect(Collectors.toSet());

        return () -> {
            registrations.forEach(Registration::remove);
        };
    }

    private void addRegistrationWithSource(Component source, Registration registration) {
        getRegistrationsWithSource(source).add(registration);
    }

    private Set<Registration> getRegistrationsWithSource(Component source) {
        Set<Registration> registrations = source2Registrations.get(source);

        if (registrations == null) {
            registrations = new HashSet<>();
            source2Registrations.put(source, registrations);
        }

        return registrations;
    }

    private Set<Registration> getAllRegistrations() {
        return source2Registrations.values().stream().flatMap(Set::stream).collect(Collectors.toSet());
    }

}
