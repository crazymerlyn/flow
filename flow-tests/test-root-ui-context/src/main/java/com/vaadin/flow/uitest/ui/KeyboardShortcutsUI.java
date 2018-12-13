package com.vaadin.flow.uitest.ui;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Input;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.internal.KeyboardEvent;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.shared.Registration;

public class KeyboardShortcutsUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        super.init(request);

        Div parent = new Div();
        parent.getElement().setAttribute("style", "width:80%; height:80%; background-color:lightblue; ");

        embed(parent, this, KeyShortcut.of('F', KeyModifier.META), true, false);

        FocusableDiv focusableParent = new FocusableDiv();
        focusableParent.setTabIndex(-1);
        focusableParent.getElement().setAttribute("style", "width:100%; height:50%; background-color:lightgreen; ");

        embed(focusableParent, parent, KeyShortcut.of('F', KeyModifier.META), true, true);

        FocusableDiv focusableParent2 = new FocusableDiv();
        focusableParent2.setTabIndex(-1);
        focusableParent2.getElement().setAttribute("style", "width:100%; height:50%; background-color:pink; ");

        embed(focusableParent2, focusableParent, KeyShortcut.of('F', KeyModifier.META), true, false);

    }

    private void embed(Div div, HasComponents parent, KeyShortcut shortcut, boolean preventDefault, boolean stopPropagation) {
        Label label = new Label();
        Input input = new Input();

        div.add(input);
        div.add(label);

        parent.add(new Hr());
        parent.add(div);

        ComponentUtil.addShortcutListener(div, shortcut, preventDefault, stopPropagation, event -> {
            label.setText(event.getKey().toString() + ", " + event.getModifiers());
        });

    }
}

class FocusableDiv extends Div implements Focusable<Div> {

}

