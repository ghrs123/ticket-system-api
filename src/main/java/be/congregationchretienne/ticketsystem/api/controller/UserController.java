package be.congregationchretienne.ticketsystem.api.controller;

import be.congregationchretienne.ticketsystem.api.dto.UserDTO;
import be.congregationchretienne.ticketsystem.api.exception.NotFoundException;
import be.congregationchretienne.ticketsystem.api.service.UserService;
import javax.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "User management API")
@RestController
@RequestMapping("/api")
public class UserController {

  private static final String USER_ENDPOINT = "/users";
  private static final String USER_ENDPOINT_ID = USER_ENDPOINT + "/{id}";

  @Autowired private UserService userService;

  @Operation(
          summary = "Retrieve a User by Id.",
          description = "Get a User object by specifying its id. The response is User object with id, title, description and published status.",
          tags = { "users", "get" })
  @ApiResponses({
          @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json") }),
          @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema()) }),
          @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
  @GetMapping(path = USER_ENDPOINT_ID)
  public ResponseEntity<Object> getUser(@PathVariable(value = "id") String id)
      throws NotFoundException {

    UserDTO user = userService.get(id);

    return ResponseEntity.status(HttpStatus.OK).body(user);
  }

  @Operation(
          summary = "Retrieve all Users.",
          description = "Get all Users object. The response is a pageable list of Users object and published status.",
          tags = { "users", "getAll" })
  @ApiResponses({
          @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json") }),
          @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema()) }),
          @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
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
  public ResponseEntity createUser(@Valid UserDTO userDTO) {

    userService.create(userDTO);

    return new ResponseEntity<>("The resource was successfully created.", HttpStatus.CREATED);
  }

  @PutMapping(path = USER_ENDPOINT_ID)
  public ResponseEntity updateUser(@Valid UserDTO userDTO) {
    userService.update(userDTO);

    return new ResponseEntity<>("The resource was successfully updated.", HttpStatus.OK);
  }

  @DeleteMapping(path = USER_ENDPOINT_ID)
  public ResponseEntity deleteUser(@PathVariable(value = "id") String id) {
    userService.delete(id);

    return new ResponseEntity<>("The resource was successfully deleted.", HttpStatus.OK);
  }
}
