package be.congregationchretienne.ticketsystem.api.service.impl;

import static be.congregationchretienne.ticketsystem.api.mapping.CategoryMapping.INSTANCE_CATEGORY;

import be.congregationchretienne.ticketsystem.api.dto.CategoryDTO;
import be.congregationchretienne.ticketsystem.api.exception.IllegalArgumentException;
import be.congregationchretienne.ticketsystem.api.exception.NotFoundException;
import be.congregationchretienne.ticketsystem.api.exception.PropertyReferenceException;
import be.congregationchretienne.ticketsystem.api.helper.ValidationHelper;
import be.congregationchretienne.ticketsystem.api.mapping.CycleAvoidingMappingContext;
import be.congregationchretienne.ticketsystem.api.model.Category;
import be.congregationchretienne.ticketsystem.api.repository.CategoryRepository;
import be.congregationchretienne.ticketsystem.api.service.CategoryService;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Getter
@Transactional
public class CategoryServiceImpl extends AbstractServiceImpl<CategoryDTO>
    implements CategoryService {

  @Autowired private CategoryRepository repository;

  public CategoryServiceImpl(CategoryRepository repository) {
    super(repository);
  }

  @Override
  @Transactional(readOnly = true)
  public CategoryDTO get(String id) {
    var uuid = checkAndConvertID(id);

    Optional<Category> category =
        Optional.ofNullable(
            repository
                .findById(uuid)
                .orElseThrow(() -> new NotFoundException("Category not founded.")));

    return INSTANCE_CATEGORY.entityToDTO(category.get(), new CycleAvoidingMappingContext());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CategoryDTO> getAll(Integer page, Integer pageSize, String orderBy, String sort) {

    Pageable pageable = null;
    Page<Category> categories = null;

    try {

      pageable = getPageable(page, pageSize, orderBy, sort);
      categories = repository.findAll(pageable);

    } catch (IllegalArgumentException exception) {

      throw new IllegalArgumentException(exception.getMessage());
    } catch (PropertyReferenceException exception) {

      throw new PropertyReferenceException(
          String.format("No property [%s] found for type 'Category'.", orderBy));
    }

    ValidationHelper.requireNonNull(categories);

    return categories.map(
        currentCategory ->
            (INSTANCE_CATEGORY.entityToDTO(currentCategory, new CycleAvoidingMappingContext())));
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void create(CategoryDTO bean) {

    ValidationHelper.requireNonNull(bean);
    var category = INSTANCE_CATEGORY.dtoToEntity(bean, new CycleAvoidingMappingContext());
    category.setCreatedAt(LocalDateTime.now());

    boolean present = repository.findByName(category.getName()).isPresent();

    if (present) {
      throw new IllegalArgumentException(
          String.format("Category with name [%s] already exists.", category.getName()));
    }

    repository.save(category);
  }

  @Override
  public void update(CategoryDTO categoryDTO) {

    checkAndConvertID(categoryDTO.getId());

    repository.save(INSTANCE_CATEGORY.dtoToEntity(categoryDTO, new CycleAvoidingMappingContext()));
  }

  @Override
  public void delete(String id) {

    var uuid = checkAndConvertID(id);

    repository.deleteById(uuid);
  }
}
