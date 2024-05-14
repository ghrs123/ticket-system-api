package be.congregationchretienne.ticketsystem.api.controller;

import be.congregationchretienne.ticketsystem.api.dto.UserDTO;
import be.congregationchretienne.ticketsystem.api.exception.NotFoundException;
import be.congregationchretienne.ticketsystem.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
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
      description =
          "Get a User object by specifying its id. The response is User object with name, email and published status.")
  @ApiResponses({
    @ApiResponse( responseCode = "200", content = { @Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json")}),
    @ApiResponse( responseCode = "404", content = { @Content(schema = @Schema()) }),
    @ApiResponse( responseCode = "500",content = { @Content(schema = @Schema()) })
  })
  @GetMapping(path = USER_ENDPOINT_ID)
  public ResponseEntity<Object> getUser(@PathVariable(value = "id") String id)
      throws NotFoundException {

    UserDTO user = userService.get(id);

    return ResponseEntity.status(HttpStatus.OK).body(user);
  }

  @Operation(
      summary = "Retrieve all users.",
      description =
          "Get all users object. The response is a pageable list of Users object with name, email and published status.")
  @ApiResponses ({
    @ApiResponse ( responseCode = "200", content = { @Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json") }),
    @ApiResponse ( responseCode = "500",  content = { @Content(schema = @Schema()) })
  })
  @GetMapping(path = USER_ENDPOINT)
  public ResponseEntity<Object> getAllusers(
      @Parameter(description = "Page number, starting from 0") @RequestParam(required = false, defaultValue = "0") int page,
      @Parameter(description = "Number of items per page") @RequestParam(required = false, defaultValue = "20") int pageSize,
      @Parameter(description = "Order by a parameter of the user for example: name, email ") @RequestParam(required = false, defaultValue = "") String orderBy,
      @Parameter(description = "Sort by a parameter of the user for example: DESC, ASC") @RequestParam(required = false, defaultValue = "") String sort) {

    var users = userService.getAll(page, pageSize, orderBy, sort);

    return ResponseEntity.status(HttpStatus.OK).body(users);
  }

  @Operation(
      summary = "Create a user.",
      description = "Create a user object. The response is published status.")
  @ApiResponses({
    @ApiResponse( responseCode = "200"),
    @ApiResponse( responseCode = "400", content = {@Content(schema = @Schema()) }),
    @ApiResponse( responseCode = "500", content = {@Content(schema = @Schema()) })
  })
  @PostMapping(path = USER_ENDPOINT)
  public ResponseEntity createUser(@Valid @RequestBody UserDTO userDTO) {

    userService.create(userDTO);

    return new ResponseEntity<>("The resource was successfully created.", HttpStatus.CREATED);
  }

  @Operation(
      summary = "Update a user.",
      description = "Update a user object by specifying its id. The response is User object with name, email and published status.")
  @ApiResponses({
    @ApiResponse( responseCode = "200"),
    @ApiResponse( responseCode = "404", content = {@Content(schema = @Schema()) }),
    @ApiResponse( responseCode = "500", content = {@Content(schema = @Schema()) })
  })
  @PutMapping(path = USER_ENDPOINT)
  public ResponseEntity updateUser(@Valid @RequestBody UserDTO userDTO) {
    userService.update(userDTO);

    return new ResponseEntity<>("The resource was successfully updated.", HttpStatus.OK);
  }

  @Operation(
      summary = "Delete a user by Id.",
      description = "Delete a user object by specifying its id. The response is published status.")
  @ApiResponses({
    @ApiResponse(responseCode = "200"),
    @ApiResponse( responseCode = "404", content = {@Content(schema = @Schema()) }),
    @ApiResponse( responseCode = "500", content = {@Content(schema = @Schema())}) })
  @DeleteMapping(path = USER_ENDPOINT_ID)
  public ResponseEntity deleteUser(@PathVariable(value = "id") String id) {
    userService.delete(id);

    return new ResponseEntity<>("The resource was successfully deleted.", HttpStatus.OK);
  }
}
