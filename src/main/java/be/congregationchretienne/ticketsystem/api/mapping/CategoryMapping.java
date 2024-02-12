package be.congregationchretienne.ticketsystem.api.mapping;

import be.congregationchretienne.ticketsystem.api.dto.CategoryDTO;
import be.congregationchretienne.ticketsystem.api.model.Category;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapping {

  CategoryMapping INSTANCE_CATEGORY = Mappers.getMapper(CategoryMapping.class);

  @Mapping(target = "createdBy", source = "createdBy")
  CategoryDTO entityToDTO(Category category, @Context CycleAvoidingMappingContext context);

  // Set<CategoryDTO> entityToDTO(Set<Category> categories);
  @Mapping(target = "createdBy", source = "createdBy")
  Category dtoToEntity(CategoryDTO category, @Context CycleAvoidingMappingContext context);

  //  @AfterMapping
  //  default void ignoreFathersChildren(Category category, @MappingTarget CategoryDTO categoryDTO)
  // {
  //    categoryDTO.setTickets(null);
  //    categoryDTO.setDepartments(null);
  //  }
}
