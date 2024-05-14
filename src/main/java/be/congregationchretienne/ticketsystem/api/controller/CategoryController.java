package be.congregationchretienne.ticketsystem.api.controller;

import be.congregationchretienne.ticketsystem.api.dto.CategoryDTO;
import be.congregationchretienne.ticketsystem.api.dto.UserDTO;
import be.congregationchretienne.ticketsystem.api.exception.NotFoundException;
import be.congregationchretienne.ticketsystem.api.service.CategoryService;
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

@Tag(name = "Category", description = "Category management API")
@RestController
@RequestMapping("/api")
public class CategoryController {

  private static final String CATEGORY_ENDPOINT = "/categories";
  private static final String CATEGORY_ENDPOINT_ID = CATEGORY_ENDPOINT + "/{id}";

  @Autowired private CategoryService categoryService;

  @Operation(
      summary = "Retrieve a User by Id.",
      description =
          "Get a User object by specifying its id. The response is User object with name, email and published status.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        content = {
          @Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json")
        }),
    @ApiResponse(
        responseCode = "404",
        content = {@Content(schema = @Schema())}),
    @ApiResponse(
        responseCode = "500",
        content = {@Content(schema = @Schema())})
  })
  @GetMapping(path = CATEGORY_ENDPOINT_ID)
  public ResponseEntity<Object> getCategory(@PathVariable(value = "id") String id)
      throws NotFoundException {
    return ResponseEntity.status(HttpStatus.OK).body(categoryService.get(id));
  }

  @Operation(
      summary = "Retrieve all categories.",
      description =
          "Get all categories object. The response is a pageable list of Users object with name, email and published status.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        content = {
          @Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json")
        }),
    @ApiResponse(
        responseCode = "500",
        content = {@Content(schema = @Schema())})
  })
  @GetMapping(path = CATEGORY_ENDPOINT)
  public ResponseEntity<Object> getAllCategory(
      @Parameter(description = "Page number, starting from 0")
          @RequestParam(required = false, defaultValue = "0")
          Integer page,
      @Parameter(description = "Number of items per page")
          @RequestParam(required = false, defaultValue = "20")
          Integer pageSize,
      @Parameter(description = "Order by a parameter of the user for example: name")
          @RequestParam(required = false, defaultValue = "")
          String orderBy,
      @Parameter(description = "Sort by a parameter of the user for example: DESC, ASC")
          @RequestParam(required = false, defaultValue = "")
          String sort) {
    var categories = categoryService.getAll(page, pageSize, orderBy, sort);

    return ResponseEntity.status(HttpStatus.OK).body(categories);
  }

  @Operation(
      summary = "Create a category.",
      description = "Create a category object. The response is published status.")
  @ApiResponses({
    @ApiResponse(responseCode = "200"),
    @ApiResponse(
        responseCode = "500",
        content = {@Content(schema = @Schema())})
  })
  @PostMapping(path = CATEGORY_ENDPOINT)
  public ResponseEntity createCategory(@Valid CategoryDTO categoryDTO) {
    categoryService.create(categoryDTO);

    return new ResponseEntity<>("The resource was successfully created.", HttpStatus.CREATED);
  }

  @Operation(
      summary = "Update a category.",
      description =
          "Update a category object by specifying its id. The response is Category object with name, date of creation and published status.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        content = {
          @Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json")
        }),
    @ApiResponse(
        responseCode = "404",
        content = {@Content(schema = @Schema())}),
    @ApiResponse(
        responseCode = "500",
        content = {@Content(schema = @Schema())})
  })
  @PutMapping(path = CATEGORY_ENDPOINT_ID)
  public ResponseEntity updateCategory(@Valid CategoryDTO categoryDTO) {
    categoryService.update(categoryDTO);

    return new ResponseEntity<>("The resource was successfully updated.", HttpStatus.OK);
  }

  @Operation(
      summary = "Delete a category by Id.",
      description =
          "Delete a category object by specifying its id. The response is published status.")
  @ApiResponses({
    @ApiResponse(responseCode = "200"),
    @ApiResponse(
        responseCode = "500",
        content = {@Content(schema = @Schema())})
  })
  @DeleteMapping(path = CATEGORY_ENDPOINT_ID)
  public ResponseEntity deleteCategory(@PathVariable(value = "id") String id) {
    categoryService.delete(id);

    return new ResponseEntity<>("The resource was successfully deleted.", HttpStatus.OK);
  }
}
