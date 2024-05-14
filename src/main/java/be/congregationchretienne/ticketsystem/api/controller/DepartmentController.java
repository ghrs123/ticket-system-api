package be.congregationchretienne.ticketsystem.api.controller;

import be.congregationchretienne.ticketsystem.api.dto.DepartmentDTO;
import be.congregationchretienne.ticketsystem.api.dto.UserDTO;
import be.congregationchretienne.ticketsystem.api.exception.NotFoundException;
import be.congregationchretienne.ticketsystem.api.service.DepartmentService;
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

@CrossOrigin(origins = "*")
@Tag(name = "Department", description = "Department management API")
@RestController
@RequestMapping("/api")
public class DepartmentController {

  private static final String DEPARTMENT_ENDPOINT = "/departments";
  private static final String DEPARTMENT_ENDPOINT_ID = DEPARTMENT_ENDPOINT + "/{id}";

  @Autowired private DepartmentService departmentService;

  @Operation(
          summary = "Retrieve a department by Id.",
          description = "Get a department object by specifying its id. The response is User object and published status.")
  @ApiResponses({
    @ApiResponse ( responseCode = "200", content = {@Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json") }),
    @ApiResponse (responseCode = "404",content = {@Content(schema = @Schema())}),
    @ApiResponse ( responseCode = "500",content = {@Content(schema = @Schema())})
  })
  @GetMapping(path = DEPARTMENT_ENDPOINT_ID)
  public ResponseEntity<Object> getDepartment(@PathVariable(value = "id") String id)
      throws NotFoundException {

    var department = departmentService.get(id);

    return ResponseEntity.status(HttpStatus.OK).body(department);
  }

  @Operation(
      summary = "Retrieve all departments.",
      description =
          "Get all departments object. The response is a pageable list of Users object and published status.")
  @ApiResponses ({
    @ApiResponse ( responseCode = "200", content = { @Content(schema = @Schema(implementation = DepartmentDTO.class), mediaType = "application/json")}),
    @ApiResponse ( responseCode = "500",content = {@Content(schema = @Schema())})
  })
  @GetMapping(path = DEPARTMENT_ENDPOINT)
  public ResponseEntity<Object> getAllDepartments(
      @Parameter(description = "Page number, starting from 0") @RequestParam(required = false, defaultValue = "0") int page,
      @Parameter(description = "Number of items per page")  @RequestParam(required = false, defaultValue = "20")  int pageSize,
      @Parameter(description = "Order by a parameter of the user for example: name") @RequestParam(required = false, defaultValue = "") String orderBy,
      @Parameter(description = "Sort by a parameter of the user for example: DESC, ASC") @RequestParam(required = false, defaultValue = "") String sort) {

    var departments = departmentService.getAll(page, pageSize, orderBy, sort);

    return ResponseEntity.status(HttpStatus.OK).body(departments);
  }

  @Operation(
      summary = "Create a department.",
      description = "Create a department object. The response is published status.")
  @ApiResponses({
    @ApiResponse(responseCode = "200"),
    @ApiResponse(responseCode = "500",  content = {@Content(schema = @Schema())})
  })
  @PostMapping(path = DEPARTMENT_ENDPOINT)
  public ResponseEntity createDepartment(@Valid @RequestBody DepartmentDTO departmentDTO) {

    departmentService.create(departmentDTO);

    return new ResponseEntity<>("The resource was successfully created.", HttpStatus.CREATED);
  }

  @Operation(
      summary = "Update a department.",
      description = "Update a department object by specifying its id. The response is Department object and published status.")
  @ApiResponses({
    @ApiResponse( responseCode = "200"),
    @ApiResponse( responseCode = "404", content = {@Content(schema = @Schema()) }),
    @ApiResponse( responseCode = "500", content = {@Content(schema = @Schema()) })
  })
  @PutMapping(path = DEPARTMENT_ENDPOINT)
  public ResponseEntity updateDepartment(@Valid @RequestBody DepartmentDTO departmentDTO) {

    departmentService.update(departmentDTO);

    return new ResponseEntity<>("The resource was successfully updated.", HttpStatus.OK);
  }

  @Operation(
      summary = "Delete a department by Id.",
      description = "Delete a department object by specifying its id. The response is published status.")
  @ApiResponses({
    @ApiResponse(responseCode = "200"),
    @ApiResponse( responseCode = "500", content = {@Content(schema = @Schema())})
  })
  @DeleteMapping(path = DEPARTMENT_ENDPOINT_ID)
  public ResponseEntity deleteDepartment(@PathVariable(value = "id") String id) {

    departmentService.delete(id);

    return new ResponseEntity<>("The resource was successfully deleted.", HttpStatus.OK);
  }
}
