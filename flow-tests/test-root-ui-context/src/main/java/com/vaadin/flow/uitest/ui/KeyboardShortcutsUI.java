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

        Div parent = new Div();
//        FocusableDiv parent = new FocusableDiv();
        parent.setWidth("50%");
        parent.setHeight("50%");
//        parent.setTabIndex(-1);
//        parent.getElement().setAttribute("style", "background-color: lightblue; ");

        parent.add(input);
        parent.add(label);

        add(parent);

        ComponentUtil.addShortcutListener(parent, KeyShortcut.of('F', KeyModifier.META), true, false, event -> {
            label.setText(event.getKey().toString() + ", " + event.getModifiers());
        });

    }
}

class FocusableDiv extends Div implements Focusable<Div> {

}