package be.congregationchretienne.ticketsystem.api.controller;

import be.congregationchretienne.ticketsystem.api.dto.DepartmentDTO;
import be.congregationchretienne.ticketsystem.api.exception.NotFoundException;
import be.congregationchretienne.ticketsystem.api.service.DepartmentService;
import javax.validation.Valid;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Department", description = "Department management API")
@RestController
@RequestMapping("/api")
public class DepartmentController {

  private static final String DEPARTMENT_ENDPOINT = "/departments";
  private static final String DEPARTMENT_ENDPOINT_ID = DEPARTMENT_ENDPOINT + "/{id}";

  @Autowired private DepartmentService departmentService;

  @GetMapping(path = DEPARTMENT_ENDPOINT_ID)
  public ResponseEntity<Object> getDepartment(@PathVariable(value = "id") String id)
      throws NotFoundException {

    var department = departmentService.get(id);

    return ResponseEntity.status(HttpStatus.OK).body(department);
  }

  @GetMapping(path = DEPARTMENT_ENDPOINT)
  public ResponseEntity<Object> getAllDepartments(
      @RequestParam(required = false, defaultValue = "0") int page,
      @RequestParam(required = false, defaultValue = "20") int pageSize,
      @RequestParam(required = false, defaultValue = "") String orderBy,
      @RequestParam(required = false, defaultValue = "") String sort) {

    var departments = departmentService.getAll(page, pageSize, orderBy, sort);

    return ResponseEntity.status(HttpStatus.OK).body(departments);
  }

  @PostMapping(path = DEPARTMENT_ENDPOINT)
  public ResponseEntity createDepartment(@Valid DepartmentDTO departmentDTO) {

    departmentService.create(departmentDTO);

    return new ResponseEntity<>("The resource was successfully created.", HttpStatus.CREATED);
  }

  @PutMapping(path = DEPARTMENT_ENDPOINT_ID)
  public ResponseEntity updateDepartment(@Valid DepartmentDTO departmentDTO) {

    departmentService.update(departmentDTO);

    return new ResponseEntity<>("The resource was successfully updated.", HttpStatus.OK);
  }

  @DeleteMapping(path = DEPARTMENT_ENDPOINT_ID)
  public ResponseEntity deleteDepartment(@PathVariable(value = "id") String id) {

    departmentService.delete(id);

    return new ResponseEntity<>("The resource was successfully deleted.", HttpStatus.OK);
  }
}
