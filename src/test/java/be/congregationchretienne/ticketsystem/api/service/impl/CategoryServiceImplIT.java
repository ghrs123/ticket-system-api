package be.congregationchretienne.ticketsystem.api.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.congregationchretienne.ticketsystem.api.dto.CategoryDTO;
import be.congregationchretienne.ticketsystem.api.dto.UserDTO;
import be.congregationchretienne.ticketsystem.api.exception.NotFoundException;
import be.congregationchretienne.ticketsystem.api.helper.ValidationHelper;
import be.congregationchretienne.ticketsystem.api.model.Category;
import be.congregationchretienne.ticketsystem.api.repository.CategoryRepository;
import be.congregationchretienne.ticketsystem.api.service.CategoryService;
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
@Import({UserServiceImpl.class, CategoryServiceImpl.class})
@FlywayTestExtension
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryServiceImplIT {

  @Autowired private CategoryService categoryService;
  @Autowired private CategoryRepository CategoryRepository;
  @Autowired private UserService userService;

  @Autowired private Flyway flyway;

  @BeforeEach
  public void before() throws Exception {
    flyway.clean();
    flyway.migrate();
  }

  @Test
  void testGetCategoryOk() {
    // given
    var uuid = UUID.fromString("468a89e2-acce-40b6-b356-2c134ba48f5e");

    // when
    var result = categoryService.get(uuid.toString());

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
  void testGetCategoryWIthInvalidId(String id, String expectedMessage) {
    // given
    // when
    Exception exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              categoryService.get(id);
            });

    // Then
    Assertions.assertEquals(expectedMessage, exception.getMessage());
  }

  @Test
  void testGetCategoryWithValidIdButNotFound() {
    // given
    // when
    Exception exception =
        assertThrows(
            NotFoundException.class,
            () -> {
              categoryService.get("082722c7-1234-4a39-b8dd-20cb08a6996c");
            });

    // Then
    Assertions.assertEquals(
        "The resource with reference [082722c7-1234-4a39-b8dd-20cb08a6996c] was not found.",
        exception.getMessage());
  }

  @Test
  void testGetCategoryNullValue() {
    // given
    CategoryDTO Category = null;
    String expectedMessage = "Parameter must be not null.";
    // when
    Exception exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              ValidationHelper.requireNonNull(Category);
            });
    // Then
    assertThat(exception).hasMessage(expectedMessage);
  }

  @ParameterizedTest
  @MethodSource("IllegalParameterOderBy")
  void testGetCategoryPaginationWithIllegalParameterOderBy(
      String text, int pageSie, String expectedMessage) {

    // given
    // when
    Exception exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              Page<CategoryDTO> category = categoryService.getAll(0, pageSie, text, "ASC");
            });
    // Then
    assertThat(exception).hasMessage(expectedMessage);
  }

  @Test
  @Transactional
  void testCreateCategory() {

    // given
    UserDTO user = userService.get("9c345098-0d0c-419c-bcdf-05c0810f295f");
    var title = "Test Category" + System.currentTimeMillis();
    CategoryDTO category = new CategoryDTO(title, null, null, user);
    // when
    categoryService.create(category);
    // Then
    List<Category> categories = CategoryRepository.findAll();
    assertThat(title)
        .isEqualTo(
            categories.stream().filter(t -> t.getName().equals(title)).findFirst().get().getName());
  }

  @Test
  void testUpdateCategory() {
    // give
    CategoryDTO category = categoryService.get("468a89e2-acce-40b6-b356-2c134ba48f5e");
    // when
    category.setName("NewTitle");
    categoryService.update(category);
    var tittle = categoryService.get("468a89e2-acce-40b6-b356-2c134ba48f5e").getName();
    // Then
    assertThat(tittle).isEqualToIgnoringCase("NewTitle");
  }

  @ParameterizedTest
  @MethodSource("invalidDeleteIds")
  void testDeleteCategoryvalidId(String id, String expectedMessage) {
    // given
    // When
    Exception exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              categoryService.delete(id);
            });

    // Then
    assertThat(exception).hasMessage(expectedMessage);
  }

  @Test
  @Transactional
  void testDeleteCategory() {
    // given
    var id = "468a89e2-acce-40b6-b356-2c134ba48f5e";
    String expectedMessage =
        "The resource with reference [468a89e2-acce-40b6-b356-2c134ba48f5e] was not found.";
    // When
    categoryService.delete(id);

    Exception exception =
        assertThrows(
            NotFoundException.class,
            () -> {
              var Category = categoryService.get(id);
            });
    // Then
    assertThat(exception).hasMessage(expectedMessage);
  }
}
