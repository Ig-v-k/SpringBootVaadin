package com.full_webapp.vsapp;

/**
 * A simple project for learn Vaadin with a spring
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
 * security
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
 * the users
 */
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
}

/**
 * a roles for the users
 */
//enum Roles implements Serializable {
//  USER, ADMIN;
//}

/**
 * init data
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

