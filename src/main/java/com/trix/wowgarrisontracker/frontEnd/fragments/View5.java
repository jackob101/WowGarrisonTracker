package com.trix.wowgarrisontracker.frontEnd.fragments;

import com.vaadin.flow.router.Route;

@Route(value = "view5", layout = MainLayout.class)
public class View5 extends AbstractView {
    @Override
    String getViewName() {
        return getClass().getName();
    }
}
