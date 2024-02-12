package be.congregationchretienne.ticketsystem.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import be.congregationchretienne.ticketsystem.api.dto.DepartmentDTO;
import be.congregationchretienne.ticketsystem.api.dto.UserDTO;
import be.congregationchretienne.ticketsystem.api.service.DepartmentService;
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
class DepartmentControllerTest {

  @Mock private DepartmentService departmentService;

  @InjectMocks private DepartmentController departmentController;

  DepartmentDTO expectedDepartment = mock(DepartmentDTO.class);
  List<DepartmentDTO> departmentsList = mock(List.class);

  UserDTO user = mock(UserDTO.class);

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    user = new UserDTO("user1", "user1@mail.com");
    user.setId("9c345098-0d0c-419c-bcdf-05c0810f295f");
    user.setCreatedAt(LocalDateTime.now());

    expectedDepartment = new DepartmentDTO("deparment", user, null, null, null);
    expectedDepartment.setId("51f63edd-4e99-45b9-922e-fc922cf0e05f");
    expectedDepartment.setCreatedAt(LocalDateTime.now());

    departmentsList =
        Arrays.asList(
            new DepartmentDTO("deparment1", user, null, null, null),
            new DepartmentDTO("deparment2", user, null, null, null));

    departmentsList.get(0).setId("51f63edd-4e99-45b9-922e-fc922cf0e05f");
    departmentsList.get(1).setId("b7f6aeae-8ad0-49c1-9819-12d35dcdc2ab");
  }

  @Test
  void getDepartment() {

    // given
    // when
    when(departmentService.get("51f63edd-4e99-45b9-922e-fc922cf0e05f"))
        .thenReturn(expectedDepartment);

    ResponseEntity<Object> response =
        departmentController.getDepartment("51f63edd-4e99-45b9-922e-fc922cf0e05f");
    // then
    verify(departmentService).get("51f63edd-4e99-45b9-922e-fc922cf0e05f");
    assertThat(HttpStatus.OK).isEqualTo(response.getStatusCode());
    assertThat(expectedDepartment).isEqualTo(response.getBody());
  }

  @Test
  void getAllDepartments() {
    // give
    int page = 0;
    int pageSize = 20;
    String orderBy = "";
    String sort = "";

    Page<DepartmentDTO> expectedPage = new PageImpl<>(departmentsList);
    // When
    when(departmentService.getAll(page, pageSize, orderBy, sort)).thenReturn(expectedPage);

    ResponseEntity<Object> response =
        departmentController.getAllDepartments(page, pageSize, orderBy, sort);

    // then
    verify(departmentService).getAll(page, pageSize, orderBy, sort);
    assertThat(HttpStatus.OK).isEqualTo(response.getStatusCode());
    assertThat(expectedPage.getContent()).isEqualTo(((PageImpl) response.getBody()).getContent());
  }

  @Test
  void createDepartment() {
    // given
    // when
    ResponseEntity response = departmentController.createDepartment(expectedDepartment);
    // then
    verify(departmentService).create(expectedDepartment);
    assertThat(HttpStatus.CREATED).isEqualTo(response.getStatusCode());
    assertThat("The resource was successfully created.")
        .isEqualToIgnoringCase(response.getBody().toString());
  }

  @Test
  void updateDepartment() {
    // give
    doNothing().when(departmentService).update(expectedDepartment);
    // when
    ResponseEntity response = departmentController.updateDepartment(expectedDepartment);
    // then
    verify(departmentService).update(expectedDepartment);
    assertThat(HttpStatus.OK).isEqualTo(response.getStatusCode());
    assertThat("The resource was successfully updated.")
        .isEqualToIgnoringCase(response.getBody().toString());
  }

  @Test
  void deleteDepartment() {
    // given
    String id = "9c345098-0d0c-419c-bcdf-05c0810f295f";
    // when
    doNothing().when(departmentService).delete(id);
    ResponseEntity response = departmentController.deleteDepartment(id);
    // then
    verify(departmentService).delete(id);
    assertThat(HttpStatus.OK).isEqualTo(response.getStatusCode());
    assertThat("The resource was successfully deleted.")
        .isEqualToIgnoringCase(response.getBody().toString());
  }
}
