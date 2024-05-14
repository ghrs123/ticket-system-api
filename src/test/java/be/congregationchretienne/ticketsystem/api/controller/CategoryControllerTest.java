package be.congregationchretienne.ticketsystem.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import be.congregationchretienne.ticketsystem.api.dto.CategoryDTO;
import be.congregationchretienne.ticketsystem.api.dto.UserDTO;
import be.congregationchretienne.ticketsystem.api.service.CategoryService;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

  private static final String uri = "/api/categories";
  @Mock private CategoryService categoryService;

  @Autowired private MockMvc mvc;

  @InjectMocks private CategoryController categoryController;

  CategoryDTO expectedCategory = mock(CategoryDTO.class);

  List<CategoryDTO> categories = mock(List.class);

  UserDTO user = mock(UserDTO.class);

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    mvc = MockMvcBuilders.standaloneSetup(categoryController).build();

    user = new UserDTO("user1", "user1@mail.com");
    user.setId("9c345098-0d0c-419c-bcdf-05c0810f295f");
    user.setCreatedAt(LocalDateTime.now());

    expectedCategory = new CategoryDTO("Category", null, null, user);
    expectedCategory.setId("468a89e2-acce-40b6-b356-2c134ba48f5e");
    expectedCategory.setCreatedAt(LocalDateTime.now());

    categories =
        Arrays.asList(
            new CategoryDTO("Category1", null, null, user),
            new CategoryDTO("Category2", null, null, user));

    categories.get(0).setId("468a89e2-acce-40b6-b356-2c134ba48f5e");
    categories.get(1).setId("67344fa9-9c58-4d4e-a9e6-51599f315655");
  }

  @Test
  void getCategory() throws Exception {
    // given
    String categoryId = "468a89e2-acce-40b6-b356-2c134ba48f5e";

    // when
    when(categoryService.get(categoryId)).thenReturn(expectedCategory);

    mvc.perform(get(uri + "/{id}", categoryId).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(categoryId))
        .andExpect(jsonPath("$.name").value("Category"));
    // then
    verify(categoryService, times(1)).get(categoryId);
  }

  @Test
  void getAllCategoryInAscOrder() throws Exception {
    // give
    int page = 0;
    int pageSize = 20;
    String orderBy = "";
    String sort = "ASC";

    Page<CategoryDTO> expectedPage = new PageImpl<>(categories);
    // When
    when(categoryService.getAll(eq(page), eq(pageSize), eq(orderBy), eq(sort)))
        .thenReturn(expectedPage);

    mvc.perform(
            get(uri)
                .param("page", Integer.toString(page))
                .param("pageSize", Integer.toString(pageSize))
                .param("orderBy", orderBy)
                .param("sort", sort)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].id").value("468a89e2-acce-40b6-b356-2c134ba48f5e"))
        .andExpect(jsonPath("$.content[0].name").value("Category1"))
        .andExpect(jsonPath("$.content[1].id").value("67344fa9-9c58-4d4e-a9e6-51599f315655"))
        .andExpect(jsonPath("$.content[1].name").value("Category2"))
        .andDo(print());

    // then
    verify(categoryService).getAll(eq(page), eq(pageSize), eq(orderBy), eq(sort));
  }

  @Test
  void getAllCategoryInDescOrder() throws Exception {
    // give
    int page = 0;
    int pageSize = 20;
    String orderBy = "";
    String sort = "DESC";

    Page<CategoryDTO> expectedPage = new PageImpl<>(categories);
    // When
    when(categoryService.getAll(eq(page), eq(pageSize), eq(orderBy), eq(sort)))
        .thenReturn(expectedPage);

    mvc.perform(
            get(uri)
                .param("page", Integer.toString(page))
                .param("pageSize", Integer.toString(pageSize))
                .param("orderBy", orderBy)
                .param("sort", sort)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].id").value("468a89e2-acce-40b6-b356-2c134ba48f5e"))
        .andExpect(jsonPath("$.content[0].name").value("Category1"))
        .andExpect(jsonPath("$.content[1].id").value("67344fa9-9c58-4d4e-a9e6-51599f315655"))
        .andExpect(jsonPath("$.content[1].name").value("Category2"));

    // then
    verify(categoryService).getAll(eq(page), eq(pageSize), eq(orderBy), eq(sort));
  }

  @Test
  void createCategory() {
    // given
    // when
    ResponseEntity response = categoryController.createCategory(expectedCategory);
    // then
    verify(categoryService).create(expectedCategory);
    assertThat(HttpStatus.CREATED).isEqualTo(response.getStatusCode());
    assertThat("The resource was successfully created.")
        .isEqualToIgnoringCase(response.getBody().toString());
  }

  @Test
  void updateCategory() {
    // give
    doNothing().when(categoryService).update(expectedCategory);
    // when
    ResponseEntity response = categoryController.updateCategory(expectedCategory);
    // then
    verify(categoryService).update(expectedCategory);
    assertThat(HttpStatus.OK).isEqualTo(response.getStatusCode());
    assertThat("The resource was successfully updated.")
        .isEqualToIgnoringCase(response.getBody().toString());
  }

  @Test
  void deleteCategory() {
    // given
    String id = "468a89e2-acce-40b6-b356-2c134ba48f5e";
    // when
    doNothing().when(categoryService).delete(id);
    ResponseEntity response = categoryController.deleteCategory(id);
    // then
    verify(categoryService).delete(id);
    assertThat(HttpStatus.OK).isEqualTo(response.getStatusCode());
    assertThat("The resource was successfully deleted.")
        .isEqualToIgnoringCase(response.getBody().toString());
  }
}
