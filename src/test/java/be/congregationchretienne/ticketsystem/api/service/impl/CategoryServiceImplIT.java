package be.congregationchretienne.ticketsystem.api.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.congregationchretienne.ticketsystem.api.dto.CategoryDTO;
import be.congregationchretienne.ticketsystem.api.dto.UserDTO;
import be.congregationchretienne.ticketsystem.api.exception.IllegalArgumentException;
import be.congregationchretienne.ticketsystem.api.exception.NotFoundException;
import be.congregationchretienne.ticketsystem.api.helper.ValidationHelper;
import be.congregationchretienne.ticketsystem.api.model.Category;
import be.congregationchretienne.ticketsystem.api.repository.CategoryRepository;
import be.congregationchretienne.ticketsystem.api.service.CategoryService;
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
            "Namee", 50, "No property 'namee' found for type 'Category' Did you mean ''name''"));
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
  void testGetCategoryWIthInvalidId(String id, String expectedMessage) {
    // given
    // when
    RuntimeException exception =
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
    RuntimeException exception =
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
    RuntimeException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              ValidationHelper.requireNonNull(Category);
            });
    // Then
    assertThat(exception).hasMessage(expectedMessage);
  }

  @ParameterizedTest
  @MethodSource("IllegalParameterPageSize")
  void testGetCategoryPaginationWithIllegalParameterPageSize(
      String orderBy, int pageSize, String expectedMessage) {
    // given
    // when
    RuntimeException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              Page<CategoryDTO> category = categoryService.getAll(0, pageSize, orderBy, "ASC");
            });
    // Then
    assertThat(exception).hasMessage(expectedMessage);
  }

  @ParameterizedTest
  @MethodSource("IllegalParameterOderBy")
  void testGetCategoryPaginationWithIllegalParameterOderBy(
      String text, int pageSize, String expectedMessage) {

    // given
    // when
    RuntimeException exception =
        assertThrows(
            PropertyReferenceException.class,
            () -> {
              categoryService.getAll(0, pageSize, text, "ASC");
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
              categoryService.getAll(0, pageSize, orderBy, "ASC");
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
  void testDeleteCategoryValidId(String id, String expectedMessage) {
    // given
    // When
    RuntimeException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              categoryService.delete(id);
            });

    // Then
    assertThat(exception).hasMessage(expectedMessage);
  }

  @Test
  void testDeleteCategoryForValidNotFoundId() {
    // given
    var id = "468a89e2-acce-40b6-b356-2c134ba48f5e";
    String expectedMessage =
        "The resource with reference [468a89e2-acce-40b6-b356-2c134ba48f5e] was not found.";
    // When
    categoryService.delete(id);

    RuntimeException exception =
        assertThrows(
            NotFoundException.class,
            () -> {
              categoryService.get(id);
            });
    // Then
    assertThat(exception).hasMessage(expectedMessage);
  }
}
