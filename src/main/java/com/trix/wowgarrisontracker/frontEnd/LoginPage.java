package com.trix.wowgarrisontracker.frontEnd;

import com.trix.wowgarrisontracker.utils.BlizzardRequestUtils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Component
@UIScope
@Profile("vaadin")
@Route(value = "login")
public class LoginPage extends FlexLayout implements BeforeEnterObserver {


    private LoginForm loginForm = new LoginForm();
    private BlizzardRequestUtils requestUtils;

    public LoginPage() {
        this.requestUtils = new BlizzardRequestUtils();
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        addClassName("background");

        loginForm.setAction("login");
        add(loginForm);

        Anchor registerLink = new Anchor("register", "Register");
        add(registerLink);

        Button request = new Button("Make blizzard request");
        request.addClickListener(event -> requestUtils.getListOfServers());
        add(request);

    }


    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (event.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            loginForm.setError(true);
        }
    }
}
