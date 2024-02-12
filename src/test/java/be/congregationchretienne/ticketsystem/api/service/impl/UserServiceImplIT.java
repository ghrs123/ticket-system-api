package be.congregationchretienne.ticketsystem.api.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.congregationchretienne.ticketsystem.api.dto.UserDTO;
import be.congregationchretienne.ticketsystem.api.exception.IllegalArgumentException;
import be.congregationchretienne.ticketsystem.api.exception.NotFoundException;
import be.congregationchretienne.ticketsystem.api.helper.ValidationHelper;
import be.congregationchretienne.ticketsystem.api.model.User;
import be.congregationchretienne.ticketsystem.api.repository.UserRepository;
import be.congregationchretienne.ticketsystem.api.service.UserService;
import java.util.List;
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
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@FlywayTest
@Import({UserServiceImpl.class})
@FlywayTestExtension
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserServiceImplIT {

  @Autowired private UserRepository userRepository;
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
        Arguments.of(nullVar, "The resource reference [null] must be not null."),
        Arguments.of("", "The resource reference [] must be not null."),
        Arguments.of("  ", "The resource reference [  ] must be not null."),
        Arguments.of("123", "The resource reference [123] is invalid, must be the UUID format."));
  }

  private static Stream<Arguments> IllegalParameterOderBy() {
    String nullVar = null;
    return Stream.of(
        Arguments.of(
            "Namee", 50, "No property 'namee' found for type 'User' Did you mean ''name''"));
  }

  private static Stream<Arguments> IllegalParameterPageSize() {
    return Stream.of(
        Arguments.of("ASC", 0, "The number of items per page should be between 1 and 50."),
        Arguments.of("ASC", 51, "The number of items per page should be between 1 and 50."));
  }

  private static Stream<Arguments> invalidDeleteIds() {
    String nullVar = null;
    return Stream.of(
        Arguments.of(nullVar, "The resource reference [null] must be not null."),
        Arguments.of("1212", "The resource reference [1212] is invalid, must be the UUID format."));
  }

  @ParameterizedTest
  @MethodSource("invalidIds")
  void testGetUserWIthInvalidId(String id, String expectedMessage) {
    // given
    // when
    Exception exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              userService.get(id);
            });

    // Then
    Assertions.assertEquals(expectedMessage, exception.getMessage());
  }

  @Test
  void testGetUserWithValidIdButNotFound() {
    // given
    String expectedMessage =
        "The resource with reference [082722c7-1234-4a39-b8dd-20cb08a6996c] was not found.";
    // when
    Exception exception =
        assertThrows(
            NotFoundException.class,
            () -> {
              userService.get("082722c7-1234-4a39-b8dd-20cb08a6996c");
            });

    // Then
    Assertions.assertEquals(expectedMessage, exception.getMessage());
  }

  @Test
  void testGetUserNullValue() {
    // given
    UserDTO user = null;
    String expectedMessage = "Parameter must be not null.";
    // when
    Exception exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              ValidationHelper.requireNonNull(user);
            });
    // Then
    assertThat(exception).hasMessage(expectedMessage);
  }

  @ParameterizedTest
  @MethodSource("IllegalParameterOderBy")
  void testGetUserPaginationWithIllegalParameterOderBy(
      String text, int pageSie, String expectedMessage) {
    // given
    // when
    Exception exception =
        assertThrows(
            PropertyReferenceException.class,
            () -> {
              Page<UserDTO> user = userService.getAll(0, pageSie, text, "ASC");
            });
    // Then
    assertThat(exception).hasMessage(expectedMessage);
  }

  @Test
  void testGetCategoryPaginationWithOderByNull() {

    // given
    String orderBy = null;
    int pageSize = 0;
    String expectedMessage = "The number of items per page should be between 1 and 50.";
    // when
    RuntimeException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              userService.getAll(0, pageSize, orderBy, "ASC");
            });
    // Then
    assertThat(exception).hasMessage(expectedMessage);
  }

  @Test
  @Transactional
  void testCreateUser() {

    // given
    var title = "Name User" + System.currentTimeMillis();
    UserDTO user = new UserDTO(title, "user@mail.com");
    // when
    userService.create(user);
    // Then
    List<User> users = userRepository.findAll();
    assertThat(title)
        .isEqualTo(
            users.stream().filter(t -> t.getName().equals(title)).findFirst().get().getName());
  }

  @Test
  void testUpdateUser() {
    // give
    UserDTO user = userService.get("9c345098-0d0c-419c-bcdf-05c0810f295f");
    // when
    user.setName("NewName");
    userService.update(user);
    var name = userService.get("9c345098-0d0c-419c-bcdf-05c0810f295f").getName();
    // Then
    assertThat(name).isEqualToIgnoringCase("NewName");
  }

  @ParameterizedTest
  @MethodSource("invalidDeleteIds")
  void testDeleteCategoryValidId(String id, String expectedMessage) {
    // given
    // When
    RuntimeException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              userService.delete(id);
            });

    // Then
    assertThat(exception).hasMessage(expectedMessage);
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
              userService.delete(id);
            });

    // Then
    assertThat(exception).hasMessage(expectedMessage);
  }

  @Test
  @Transactional
  void testDeleteUserForValidNotFoundId() {
    // given
    var id = "9c345098-0d0c-419c-bcdf-05c0810f295f";
    String expectedMessage =
        "The resource with reference [9c345098-0d0c-419c-bcdf-05c0810f295f] was not found.";
    // When
    userService.delete(id);

    Exception exception =
        assertThrows(
            NotFoundException.class,
            () -> {
              var user = userService.get(id);
            });
    // Then
    assertThat(exception).hasMessage(expectedMessage);
  }
}
