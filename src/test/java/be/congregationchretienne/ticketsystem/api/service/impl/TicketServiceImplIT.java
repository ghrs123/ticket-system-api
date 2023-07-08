package be.congregationchretienne.ticketsystem.api.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.congregationchretienne.ticketsystem.api.dto.CategoryDTO;
import be.congregationchretienne.ticketsystem.api.dto.DepartmentDTO;
import be.congregationchretienne.ticketsystem.api.dto.TicketDTO;
import be.congregationchretienne.ticketsystem.api.dto.UserDTO;
import be.congregationchretienne.ticketsystem.api.dto.type.PriorityDTO;
import be.congregationchretienne.ticketsystem.api.dto.type.StatusDTO;
import be.congregationchretienne.ticketsystem.api.exception.NotFoundException;
import be.congregationchretienne.ticketsystem.api.helper.ValidationHelper;
import be.congregationchretienne.ticketsystem.api.model.Ticket;
import be.congregationchretienne.ticketsystem.api.repository.TicketRepository;
import be.congregationchretienne.ticketsystem.api.service.CategoryService;
import be.congregationchretienne.ticketsystem.api.service.DepartmentService;
import be.congregationchretienne.ticketsystem.api.service.TicketService;
import be.congregationchretienne.ticketsystem.api.service.UserService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.flywaydb.core.Flyway;
import org.flywaydb.test.annotation.FlywayTest;
import org.flywaydb.test.junit5.annotation.FlywayTestExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@FlywayTest
@Import({
  TicketServiceImpl.class,
  UserServiceImpl.class,
  CategoryServiceImpl.class,
  DepartmentServiceImpl.class
})
@FlywayTestExtension
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TicketServiceImplIT {

  @Autowired private TicketService ticketService;
  @Autowired private TicketRepository ticketRepository;
  @Autowired private UserService userService;
  @Autowired private DepartmentService departmentService;
  @Autowired private CategoryService categoryService;

  @Autowired private Flyway flyway;

  @BeforeEach
  public void before() throws Exception {
    flyway.clean();
    flyway.migrate();
  }

  @Test
  void testGetTicketOk() {
    // given
    var uuid = UUID.fromString("082722c7-856f-4a39-b8dd-20cb08a6996c");

    // when
    var result = ticketService.get(uuid.toString());

    // Then
    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid.toString(), result.getId());
  }

  private static Stream<Arguments> invalidIds() {
    String nullVar = null;
    return Stream.of(
        Arguments.of(nullVar, "The resource reference must be not null."),
        Arguments.of("", "The resource reference must be not null."),
        Arguments.of("  ", "The resource reference must be not null."),
        Arguments.of("123", "The resource reference is invalid."));
  }

  private static Stream<Arguments> IllegalParameterOderBy() {
    String nullVar = null;
    return Stream.of(
        Arguments.of(nullVar, 50, "The parameter orderBy [null] is invalid."),
        Arguments.of("ASC", 51, "The number items per page should be between 1 and 50."),
        Arguments.of(" ", 50, "The parameter orderBy [ ] is invalid."));
  }

  private static Stream<Arguments> invalidDeleteIds() {
    String nullVar = null;
    return Stream.of(
        Arguments.of(nullVar, "The resource reference must be not null."),
        Arguments.of("1212", "The resource reference is invalid."));
  }

  @ParameterizedTest
  @MethodSource("invalidIds")
  void testGetTicketWIthInvalidId(String id, String expectedMessage) {
    // given
    // when
    Exception exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              ticketService.get(id);
            });

    // Then
    Assertions.assertEquals(expectedMessage, exception.getMessage());
  }

  @Test
  void testGetTicketWithValidIdButNotFound() {
    // given
    // when
    Exception exception =
        assertThrows(
            NotFoundException.class,
            () -> {
              ticketService.get("082722c7-1234-4a39-b8dd-20cb08a6996c");
            });

    // Then
    Assertions.assertEquals(
        "The resource with reference [082722c7-1234-4a39-b8dd-20cb08a6996c] was not found.",
        exception.getMessage());
  }

  @Test
  void testGetTicketNullValue() {
    // given
    TicketDTO ticket = null;
    String expectedMessage = "Parameter must be not null.";
    // when
    Exception exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              ValidationHelper.requireNonNull(ticket);
            });
    // Then
    assertThat(exception).hasMessage(expectedMessage);
  }

  @ParameterizedTest
  @MethodSource("IllegalParameterOderBy")
  void testGetTicketPaginationWithIllegalParameterOderBy(
      String text, int pageSie, String expectedMessage) {

    // given
    // when
    Exception exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              Page<TicketDTO> ticket = ticketService.getAll(0, pageSie, text, "ASC");
            });
    // Then
    assertThat(exception).hasMessage(expectedMessage);
  }

  @Test
  @Transactional
  void testCreateTicket() {

    // given
    UserDTO user = userService.get("9c345098-0d0c-419c-bcdf-05c0810f295f");
    DepartmentDTO department = departmentService.get("51f63edd-4e99-45b9-922e-fc922cf0e05f");
    CategoryDTO category = categoryService.get("468a89e2-acce-40b6-b356-2c134ba48f5e");
    var title = "Test Ticket" + System.currentTimeMillis();

    TicketDTO ticket =
        new TicketDTO(
            title,
            "Description",
            "i5P198vdtGT",
            PriorityDTO.MEDIUM,
            user,
            department,
            category,
            3,
            LocalDateTime.of(2023, 02, 02, 23, 34, 15),
            null,
            StatusDTO.OPEN,
            user);
    // when
    ticketService.create(ticket);
    // Then
    List<Ticket> tickets = ticketRepository.findAll();
    assertThat(title)
        .isEqualTo(
            tickets.stream().filter(t -> t.getTitle().equals(title)).findFirst().get().getTitle());
  }

  @Test
  void testUpdateTicket() {
    // give
    TicketDTO ticket = ticketService.get("082722c7-856f-4a39-b8dd-20cb08a6996c");
    // when
    ticket.setTitle("NewTitle");
    ticketService.update(ticket);
    var tittle = ticketService.get("082722c7-856f-4a39-b8dd-20cb08a6996c").getTitle();
    // Then
    assertThat(tittle).isEqualToIgnoringCase("NewTitle");
  }

  @ParameterizedTest
  @MethodSource("invalidDeleteIds")
  void testDeleteDepartmentWithInvalidId(String id, String expectedMessage) {
    // given
    // When
    Exception exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              departmentService.delete(id);
            });

    // Then
    assertThat(exception).hasMessage(expectedMessage);
  }

  @Test
  @Transactional
  void testDeleteTicket() {
    // given
    var id = "082722c7-856f-4a39-b8dd-20cb08a6996c";
    // When
    ticketService.delete(id);

    Exception exception =
        assertThrows(
            NotFoundException.class,
            () -> {
              var ticket = ticketService.get(id);
            });
    // Then
    assertThat(exception)
        .hasMessage(
            "The resource with reference [082722c7-856f-4a39-b8dd-20cb08a6996c] was not found.");
  }
}
