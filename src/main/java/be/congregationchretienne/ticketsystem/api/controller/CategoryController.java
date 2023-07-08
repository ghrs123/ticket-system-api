package be.congregationchretienne.ticketsystem.api.controller;

import be.congregationchretienne.ticketsystem.api.dto.CategoryDTO;
import be.congregationchretienne.ticketsystem.api.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private static final String CATEGORY_ENDPOINT = "/categories";
    private static final String CATEGORY_ENDPOINT_ID = CATEGORY_ENDPOINT + "/{id}";

    @Autowired
    private CategoryService categoryService;

    @GetMapping(path = CATEGORY_ENDPOINT_ID)
    public ResponseEntity<Object> getCategory(@PathVariable(value = "id") String id) {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.get(id));
    }

    @GetMapping(path = CATEGORY_ENDPOINT)
    public ResponseEntity<Object> getAllCategory(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int pageSize,
            @RequestParam(required = false, defaultValue = "") String orderBy,
            @RequestParam(required = false, defaultValue = "") String sort) {
        var categories = categoryService.getAll(page, pageSize, orderBy, sort);

        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }

    @PostMapping(path = CATEGORY_ENDPOINT)
    public ResponseEntity createCategory(@RequestBody CategoryDTO categoryDTO) {
        categoryService.create(categoryDTO);

        return new ResponseEntity<>("The resource was successfully created.", HttpStatus.CREATED);
    }

    @PutMapping(path = CATEGORY_ENDPOINT_ID)
    public ResponseEntity updateCategory(CategoryDTO categoryDTO) {
        categoryService.update(categoryDTO);

        return new ResponseEntity<>("The resource was successfully updated.", HttpStatus.OK);
    }

    @DeleteMapping(path = CATEGORY_ENDPOINT_ID)
    public ResponseEntity deleteCategory(@RequestParam String id) {
        categoryService.delete(id);

        return new ResponseEntity<>("The resource was successfully deleted.", HttpStatus.OK);
    }
}
