package com.full_webapp.vsapp;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;

/**
 * 	Hierarchy:
 *		-Main
 *		-Repository
 *		-Model
 *		-Type's
 */
/*
* 	Main
*/
@SpringBootApplication
public class VsappApplication {
  public static void main(String[] args) {
	SpringApplication.run(VsappApplication.class, args);
  }
}

  /*
  *   Init data's
  */


/*
* 	UI
*/
@Route
@PWA(name = "Vaadin Application", shortName = "Vaadin App", description = "This is an example Vaadin application.", enableInstallPrompt = true)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
class MainView extends VerticalLayout {
  private static final long serialVersionUID = 1L;

  /**
   * Construct a new Vaadin view.
   * <p>
   * Build the initial UI state for the user accessing the application.
   *
   * @param service The message service. Automatically injected Spring managed
   *                bean.
   */
  public MainView(@Autowired GreetService service) {

	TextField textField = new TextField("Your name");
	Button button = new Button("Say hello", e -> Notification.show(service.greet(textField.getValue())));
	button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
	button.addClickShortcut(Key.ENTER);
	addClassName("centered-content");
	add(new H1("Hello world"), new H2("Hello"), textField, button);
  }

}

/*
* 	Repository
*/


/*
* 	Model
*/

/*
* 	Service
*/
@Service
class GreetService {

  public String greet(String name) {
	if (name == null || name.isEmpty()) {
	  return "Hello anonymous user";
	} else {
	  return "Hello " + name;
	}
  }

}

/*
* 	Type's
*/
