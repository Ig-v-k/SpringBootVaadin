package com.full_webapp.vsapp;

/**
 * A simple project for learn Vaadin with the Spring
 */


import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

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
@Route("")
class MainView extends VerticalLayout implements BeforeLeaveObserver {
  private final CustomerRepository customerRepository;
  Grid<Usr> grid = new Grid<>();
  Button loginButton = new Button("Login", new Icon(VaadinIcon.ARROW_LEFT));
  Dialog dialog = new Dialog(new Label("Are you sure you want to leave this page?"));
  NativeButton confirmButton = new NativeButton("Confirm");
  NativeButton cancelButton = new NativeButton("Cancel");
  MenuBar menuBar = new MenuBar();
  MenuItem menuBarMainPage = menuBar.addItem("Main");
  MenuItem menuBarLoginPage = menuBar.addItem("Login");

  public MainView(CustomerRepository customerRepository) {
	this.customerRepository = customerRepository;

//	this.customGridMain();
	this.customMenuBarMain();
	this.customButtonMain();
	this.customDialogMain();

	addClassName("main-view");

	add(/*grid, */loginButton, dialog, menuBar);
  }

  void customMenuBarMain() {
	menuBar.setOpenOnHover(true);
	menuBarMainPage.addClickListener(menuItemClickEvent -> UI.getCurrent().navigate(""));
	menuBarLoginPage.addClickListener(menuItemClickEvent -> UI.getCurrent().navigate("login"));
  }

  void customButtonMain() {
	loginButton.getElement().setAttribute("aria-label", "Click me");
	customButtonClickListenerMain();
  }

  void customDialogMain() {
	dialog.setCloseOnEsc(false);
	dialog.setCloseOnOutsideClick(false);
  }

  void customDialogNativeButtonsInto(BeforeLeaveEvent.ContinueNavigationAction action) {
	confirmButton.addClickListener(nativeButtonClickEvent ->
		  action.proceed()
	);
	cancelButton.addClickListener(nativeButtonClickEvent ->
		  dialog.close()
	);
	dialog.add(confirmButton, cancelButton);
  }

//  void customGridMain() {
//	grid.addClassName("usr-grid");
//	grid.setWidthFull();
//	grid.setHeight("800px");
//	grid.setItems(customerRepository.findAll());
//	customGridAddColumns();
//	customGridSelectionModeMulti();
//	customGridColumnReordering();
//	customGridAddColumnTemplateRendering();
//	customGridAddColumnComponentRendering();
//	customGridAddColumnBiConsumer();
//	customGridAddColumnTemplateRenderingButtons();
//  }

  void customButtonClickListenerMain() {
	loginButton.addClickListener(buttonClickEvent -> UI.getCurrent().navigate("login"));
  }

//  void customGridAddColumnsInLoop() {
//	grid.addColumn(usr -> usr.getId() + " " +
//		  usr.getFirstName() + " " +
//		  usr.getLastName() + " " +
//		  usr.getPatronymic()).setHeader("Id").setSortable(true);
//  }
//
//  void customGridColumnReordering() {
//	grid.setColumnReorderingAllowed(true);
//  }
//
//  void customGridAddColumns() {
//	grid.addColumn(Usr::getId).setFlexGrow(0).setWidth("100px").setResizable(true).setKey("Id-key").setFrozen(true);
//	grid.addColumn(Usr::getFirstName).setHeader("First Name").setKey("firstName-key");
//	grid.addColumn(Usr::getLastName).setHeader("Last Name").setKey("lastName-key");
//	grid.addColumn(Usr::getPatronymic).setHeader("Patronymic").setKey("patronymic-key");
//  }
//
//  void customGridAddColumnBiConsumer() {
//	SerializableBiConsumer<Div, Usr> consumer =
//		  (div, person) -> div.setText(person.getPatronymic() + " consumer");
//	grid.addColumn(
//		  new ComponentRenderer<>(Div::new, consumer))
//		  .setHeader("DivBiConsumer");
//  }
//
//  void customGridAddColumnComponentRendering() {
//	grid.addColumn(new ComponentRenderer<>(user -> {
//	  if (("Aa").equals(user.getLastName()) || ("Bb").equals(user.getLastName())) {
//		return new Icon(VaadinIcon.MALE);
//	  } else {
//		return new Icon(VaadinIcon.FEMALE);
//	  }
//	})).setHeader("Gender");
//	grid.addColumn(
//		  new ComponentRenderer<>(
//				() -> new Icon(VaadinIcon.ARROW_LEFT)));
//  }
//
//  void customGridAddColumnTemplateRendering() {
//	grid.addColumn(TemplateRenderer.<Usr>of(
//		  "<button on-click='handleUpdate'>Update</button>" + "<button on-click='handleRemove'>Remove</button>")
//		  .withEventHandler("handleUpdate", user -> {
//			Usr usr = customerRepository.findById(user.getId()).get();
//			usr.setFirstName(user.getFirstName() + "Updated");
//			customerRepository.save(usr);
//			grid.getDataProvider().refreshItem(user);
//		  })
//		  .withEventHandler("handleRemove", person -> {
//			ListDataProvider<Usr> dataProvider = (ListDataProvider<Usr>) grid.getDataProvider();
//			dataProvider.getItems().remove(person);
//			dataProvider.refreshAll();
//		  })
//	)
//		  .setHeader("Actions");
//  }
//
//  void customGridAddColumnTemplateRenderingButtons() {
//	grid.addColumn(new ComponentRenderer<>(person -> {
//	  TextField name = new TextField("Name_b");
//	  Usr usr = customerRepository.findById(1).get();
//	  name.setValue(usr.getFirstName() + "_b");
//	  Button update = new Button("Update", event -> {
//		usr.setFirstName(usr.getFirstName() + "_bb");
//		customerRepository.save(usr);
//		grid.getDataProvider().refreshItem(usr);
//	  });
//	  Button remove = new Button("Remove", event -> {
//		ListDataProvider<Usr> dataProvider = (ListDataProvider<Usr>) grid.getDataProvider();
//		dataProvider.getItems().remove(usr);
//		dataProvider.refreshAll();
//	  });
//	  HorizontalLayout buttons = new HorizontalLayout(update, remove);
//	  return new VerticalLayout(name, buttons);
//	})).setHeader("Actions");
//  }
//
//  void customGridAddTheme() {
//	grid.addThemeVariants(GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_ROW_STRIPES);
//  }
//
//  void customGridSelectionModeNone() {
//	grid.setSelectionMode(Grid.SelectionMode.NONE);
//	grid.addItemClickListener(event -> System.out.println(("Clicked Item: " + event.getItem())));
//  }
//
//  void customGridSelectionModeSingle() {
//	grid.setSelectionMode(Grid.SelectionMode.SINGLE);
//	grid.addSelectionListener(selectionEvent -> {
//	  selectionEvent.getFirstSelectedItem().ifPresent(user -> {
//		Notification.show(user.getFirstName() + " is selected");
//	  });
//	});
//  }
//
//  void customGridSelectionModeSingleSelect() {
//	grid.setSelectionMode(Grid.SelectionMode.SINGLE);
//	SingleSelect<Grid<Usr>, Usr> personSelect = grid.asSingleSelect();
//	personSelect.addValueChangeListener(e -> {
//	  Usr selectedPerson = e.getValue();
//	});
//	customGridSelectionModeSingleSelectDeselect();
//  }
//
//  void customGridSelectionModeMulti() {
//	grid.setSelectionMode(Grid.SelectionMode.MULTI);
//	MultiSelect<Grid<Usr>, Usr> multiSelect = grid.asMultiSelect();
//	multiSelect.addValueChangeListener(e -> {
//	  Set<Usr> selectedPersons = e.getValue();
//	});
//	grid.addSelectionListener(selectionEvent -> {
//	  Set<Usr> selected = selectionEvent.getAllSelectedItems();
//	  Notification.show(selected.size() + " items selected");
//	});
//	grid.addItemDoubleClickListener(event -> Notification.show(event.getItem().getFirstName() + " item double selected"));
////	customGridSelectionModeMultiSelectMode();
//  }
//
//  void customGridSelectionModeMultiSelectMode() {
//	GridMultiSelectionModel<Usr> selectionModel = (GridMultiSelectionModel<Usr>) grid.setSelectionMode(Grid.SelectionMode.MULTI);
//
//	selectionModel.selectAll();
//
//	selectionModel.addMultiSelectionListener(event -> {
//	  Notification.show(String.format(
//			"%s items added, %s removed.",
//			event.getAddedSelection().size(),
//			event.getRemovedSelection().size()));
//	  setEnabled(event.getNewSelection().isEmpty());
//	});
//  }
//
//  void customGridSelectionModeSingleSelectDeselect() {
//	GridSingleSelectionModel<Usr> singleSelect = (GridSingleSelectionModel<Usr>) grid.getSelectionModel();
//	singleSelect.setDeselectAllowed(false);
//  }

  @Override
  public void beforeLeave(BeforeLeaveEvent beforeLeaveEvent) {
	BeforeLeaveEvent.ContinueNavigationAction continueNavigationAction = beforeLeaveEvent.postpone();
	dialog.open();
//	beforeLeaveEvent.forwardTo(LoginView.class);
	customDialogNativeButtonsInto(continueNavigationAction);
  }
}

@Route("login")
class LoginView extends VerticalLayout {
  public LoginView() {
	LoginOverlay component = new LoginOverlay();
	H1 title = new H1();
	Icon icon = VaadinIcon.VAADIN_H.create();
	title.getStyle().set("color", "var(--lumo-base-color)");
	icon.setSize("30px");
	icon.getStyle().set("top", "-4px");
	title.add(icon);
	title.add(new Text(" My App"));
	component.setTitle(title);
	component.addLoginListener(e -> component.close());

	LoginI18n i18n = LoginI18n.createDefault();
	i18n.setAdditionalInformation("To close the login form submit non-empty username and password");
	component.setI18n(i18n);

	Button open = new Button("Open login overlay",
		  e -> component.setOpened(true));

	add(open, component, title, icon);
  }
}

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
//@Route(value = "app")
//class MainView extends VerticalLayout {
//  private final CustomUserService userService;
//  final Grid<User> grid;
//
//  public MainView(CustomUserService userService) {
//	this.userService = userService;
//	this.grid = new Grid<>(User.class);
//	add(grid);
//	listCustomers();
//  }
//
//  private void listCustomers() {
//	grid.setItems(userService.getAllUsers());
//  }
//}

/**
 * configuration
 */
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//class WebMvcUserDetailsServiceConfiguration extends WebSecurityConfigurerAdapter {

//  private final CustomUserService userService;
//  private final PasswordEncoder passwordEncoder;

//  @Override
//  protected void configure(HttpSecurity http) throws Exception {
//	http
//		  .csrf().disable()
//		  .authorizeRequests()
//		  .antMatchers("/app/**").hasAnyRole("ADMIN", "USER")
//		  .anyRequest().authenticated()
//		  .and()
//		  .formLogin().disable();
//	http.logout().invalidateHttpSession(true);
//	http.addFilterAfter(developmentCasMockFilter(), LogoutFilter.class);
//	http.addFilterBefore(singleLogoutFilter(), CasAuthenticationFilter.class);
//	http.addFilter(casAuthenticationFilter());
//	http.addFilterBefore(requestLogoutFilter(), LogoutFilter.class);
//	http.sessionManagement().sessionAuthenticationStrategy(sessionAuthenticationStrategy());
//		  .loginPage("/app/login")
//		  .failureUrl("/app/login?error")
//		  .loginProcessingUrl("/app-login")
//		  .usernameParameter("userN")
//		  .passwordParameter("userP")
//		  .defaultSuccessUrl("/app").permitAll()
//		  .and()
//		  .logout()
//		  .logoutUrl("/app-logout")
//		  .logoutSuccessUrl("/app/logout").permitAll()
//		  .and()
//		  .exceptionHandling()
//		  .accessDeniedPage("/app/error");
//  }

//  @Override
//  public void configure(WebSecurity web) throws Exception {
//	web
//		  .ignoring().antMatchers(
//		  "/VAADIN/**",
//		  "/favicon.ico",
//		  "/robots.txt",
//		  "/manifest.webmanifest",
//		  "/sw.js",
//		  "/offline-page.html",
//		  "/frontend/**",
//		  "/webjars/**",
//		  "/frontend-es5/**", "/frontend-es6/**");
//  }

//  @Override
//  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//	auth
//		  .userDetailsService(userService)
//		  .passwordEncoder(passwordEncoder);
//  }
//
//  static boolean isUserLoggedIn() {
//	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//	return authentication != null
//		  && !(authentication instanceof AnonymousAuthenticationToken)
//		  && authentication.isAuthenticated();
//  }
//}

//@Component
//class ConfiguredUIServiceInitListener implements VaadinServiceInitListener {
//  @Override
//  public void serviceInit(ServiceInitEvent event) {
//	event.getSource().addUIInitListener(uiEvent -> {
//	  final UI ui = uiEvent.getUI();
//	  ui.addBeforeEnterListener(this::beforeEnter);
//	});
//  }
//
//  private void beforeEnter(BeforeEnterEvent event) {
//	if (!LoginView.class.equals(event.getNavigationTarget()) && !SecurityUtils.isUserLoggedIn()) {
//	  event.rerouteTo(LoginView.class);
//	}
//  }
//}

/**
 * service
 */
//@Service
//@RequiredArgsConstructor
//class CustomUserService/* implements UserDetailsService */ {
//  private final UserRepository userRepository;
//
//  User getUserByUserName(String userName) {
//	return userRepository.findByUserName(userName);
//  }
//
//  List<User> getAllUsers() {
//	return userRepository.findAll();
//  }
//
//  List<User> getAllUsersByUserName(String userName) {
//	return userRepository.findAllByUserName(userName);
//  }

//  @Override
//  public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
//	User user = userRepository.findByUserName(s);
//	if (null == user) {
//	  throw new UsernameNotFoundException(s);
//	}
//	return new CustomUserPrincipal(user);
//  }
//}

/**
 * jpa - repository
 */
@Repository
interface CustomerRepository extends JpaRepository<Usr, Integer> {
  @Query("from Usr c " +
		"where concat(c.firstName, ' ', c.lastName, ' ', c.patronymic) " +
		"like concat('%', :name, '%')")
  List<Usr> findByName(@Param("name") String name);
}
//@Repository
//interface UserRepository extends JpaRepository<User, Integer> {
//  User findByUserName(String userName);
//
//  @Query("from User u where concat(u.userName) like concat('%', :userName, '%')")
//  List<User> findAllByUserName(@Param("userName") String userName);
//}

/**
 * the Users wrapper
 */
//@AllArgsConstructor
//class CustomUserPrincipal implements UserDetails {
//  private User user;
//
//  @Override
//  public Collection<? extends GrantedAuthority> getAuthorities() {
//	return user.getRoles();
//  }
//
//  @Override
//  public String getPassword() {
//	return user.getPassword();
//  }
//
//  @Override
//  public String getUsername() {
//	return user.getUserName();
//  }
//
//  @Override
//  public boolean isAccountNonExpired() {
//	return true;
//  }
//
//  @Override
//  public boolean isAccountNonLocked() {
//	return true;
//  }
//
//  @Override
//  public boolean isCredentialsNonExpired() {
//	return true;
//  }
//
//  @Override
//  public boolean isEnabled() {
//	return true;
//  }
//}

/**
 * pojo
 */
@Data
@Entity
@NoArgsConstructor
class Usr {
  @Id
  @GeneratedValue
  private Integer id;
  private String firstName;
  private String lastName;
  private String patronymic;
}
//@Data
//@Entity
//@ToString
//@EqualsAndHashCode
//@Table(name = "usr")
//class User{
//  @Id
//  @GeneratedValue(strategy = GenerationType.AUTO)
//  @Column(name = "id")
//  private Integer id;
//
//  @NotNull
//  @NotBlank(message = "Username can't be null")
//  @Column(nullable = false, name = "username", unique = true)
//  private String userName;
//
//  @NotNull
//  @NotBlank(message = "Password can't be null")
//  @Column(nullable = false, name = "password")
//  private String password;

//  @Column(name = "roles")
//  @Enumerated(EnumType.STRING)
//  @ElementCollection(targetClass = Roles.class, fetch = FetchType.EAGER)
//  @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
//  private Set<Roles> roles = new HashSet<Roles>(0);
//}

/**
 * roles
 */
//enum Roles implements Serializable {
//  USER, ADMIN;
//}

/**
 * data initialization
 */
//@Slf4j
//@Component
//@RequiredArgsConstructor
//class SimpleDataInitializer implements CommandLineRunner {
//  private final UserRepository userRepository;
//
//  @Override
//  public void run(String... args) throws Exception {
//	Stream
//		  .of(
//				new User("user", "password", Collections.singleton(USER)),
//				new User("admin", "password", Stream.of(USER, ADMIN).collect(Collectors.toSet()))
//				new User("user", "password"),
//				new User("admin", "password")
//		  )
//		  .forEachOrdered(userRepository::save);

//	User[] users = new User[]{
//		  new User("user", "password"),
//	new User("admin", "password")
//	};
//
//
//	for(int i = 0; i < 2; i++) {
//	  userRepository.save(users[i]);
//	}
//
//	Stream
//		  .of(userRepository.findAll())
//		  .map(users -> users.toString() + "\n")
//		  .forEachOrdered(log::info);
//  }
//}