package com.vaadin.flow.uitest.ui;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Input;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.server.VaadinRequest;

public class KeyboardShortcutsUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        super.init(request);

        Div parent = new Div();
        parent.getElement().setAttribute("style", "width:80%; height:80%; background-color:lightblue; ");

        embed(parent, this, ShortcutRegistration.of('F', KeyModifier.META), true, false, "1");

        Div focusableParent = new Div();
        focusableParent.getElement().setAttribute("style", "width:100%; height:50%; background-color:lightgreen; ");
        ComponentUtil.makeFocusable(focusableParent);

        embed(focusableParent, parent, ShortcutRegistration.of('F', KeyModifier.META), true, true, "2");

        Div focusableParent2 = new Div();
        focusableParent2.getElement().setAttribute("style", "width:100%; height:50%; background-color:pink; ");
//        ComponentUtil.makeFocusable(focusableParent2);

        embed(focusableParent2, focusableParent, ShortcutRegistration.of('F', KeyModifier.META), false, false, "3");

        ComponentUtil.addShortcut(UI.getCurrent(), ShortcutRegistration.of('A'), true, true, this::crudView);
    }

    private void crudView() {
        System.out.println("!!! crudView");
    }

    private void embed(Div div, HasComponents parent, ShortcutRegistration shortcut, boolean preventDefault, boolean stopPropagation, String tag) {
        Label label = new Label();
        Input input = new Input();

        div.add(input);
        div.add(label);

        parent.add(new Hr());
        parent.add(div);

//        ComponentUtil.addKeyboardListener(div, shortcut, preventDefault, stopPropagation, event -> {
//            String message = tag + ": " + event.getKey().getKeys().get(0).toString() + ", " + event.getModifiers();
//
//            System.out.println(message);
//
//            label.setText(message);
//        });

    }

}

