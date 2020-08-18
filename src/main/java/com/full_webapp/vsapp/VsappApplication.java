package com.full_webapp.vsapp;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.HandlerHelper;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.shared.ApplicationConstants;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * main
 */
@SpringBootApplication
public class VsappApplication {
  public static void main(String[] args) {
	SpringApplication.run(VsappApplication.class, args);
  }
}

/**
 * views
 */
@Route(value = "app-layout-basic")
@PageTitle("Basic App Layout")
class BasicAppLayoutView extends AppLayout {
  public BasicAppLayoutView() {
	Image img = new Image("https://i.imgur.com/GPpnszs.png", "Vaadin Logo");
	img.setHeight("44px");
	addToNavbar(new DrawerToggle(), img);
	Tabs tabs = new Tabs(new Tab("Home"), new Tab("About"));
	tabs.setOrientation(Tabs.Orientation.VERTICAL);
	addToDrawer(tabs);
	VerticalLayout main = new VerticalLayout();

	Button sourceButton = new Button("View source code",
		  new Image("icons/iconfinder_mark-github_298822_64.png", "View source code"),
		  event -> UI.getCurrent().getPage().setLocation("https://github.com/Ig-v-k/SpringBootVaadin/blob/master/src/main/java/com/full_webapp/vsapp/VsappApplication.java"));

	main.add(new H1("Header text"), new Paragraph("Main content goes here."), sourceButton);
	setContent(main);
  }
}

@EnableWebSecurity
@Configuration
class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  private static final String LOGIN_PROCESSING_URL = "/login";
  private static final String LOGIN_FAILURE_URL = "/login?error";
  private static final String LOGIN_URL = "/login";
  private static final String LOGOUT_SUCCESS_URL = "/app-layout-basic";

  private final UserDetailsService userDetailsService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  public SecurityConfiguration(UserDetailsService userDetailsService) {
	this.userDetailsService = userDetailsService;
  }

  /**
   * The password encoder to use when encrypting passwords.
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
	return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	super.configure(auth);
	auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
	http.csrf().disable()
		  .requestCache().disable()
		  .authorizeRequests()
		  .requestMatchers(AnyRequestMatcher.INSTANCE).permitAll()
		  .anyRequest().hasAnyAuthority("USER", "ADMIN")
		  .and().formLogin().loginPage(LOGIN_URL).permitAll().loginProcessingUrl(LOGIN_PROCESSING_URL)
		  .failureUrl(LOGIN_FAILURE_URL)
		  .successHandler(new SavedRequestAwareAuthenticationSuccessHandler())
		  .and().logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL);
  }

  @Override
  public void configure(WebSecurity web) {
	web.ignoring().antMatchers(
		  "/VAADIN/**",
		  "/favicon.ico",
		  "/robots.txt",
		  "/manifest.webmanifest",
		  "/sw.js",
		  "/offline-page.html",
		  "/icons/**",
		  "/images/**",
		  "/frontend/**",
		  "/webjars/**",
		  "/h2-console/**",
		  "/frontend-es5/**", "/frontend-es6/**");
  }
}

@Service
@Primary
class UserDetailsServiceImpl implements UserDetailsService {
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	return new org.springframework.security.core.userdetails.User("user", "password", Collections.singletonList(new SimpleGrantedAuthority("USER")));
  }
}

@Route
@PageTitle("Vaadin Demo Bakery App")
@JsModule("./styles/shared-styles.js")
class LoginView extends LoginOverlay implements AfterNavigationObserver, BeforeEnterObserver {

  public LoginView() {
	LoginI18n i18n = LoginI18n.createDefault();
	i18n.setHeader(new LoginI18n.Header());
	i18n.getHeader().setTitle("Vaadin Demo Bakery App");
	i18n.getHeader().setDescription(
		  "admin@vaadin.com + admin\n" + "barista@vaadin.com + barista");
	i18n.setAdditionalInformation(null);
	i18n.setForm(new LoginI18n.Form());
	i18n.getForm().setSubmit("Sign in");
	i18n.getForm().setTitle("Sign in");
	i18n.getForm().setUsername("Email");
	i18n.getForm().setPassword("Password");
	setI18n(i18n);
	setForgotPasswordButtonVisible(false);
	setAction("login");
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
	if (Helper.isUserLoggedIn()) {
	  event.forwardTo(BasicAppLayoutView.class);
	} else {
	  setOpened(true);
	}
  }


  @Override
  public void afterNavigation(AfterNavigationEvent event) {
	setError(
		  event.getLocation().getQueryParameters().getParameters().containsKey(
				"error"));
  }
}

@SpringComponent
class ConfigureUIServiceInitListener implements VaadinServiceInitListener {

  @Override
  public void serviceInit(ServiceInitEvent event) {
	event.getSource().addUIInitListener(uiEvent -> {
	  final UI ui = uiEvent.getUI();
	  ui.add(new OfflineBanner());
	  ui.addBeforeEnterListener(this::beforeEnter);
	});
  }

  private void beforeEnter(BeforeEnterEvent event) {
	final boolean accessGranted = Helper.isAccessGranted(event.getNavigationTarget());
	if (!accessGranted) {
	  if (Helper.isUserLoggedIn()) {
		event.rerouteToError(AccessDeniedException.class);
	  } else {
		event.rerouteTo(LoginView.class);
	  }
	}
  }
}

@Tag("offline-banner")
@JsModule("./src/components/offline-banner.js")
@NpmPackage(value="@polymer/iron-ajax", version = "3.0.1")
class OfflineBanner extends Component {

}

@Tag("access-denied-view")
@JsModule("./src/views/errors/access-denied-view.js")
@ParentLayout(BasicAppLayoutView.class)
@PageTitle("Access denied")
class AccessDeniedView extends PolymerTemplate<TemplateModel> implements HasErrorParameter<AccessDeniedException> {

  @Override
  public int setErrorParameter(BeforeEnterEvent beforeEnterEvent, ErrorParameter<AccessDeniedException> errorParameter) {
	return HttpServletResponse.SC_FORBIDDEN;
  }
}

class AccessDeniedException extends RuntimeException {
  public AccessDeniedException() {
  }

  public AccessDeniedException(String message) {
	super(message);
  }
}

class Helper {
  public static boolean isUserLoggedIn() {
	return isUserLoggedIn(SecurityContextHolder.getContext().getAuthentication());
  }

  private static boolean isUserLoggedIn(Authentication authentication) {
	return authentication != null && !(authentication instanceof AnonymousAuthenticationToken);
  }

  public static boolean isAccessGranted(Class<?> securedClass) {
	final boolean publicView = LoginView.class.equals(securedClass)
		  || AccessDeniedView.class.equals(securedClass)
		  || CustomRouteNotFoundError.class.equals(securedClass);

	if (publicView) {
	  return true;
	}

	Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();

	if (!isUserLoggedIn(userAuthentication)) {
	  return false;
	}

	Secured secured = AnnotationUtils.findAnnotation(securedClass, Secured.class);
	if (secured == null) {
	  return true;
	}

	List<String> allowedRoles = Arrays.asList(secured.value());
	return userAuthentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
		  .anyMatch(allowedRoles::contains);
  }

  static boolean isFrameworkInternalRequest(HttpServletRequest request) {
	final String parameterValue = request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER);
	return parameterValue != null
		  && Stream.of(HandlerHelper.RequestType.values()).anyMatch(r -> r.getIdentifier().equals(parameterValue));
  }
}

@ParentLayout(BasicAppLayoutView.class)
@PageTitle("Page was not found")
@JsModule("./styles/shared-styles.js")
class CustomRouteNotFoundError extends RouteNotFoundError {

  public CustomRouteNotFoundError() {
	RouterLink link = Component.from(
		  ElementFactory.createRouterLink("", "Go to the front page."),
		  RouterLink.class);
	getElement().appendChild(new Text("Oops you hit a 404. ").getElement(), link.getElement());
  }

  @Override
  public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> parameter) {
	return HttpServletResponse.SC_NOT_FOUND;
  }
}