package com.full_webapp.vsapp;

/**
 *
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static com.full_webapp.vsapp.Roles.*;

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
 * security
 */
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@Order(Ordered.HIGHEST_PRECEDENCE)
class WebMvcUserDetailsServiceConfiguration extends WebSecurityConfigurerAdapter {

  private final CustomUserDetailsServer customUserDetailsServer;
  @Bean
  DaoAuthenticationProvider daoAuthenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(customUserDetailsServer);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(daoAuthenticationProvider());
  }

}

/**
 * custom user service
 */
@Service
@RequiredArgsConstructor
class CustomUserDetailsServer implements UserDetailsService {
  private final UserRepository userRepository;
  @Override
  public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
    User user = userRepository.findByUserName(s);
    if (null == user) {
      throw new UsernameNotFoundException(s);
    }
    return new CustomUserPrincipal(user);
  }
}

/**
 * jpa
 */
@Repository
interface UserRepository extends JpaRepository<User, Integer> {
  User findByUserName(String userName);
}

/**
 * the Users wrapper
 */
@AllArgsConstructor
class CustomUserPrincipal implements UserDetails {
  private User user;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return user.getRoles();
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getUserName();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}

/**
 * the users
 */
@Data
@EqualsAndHashCode
@Entity
@Table(name = "usr")
class User implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private Integer id;

  @Column(nullable = false, name = "username", unique = true)
  @NotNull
  private String userName;

  @Column(name = "password")
  @NotNull
  private String password;

  @ElementCollection(targetClass = Roles.class, fetch = FetchType.EAGER)
  @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
  @Enumerated(EnumType.STRING)
  private Set<Roles> roles = new HashSet<Roles>(0);

  public User(String userName, String password, Set<Roles> collections) {
    this.userName = userName;
    this.password = password;
    this.roles = collections;
  }
}

/**
 * a roles for the users
 */
enum Roles implements GrantedAuthority, Serializable {
  USER, ADMIN;

  @Override
  public String getAuthority() {
    return name();
  }
}

/**
* init data
*/
@Slf4j
@Component
class SimpleDataInitializer {
  @Bean
  CommandLineRunner runner(UserRepository userRepository) {
    return args -> Stream
          .of(
                new User("user", "password", Collections.singleton(USER)),
                new User("admin", "password", Collections.singleton(ADMIN))
          )
          .forEach(userRepository::save);
  }
}
