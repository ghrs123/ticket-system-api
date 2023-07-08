package be.congregationchretienne.ticketsystem.api.mapping;

import be.congregationchretienne.ticketsystem.api.dto.DepartmentDTO;
import be.congregationchretienne.ticketsystem.api.model.Department;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DepartmentMapping {

  DepartmentMapping INSTANCE_DEPARTMENT = Mappers.getMapper(DepartmentMapping.class);

  DepartmentDTO entityToDTO(Department department, @Context CycleAvoidingMappingContext context);

  // Set<DepartmentDTO> entityToDTO(Set<Department> departments);

  Department dtoToEntity(DepartmentDTO department);

  //  @AfterMapping
  //  default void ignoreFathersChildren(
  //      Department department, @MappingTarget DepartmentDTO departmentDTO) {
  //    departmentDTO.setCategories(null);
  //    departmentDTO.setTickets(null);
  //  }
  // Set<Department> dtoToEntity(Set<DepartmentDTO> departments);
}
