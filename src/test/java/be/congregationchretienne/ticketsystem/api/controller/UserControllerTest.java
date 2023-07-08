package be.congregationchretienne.ticketsystem.api.controller;

import be.congregationchretienne.ticketsystem.api.dto.UserDTO;
import be.congregationchretienne.ticketsystem.api.service.UserService;
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
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    UserDTO expectedUser = mock(UserDTO.class);
    List<UserDTO> expectedUsers = mock(List.class);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        expectedUser = new UserDTO("user1", "user1@mail.com");
        expectedUser.setId("9c345098-0d0c-419c-bcdf-05c0810f295f");
        expectedUser.setCreatedAt(LocalDateTime.now());

        expectedUsers =
                Arrays.asList(
                        new UserDTO("John", "john@example.com"), new UserDTO("Mary", "mary@example.com"));

        expectedUsers.get(0).setId("9c345098-0d0c-419c-bcdf-05c0810f655b");
        expectedUsers.get(1).setId("9c345098-0d0c-419c-bcdf-05c0810f895c");
    }

    @Test
    void getUser() {
        //given
        //when
        when(userService.get("9c345098-0d0c-419c-bcdf-05c0810f295f")).thenReturn(expectedUser);

        ResponseEntity<Object> response =
                userController.getUser("9c345098-0d0c-419c-bcdf-05c0810f295f");
        //then
        verify(userService).get("9c345098-0d0c-419c-bcdf-05c0810f295f");
        assertThat(HttpStatus.OK).isEqualTo(response.getStatusCode());
        assertThat(expectedUser).isEqualTo(response.getBody());
    }

    @Test
    void getAllusers() {

        //give
        int page = 0;
        int pageSize = 20;
        String orderBy = "";
        String sort = "";

        Page<UserDTO> expectedPage = new PageImpl<>(expectedUsers);
        //When
        when(userService.getAll(page, pageSize, orderBy, sort)).thenReturn(expectedPage);

        ResponseEntity<Object> response = userController.getAllusers(page, pageSize, orderBy, sort);

        //then
        verify(userService).getAll(page, pageSize, orderBy, sort);
        assertThat(HttpStatus.OK).isEqualTo(response.getStatusCode());
        assertThat(expectedPage.getContent()).isEqualTo(((PageImpl) response.getBody()).getContent());
    }

    @Test
    void createUser() {
        //given
        //when
        ResponseEntity response = userController.createUser(expectedUser);
        //then
        verify(userService).create(expectedUser);
        assertThat(HttpStatus.CREATED).isEqualTo(response.getStatusCode());
        assertThat("The resource was successfully created.").isEqualToIgnoringCase(response.getBody().toString());
    }

    @Test
    void updateTicket() {
        //give
        doNothing().when(userService).update(expectedUser);
        //when
        ResponseEntity response = userController.updateUser(expectedUser);
        //then
        verify(userService).update(expectedUser);
        assertThat(HttpStatus.OK).isEqualTo(response.getStatusCode());
        assertThat("The resource was successfully updated.").isEqualToIgnoringCase(response.getBody().toString());
    }

    @Test
    void deleteUser() {
        //given
        String id = "9c345098-0d0c-419c-bcdf-05c0810f295f";
        //when
        doNothing().when(userService).delete(id);
        ResponseEntity response = userController.deleteUser(id);
        //then
        verify(userService).delete(id);
        assertThat(HttpStatus.OK).isEqualTo(response.getStatusCode());
        assertThat("The resource was successfully deleted.").isEqualToIgnoringCase(response.getBody().toString());
    }
}
