package be.congregationchretienne.ticketsystem.api.controller;

import be.congregationchretienne.ticketsystem.api.dto.CategoryDTO;
import be.congregationchretienne.ticketsystem.api.dto.UserDTO;
import be.congregationchretienne.ticketsystem.api.service.CategoryService;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    CategoryDTO expectedCategory = mock(CategoryDTO.class);

    List<CategoryDTO> categories = mock(List.class);

    UserDTO user = mock(UserDTO.class);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new UserDTO("user1", "user1@mail.com");
        user.setId("9c345098-0d0c-419c-bcdf-05c0810f295f");
        user.setCreatedAt(LocalDateTime.now());

        expectedCategory = new CategoryDTO("Category", null, null, user);
        expectedCategory.setId("468a89e2-acce-40b6-b356-2c134ba48f5e");
        expectedCategory.setCreatedAt(LocalDateTime.now());

        categories = Arrays.asList(
                new CategoryDTO("Category", null, null, user),
                new CategoryDTO("Category", null, null, user)
        );

        categories.get(0).setId("468a89e2-acce-40b6-b356-2c134ba48f5e");
        categories.get(1).setId("67344fa9-9c58-4d4e-a9e6-51599f315655");

    }

    @Test
    void getCategory() {
        //given
        //when
        when(categoryService.get("9c345098-0d0c-419c-bcdf-05c0810f295f")).thenReturn(expectedCategory);

        ResponseEntity<Object> response =
                categoryController.getCategory("9c345098-0d0c-419c-bcdf-05c0810f295f");
        //then
        verify(categoryService).get("9c345098-0d0c-419c-bcdf-05c0810f295f");
        assertThat(HttpStatus.OK).isEqualTo(response.getStatusCode());
        assertThat(expectedCategory).isEqualTo(response.getBody());
    }

    @Test
    void getAllCategory() {
        //give
        int page = 0;
        int pageSize = 20;
        String orderBy = "";
        String sort = "";

        Page<CategoryDTO> expectedPage = new PageImpl<>(categories);
        //When
        when(categoryService.getAll(page, pageSize, orderBy, sort)).thenReturn(expectedPage);

        ResponseEntity<Object> response = categoryController.getAllCategory(page, pageSize, orderBy, sort);

        //then
        verify(categoryService).getAll(page, pageSize, orderBy, sort);
        assertThat(HttpStatus.OK).isEqualTo(response.getStatusCode());
        assertThat(expectedPage.getContent()).isEqualTo(((PageImpl) response.getBody()).getContent());
    }

    @Test
    void createCategory() {
        //given
        //when
        ResponseEntity response = categoryController.createCategory(expectedCategory);
        //then
        verify(categoryService).create(expectedCategory);
        assertThat(HttpStatus.CREATED).isEqualTo(response.getStatusCode());
        assertThat("The resource was successfully created.").isEqualToIgnoringCase(response.getBody().toString());
    }

    @Test
    void updateCategory() {
        //give
        doNothing().when(categoryService).update(expectedCategory);
        //when
        ResponseEntity response = categoryController.updateCategory(expectedCategory);
        //then
        verify(categoryService).update(expectedCategory);
        assertThat(HttpStatus.OK).isEqualTo(response.getStatusCode());
        assertThat("The resource was successfully updated.").isEqualToIgnoringCase(response.getBody().toString());
    }

    @Test
    void deleteCategory() {
        //given
        String id = "9c345098-0d0c-419c-bcdf-05c0810f295f";
        //when
        doNothing().when(categoryService).delete(id);
        ResponseEntity response = categoryController.deleteCategory(id);
        //then
        verify(categoryService).delete(id);
        assertThat(HttpStatus.OK).isEqualTo(response.getStatusCode());
        assertThat("The resource was successfully deleted.").isEqualToIgnoringCase(response.getBody().toString());
    }
}