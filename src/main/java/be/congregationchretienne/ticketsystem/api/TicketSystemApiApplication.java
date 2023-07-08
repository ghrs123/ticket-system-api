package be.congregationchretienne.ticketsystem.api;

import be.congregationchretienne.ticketsystem.api.model.*;
import be.congregationchretienne.ticketsystem.api.model.type.Priority;
import be.congregationchretienne.ticketsystem.api.model.type.Status;
import be.congregationchretienne.ticketsystem.api.repository.*;
import java.time.LocalDateTime;
import javax.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@SpringBootApplication
public class TicketSystemApiApplication {

  EntityManager entityManager;

  public static void main(String[] args) {
    new TicketSystemApiApplication().exec(args);
  }

  private void exec(String[] args) {
    var context = SpringApplication.run(TicketSystemApiApplication.class, args);
    UserRepository userRepository = context.getBean(UserRepository.class);
    TicketRepository ticketRepository = context.getBean(TicketRepository.class);
    CategoryRepository categoryRepository = context.getBean(CategoryRepository.class);
    DepartmentRepository departmentRepository = context.getBean(DepartmentRepository.class);

    User user = new User();
    user.setEmail(System.currentTimeMillis() + "-gustavo@email.com");
    user.setName("User2 test");
    userRepository.save(user);

    Department department = new Department();
    department.setName("Informatique");
    department.setCreatedBy(user);

    Category category = new Category();
    category.setCreatedBy(user);
    category.setName("Suporte");

    department.getCategories().add(category);
    category.getDepartments().add(department);
    department.getUsers().add(user);
    user.getMyDepartments().add(department);
    categoryRepository.save(category);
    departmentRepository.save(department);

    Ticket ticket = new Ticket();
    ticket.setTitle("My task title 2");
    ticket.setDescription("Category 2");
    ticket.setStartedOn(LocalDateTime.now());
    ticket.setAssignedTo(user);
    ticket.setCreatedBy(user);
    ticket.setReference(RandomStringUtils.randomAlphanumeric(10, 11));
    ticket.setPriority(Priority.HIGH);
    ticket.setEstimation(3);
    ticket.setStatus(Status.OPEN);
    ticket.setCategory(category);
    ticket.setDepartment(department);
    ticketRepository.save(ticket);

    var list = ticketRepository.findAll();

    log.info("list", list);
  }

  /*
  public static void main(String[] args) {
    SpringApplication.run(TicketSystemApiApplication.class, args);

    Set<Category> listCategory = new HashSet<>();
    Set<User> listUsers = new HashSet<>();
    Set<Ticket> listTickets = new HashSet<>();

    // assignedTo = new User(UUID.randomUUID(),"user1","user1@mail.com");
    Category category = new Category("categoryName");
    User assignedTo = new User("name1", "email1@mail.com");
    User createdBy = new User("name2", "email2@mail.com");
    Department department =
        new Department("departmentName", createdBy, listCategory, listUsers, listTickets);

    Status status = new Status("statusName", createdBy);

    Ticket ticket =
        new Ticket(
            "title",
            "description",
            20393055L,
            "priority",
            assignedTo,
            department,
            category,
            5,
            LocalDateTime.of(2022, 11, 10, 15, 5),
            LocalDateTime.now(),
            status,
            createdBy);

    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson = Converters.registerLocalDateTime(gsonBuilder).create();

    // converte objetos Java para JSON e retorna JSON como String
    List<String> json =
        new ArrayList<>(
            List.of(
                gson.toJson(department),
                gson.toJson(category),
                gson.toJson(assignedTo),
                gson.toJson(createdBy),
                gson.toJson(ticket)));
    // String json = gson.toJson(ticket);
    try {
      // Escreve Json convertido em arquivo chamado "file.json"
      FileWriter writer =
          new FileWriter(
              "D:\\CCBProject\\workspace\\ccb-ticket-system\\JsonToObject\\ObjectToJson.json");

      writer.write(String.valueOf(json));
      writer.close();

    } catch (IOException e) {
      e.printStackTrace();
    }

    System.out.println(json);
  }*/
}
