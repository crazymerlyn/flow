package com.vaadin.flow.uitest.ui;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Input;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.internal.KeyboardEvent;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.shared.Registration;

public class KeyboardShortcutsUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        super.init(request);

        Label label = new Label();

        Input input = new Input();
//        input.getElement().addEventListener("keydown", event -> {
//            label.setText(event.getEventData().toJson());
//        }).setFilter("event.key == 'f' && event.getModifierState('Meta')").addEventData("event.preventDefault()");

//        this.getElement().addEventListener("keydown", event -> {
//            label.setText(event.getEventData().toJson());
//        }).setFilter("event.key == 'U'");
//
//        label.getElement().addEventListener("keydown", event -> {
//            label.setText(event.getEventData().toJson());
//        }).setFilter("event.key == 'L'");
//
        Registration registration = ComponentUtil.addListener(this, KeyPressEvent.class, event -> {
            label.setText(event.getKey().toString() + ", " + event.getModifiers());
        });

        input.addShortcutListener(event -> {}, true, false, 'f', Key.META);


        add(input);
        add(label);
    }
}
