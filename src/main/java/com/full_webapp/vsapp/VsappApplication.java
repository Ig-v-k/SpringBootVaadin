package com.full_webapp.vsapp;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import lombok.*;
import lombok.extern.java.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static jdk.nashorn.internal.objects.NativeMath.log;

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
@Route("")
class MainView extends VerticalLayout {
  private static final long serialVersionUID = 1L;

  public MainView() { }

}

/*
* 	Repository
*/
@Repository
interface ContactRepository extends JpaRepository<Contact, Long> { }
@Repository
interface CompanyRepository extends JpaRepository<Company, Long> { }

/*
* 	Model
*/
@MappedSuperclass
@EqualsAndHashCode
abstract class AbstractEntity {
  @Id
  @Getter
  @GeneratedValue(strategy= GenerationType.SEQUENCE)
  private Long id;

  public boolean isPersisted() {
	return id != null;
  }
}

@Entity
@Data
@ToString
class Contact extends AbstractEntity implements Cloneable {

  public enum Status {
	ImportedLead, NotContacted, Contacted, Customer, ClosedLost
  }

  @NotNull
  @NotEmpty
  private String firstName = "";

  @NotNull
  @NotEmpty
  private String lastName = "";

  @ManyToOne
  @JoinColumn(name = "company_id")
  private Company company;

  @Enumerated(EnumType.STRING)
  @NotNull
  private Contact.Status status;

  @Email
  @NotNull
  @NotEmpty
  private String email = "";

}

@Entity
@Data
@NoArgsConstructor
class Company extends AbstractEntity {
  private String name;

  public Company(String name) {
    this.name = name;
  }

  @OneToMany(mappedBy = "company", fetch = FetchType.EAGER)
  private List<Contact> employees = new LinkedList<>();
}

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

@Log
@Service
class ContactService {
  private final ContactRepository contactRepository;
  private final CompanyRepository companyRepository;

  ContactService(ContactRepository contactRepository, CompanyRepository companyRepository) {
	this.contactRepository = contactRepository;
	this.companyRepository = companyRepository;
  }

  public List<Contact> findAll() {
	return contactRepository.findAll();
  }

  public long count() {
	return contactRepository.count();
  }

  public void delete(Contact contact) {
	contactRepository.delete(contact);
  }

  public void save(Contact contact) {
	if (contact == null) {
	  log(Level.SEVERE, "Contact is null. Are you sure you have connected your form to the application?");
	  return;
	}
	contactRepository.save(contact);
  }

  @PostConstruct
  public void populateTestData() {

	log.info("companyRepository -----------> " + companyRepository.toString());
	log.info("contactRepository -----------> " + contactRepository.toString());

	if (companyRepository.count() == 0) {
	  companyRepository.saveAll(
			Stream.of("Path-Way Electronics", "E-Tech Management", "Path-E-Tech Management")
				  .map(Company::new)
				  .collect(Collectors.toList()));
	}

	if (contactRepository.count() == 0) {
	  Random r = new Random(0);
	  List<Company> companies = companyRepository.findAll();
	  contactRepository.saveAll(
			Stream.of("Gabrielle Patel", "Brian Robinson", "Eduardo Haugen",
				  "Koen Johansen", "Alejandro Macdonald", "Angel Karlsson", "Yahir Gustavsson", "Haiden Svensson",
				  "Emily Stewart", "Corinne Davis", "Ryann Davis", "Yurem Jackson", "Kelly Gustavsson",
				  "Eileen Walker", "Katelyn Martin", "Israel Carlsson", "Quinn Hansson", "Makena Smith",
				  "Danielle Watson", "Leland Harris", "Gunner Karlsen", "Jamar Olsson", "Lara Martin",
				  "Ann Andersson", "Remington Andersson", "Rene Carlsson", "Elvis Olsen", "Solomon Olsen",
				  "Jaydan Jackson", "Bernard Nilsen")
				  .map(name -> {
					String[] split = name.split(" ");
					Contact contact = new Contact();
					contact.setFirstName(split[0]);
					contact.setLastName(split[1]);
					contact.setCompany(companies.get(r.nextInt(companies.size())));
					contact.setStatus(Contact.Status.values()[r.nextInt(Contact.Status.values().length)]);
					String email = (contact.getFirstName() + "." + contact.getLastName() + "@" + contact.getCompany().getName().replaceAll("[\\s-]", "") + ".com").toLowerCase();
					contact.setEmail(email);
					return contact;
				  }).collect(Collectors.toList()));
	}
  }
}

/*
* 	Type's
*/
