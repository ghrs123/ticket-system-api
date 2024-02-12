package be.congregationchretienne.ticketsystem.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import be.congregationchretienne.ticketsystem.api.dto.CategoryDTO;
import be.congregationchretienne.ticketsystem.api.dto.DepartmentDTO;
import be.congregationchretienne.ticketsystem.api.dto.TicketDTO;
import be.congregationchretienne.ticketsystem.api.dto.UserDTO;
import be.congregationchretienne.ticketsystem.api.dto.type.PriorityDTO;
import be.congregationchretienne.ticketsystem.api.dto.type.StatusDTO;
import be.congregationchretienne.ticketsystem.api.service.TicketService;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RunWith(MockitoJUnitRunner.class)
class TicketControllerTest {

  @Mock private TicketService ticketService;

  @InjectMocks private TicketController ticketController;

  TicketDTO expectedTicket = mock(TicketDTO.class);
  List<TicketDTO> tickets = mock(List.class);

  UserDTO assignedTo = mock(UserDTO.class);
  UserDTO createdBy = mock(UserDTO.class);
  DepartmentDTO department = mock(DepartmentDTO.class);
  CategoryDTO category = mock(CategoryDTO.class);

  LocalDateTime startDate = LocalDateTime.now();
  LocalDateTime resolvedOn = LocalDateTime.of(2023, 05, 13, 00, 00, 00);

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    assignedTo = new UserDTO("1675893759024-gustavo@email.com", "User2 test");
    assignedTo.setId("9c345098-0d0c-419c-bcdf-05c0810f295f");
    assignedTo.setCreatedAt(LocalDateTime.now());

    createdBy = new UserDTO("1675893669342-flavios@email.com", "User test");
    createdBy.setId("c97d8449-3600-4834-92ad-3bf77b98d923");
    createdBy.setCreatedAt(LocalDateTime.now());

    department = new DepartmentDTO("Informatique", createdBy, null, null, null);
    department.setId("51f63edd-4e99-45b9-922e-fc922cf0e05f");
    department.setCreatedAt(LocalDateTime.now());

    category = new CategoryDTO("Suporte", null, null, createdBy);
    category.setId("468a89e2-acce-40b6-b356-2c134ba48f5e");
    category.setCreatedAt(LocalDateTime.now());

    expectedTicket =
        new TicketDTO(
            "Ticket1",
            "Description",
            "1236454",
            PriorityDTO.MEDIUM,
            assignedTo,
            department,
            category,
            3,
            startDate,
            resolvedOn,
            StatusDTO.COMPLETE,
            createdBy);

    expectedTicket.setId("082722c7-856f-4a39-b8dd-20cb08a6996c");
    expectedTicket.setCreatedAt(LocalDateTime.now());

    tickets =
        Arrays.asList(
            new TicketDTO(
                "Ticket1",
                "Description",
                "1236454",
                PriorityDTO.MEDIUM,
                assignedTo,
                department,
                category,
                3,
                startDate,
                resolvedOn,
                StatusDTO.COMPLETE,
                createdBy),
            new TicketDTO(
                "Ticket1",
                "Description",
                "1236454",
                PriorityDTO.MEDIUM,
                assignedTo,
                department,
                category,
                3,
                startDate,
                resolvedOn,
                StatusDTO.COMPLETE,
                createdBy));

    tickets.get(0).setId("082722c7-856f-4a39-b8dd-20cb08a6996c");
    tickets.get(1).setId("5365151c-edb9-47b0-954c-412d8f2c7161");
  }

  @Test
  void getTicket() {
    // given
    // when
    when(ticketService.get("082722c7-856f-4a39-b8dd-20cb08a6996c")).thenReturn(expectedTicket);

    ResponseEntity<Object> response =
        ticketController.getTicket("082722c7-856f-4a39-b8dd-20cb08a6996c");
    // then
    verify(ticketService).get("082722c7-856f-4a39-b8dd-20cb08a6996c");
    assertThat(HttpStatus.OK).isEqualTo(response.getStatusCode());
    assertThat(expectedTicket).isEqualTo(response.getBody());
  }

  @Test
  void getAllTickets() {
    // give
    int page = 0;
    int pageSize = 20;
    String orderBy = "";
    String sort = "";

    Page<TicketDTO> expectedPage = new PageImpl<>(tickets);
    // When
    when(ticketService.getAll(page, pageSize, orderBy, sort)).thenReturn(expectedPage);

    ResponseEntity<Object> response = ticketController.getAllTickets(page, pageSize, orderBy, sort);

    // then
    verify(ticketService).getAll(page, pageSize, orderBy, sort);
    assertThat(HttpStatus.OK).isEqualTo(response.getStatusCode());
    assertThat(expectedPage.getContent()).isEqualTo(((PageImpl) response.getBody()).getContent());
  }

  @Test
  void createTicket() {
    // given
    // when
    ResponseEntity response = ticketController.createTicket(expectedTicket);
    // then
    verify(ticketService).create(expectedTicket);
    assertThat(HttpStatus.CREATED).isEqualTo(response.getStatusCode());
    assertThat("The resource was successfully created.")
        .isEqualToIgnoringCase(response.getBody().toString());
  }

  @Test
  void updateTicket() {
    // give
    doNothing().when(ticketService).update(expectedTicket);
    // when
    ResponseEntity response = ticketController.updateTicket(expectedTicket);
    // then
    verify(ticketService).update(expectedTicket);
    assertThat(HttpStatus.OK).isEqualTo(response.getStatusCode());
    assertThat("The resource was successfully updated.")
        .isEqualToIgnoringCase(response.getBody().toString());
  }

  @Test
  void deleteTicket() {
    // given
    String id = "082722c7-856f-4a39-b8dd-20cb08a6996c";
    // when
    doNothing().when(ticketService).delete(id);
    ResponseEntity response = ticketController.deleteTicket(id);
    // then
    verify(ticketService).delete(id);
    assertThat(HttpStatus.OK).isEqualTo(response.getStatusCode());
    assertThat("The resource was successfully deleted.")
        .isEqualToIgnoringCase(response.getBody().toString());
  }
}
