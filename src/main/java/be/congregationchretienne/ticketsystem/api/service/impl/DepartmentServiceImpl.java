package be.congregationchretienne.ticketsystem.api.service.impl;

import static be.congregationchretienne.ticketsystem.api.mapping.DepartmentMapping.INSTANCE_DEPARTMENT;
import static be.congregationchretienne.ticketsystem.api.mapping.TicketMapping.INSTANCE_TICKET;

import be.congregationchretienne.ticketsystem.api.dto.DepartmentDTO;
import be.congregationchretienne.ticketsystem.api.helper.ValidationHelper;
import be.congregationchretienne.ticketsystem.api.mapping.CycleAvoidingMappingContext;
import be.congregationchretienne.ticketsystem.api.model.Department;
import be.congregationchretienne.ticketsystem.api.model.Ticket;
import be.congregationchretienne.ticketsystem.api.repository.DepartmentRepository;
import be.congregationchretienne.ticketsystem.api.service.DepartmentService;

import java.time.LocalDateTime;
import java.util.Optional;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Getter
@Transactional
public class DepartmentServiceImpl extends AbstractServiceImpl<DepartmentDTO>
    implements DepartmentService {

  @Autowired private DepartmentRepository repository;

  public DepartmentServiceImpl(DepartmentRepository repository) {
    super(repository);
  }

  @Override
  @Transactional(readOnly = true)
  public DepartmentDTO get(String id) {
    var uuid = checkAndConvertID(id);

    Optional<Department> department = repository.findById(uuid);

    return INSTANCE_DEPARTMENT.entityToDTO(department.get(), new CycleAvoidingMappingContext());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<DepartmentDTO> getAll(Integer page, Integer pageSize, String orderBy, String sort) {

    var pageable = getPageable(page, pageSize, orderBy, sort);

    Page<Department> departments = repository.findAll(pageable);
    ValidationHelper.requireNonNull(departments);

    return departments.map(
        currentDepartment ->
            (INSTANCE_DEPARTMENT.entityToDTO(
                currentDepartment, new CycleAvoidingMappingContext())));
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void create(DepartmentDTO bean) {

    ValidationHelper.requireNonNull(bean);
    var department = INSTANCE_DEPARTMENT.dtoToEntity(bean);
    department.setCreatedAt(LocalDateTime.now());

    repository.save(department);
  }

  @Override
  public void update(DepartmentDTO departmentDTO) {
    checkAndConvertID(departmentDTO.getId());

    repository.save(INSTANCE_DEPARTMENT.dtoToEntity(departmentDTO));
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void delete(String id) {
    var uuid = checkAndConvertID(id);

    repository.deleteById(uuid);
  }
}
