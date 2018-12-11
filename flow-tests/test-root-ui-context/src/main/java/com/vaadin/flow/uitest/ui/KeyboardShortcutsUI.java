package com.vaadin.flow.uitest.ui;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.KeyDownEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Input;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.internal.KeyboardEvent;
import com.vaadin.flow.server.VaadinRequest;

public class KeyboardShortcutsUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        super.init(request);

        Label label = new Label();

        Input input = new Input();
        input.getElement().addEventListener("keydown", event -> {
            label.setText(event.getEventData().toJson());
        }).setFilter("event.key == 'X'");

        ComponentUtil.addListener(this, KeyDownEvent.class, event -> {
            label.setText(event.getKey().toString() + ", " + event.getModifiers());
        });

        add(input);
        add(label);
    }
}
