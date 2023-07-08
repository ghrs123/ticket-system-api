package be.congregationchretienne.ticketsystem.api.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.congregationchretienne.ticketsystem.api.dto.DepartmentDTO;
import be.congregationchretienne.ticketsystem.api.exception.NotFoundException;
import be.congregationchretienne.ticketsystem.api.helper.ValidationHelper;
import be.congregationchretienne.ticketsystem.api.model.Department;
import be.congregationchretienne.ticketsystem.api.repository.DepartmentRepository;
import be.congregationchretienne.ticketsystem.api.service.DepartmentService;
import be.congregationchretienne.ticketsystem.api.service.UserService;
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
@Import({DepartmentServiceImpl.class, UserServiceImpl.class})
@FlywayTestExtension
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DepartmentServiceImplIT {

  @Autowired private DepartmentRepository DepartmentRepository;
  @Autowired private DepartmentService departmentService;
  @Autowired private UserService userService;

  @Autowired private Flyway flyway;

  @BeforeEach
  public void before() throws Exception {
    flyway.clean();
    flyway.migrate();
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

  @Test
  void testGetDepartmentOk() {
    // given
    var uuid = UUID.fromString("51f63edd-4e99-45b9-922e-fc922cf0e05f");

    // when
    var result = departmentService.get(uuid.toString());

    // Then
    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid.toString(), result.getId());
  }

  @ParameterizedTest
  @MethodSource("invalidIds")
  void testGetDepartmentWIthInvalidId(String id, String expectedMessage) {
    // given
    // when
    Exception exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              departmentService.get(id);
            });

    // Then
    Assertions.assertEquals(expectedMessage, exception.getMessage());
  }

  @Test
  void testGetDepartmentWithValidIdButNotFound() {
    // given
    String expectedMessage =
        "The resource with reference [082722c7-1234-4a39-b8dd-20cb08a6996c] was not found.";
    // when
    Exception exception =
        assertThrows(
            NotFoundException.class,
            () -> {
              departmentService.get("082722c7-1234-4a39-b8dd-20cb08a6996c");
            });

    // Then
    Assertions.assertEquals(expectedMessage, exception.getMessage());
  }

  @Test
  void testGetDepartmentNullValue() {
    // given
    DepartmentDTO department = null;
    String expectedMessage = "Parameter must be not null.";
    // when
    Exception exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              ValidationHelper.requireNonNull(department);
            });
    // Then
    assertThat(exception).hasMessage(expectedMessage);
  }

  @ParameterizedTest
  @MethodSource("IllegalParameterOderBy")
  void testGetDepartmentPaginationWithIllegalParameterOderBy(
      String text, int pageSie, String expectedMessage) {

    // given
    // when
    Exception exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              Page<DepartmentDTO> Department = departmentService.getAll(0, pageSie, text, "");
            });
    // Then
    assertThat(exception).hasMessage(expectedMessage);
  }

  @Test
  @Transactional
  void testCreateDepartment() {

    // given
    var user = userService.get("9c345098-0d0c-419c-bcdf-05c0810f295f");
    var title = "Name Department" + System.currentTimeMillis();

    DepartmentDTO department = new DepartmentDTO(title, user, null, null, null);
    // when
    departmentService.create(department);
    // Then
    List<Department> Departments = DepartmentRepository.findAll();
    assertThat(title)
        .isEqualTo(
            Departments.stream()
                .filter(t -> t.getName().equals(title))
                .findFirst()
                .get()
                .getName());
  }

  @Test
  void testUpdateDepartment() {
    // give
    DepartmentDTO Department = departmentService.get("51f63edd-4e99-45b9-922e-fc922cf0e05f");
    // when
    Department.setName("NewName");
    departmentService.update(Department);
    var name = departmentService.get("51f63edd-4e99-45b9-922e-fc922cf0e05f").getName();
    // Then
    assertThat(name).isEqualToIgnoringCase("NewName");
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
  void testDeleteDepartment() {
    // given
    var id = "51f63edd-4e99-45b9-922e-fc922cf0e05f";
    // When
    departmentService.delete(id);

    Exception exception =
        assertThrows(
            NotFoundException.class,
            () -> {
              var Department = departmentService.get(id);
            });
    // Then
    assertThat(exception)
        .hasMessage(
            "The resource with reference [51f63edd-4e99-45b9-922e-fc922cf0e05f] was not found.");
  }
}
