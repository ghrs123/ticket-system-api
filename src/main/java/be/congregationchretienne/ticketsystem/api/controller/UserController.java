package be.congregationchretienne.ticketsystem.api.controller;

import be.congregationchretienne.ticketsystem.api.dto.UserDTO;
import be.congregationchretienne.ticketsystem.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class UserController {

  private static final String USER_ENDPOINT = "/users";
  private static final String USER_ENDPOINT_ID = USER_ENDPOINT + "/{id}";

  @Autowired private UserService userService;

  @GetMapping(path = USER_ENDPOINT_ID)
  public ResponseEntity<Object> getUser(@PathVariable(value = "id") String id) {

    UserDTO user = userService.get(id);

    return ResponseEntity.status(HttpStatus.OK).body(user);
  }

  @GetMapping(path = USER_ENDPOINT)
  public ResponseEntity<Object> getAllusers(
      @RequestParam(required = false, defaultValue = "0") int page,
      @RequestParam(required = false, defaultValue = "20") int pageSize,
      @RequestParam(required = false, defaultValue = "") String orderBy,
      @RequestParam(required = false, defaultValue = "") String sort) {

    var users = userService.getAll(page, pageSize, orderBy, sort);

    return ResponseEntity.status(HttpStatus.OK).body(users);
  }

  @PostMapping(path = USER_ENDPOINT)
  public ResponseEntity createUser(@RequestBody UserDTO userDTO) {

    userService.create(userDTO);

    return new ResponseEntity<>("The resource was successfully created.", HttpStatus.CREATED);
  }

  @PutMapping(path = USER_ENDPOINT_ID)
  public ResponseEntity updateUser(UserDTO userDTO) {
    userService.update(userDTO);

    return new ResponseEntity<>("The resource was successfully updated.", HttpStatus.OK);
  }

  @DeleteMapping(path = USER_ENDPOINT_ID)
  public ResponseEntity deleteUser(@RequestParam String id) {
    userService.delete(id);

    return new ResponseEntity<>("The resource was successfully deleted.", HttpStatus.OK);
  }
}
