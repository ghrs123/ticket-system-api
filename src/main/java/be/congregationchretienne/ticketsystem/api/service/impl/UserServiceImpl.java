package be.congregationchretienne.ticketsystem.api.service.impl;

import static be.congregationchretienne.ticketsystem.api.mapping.DepartmentMapping.INSTANCE_DEPARTMENT;
import static be.congregationchretienne.ticketsystem.api.mapping.UserMapping.INSTANCE_USER;

import be.congregationchretienne.ticketsystem.api.dto.UserDTO;
import be.congregationchretienne.ticketsystem.api.helper.ValidationHelper;
import be.congregationchretienne.ticketsystem.api.mapping.CycleAvoidingMappingContext;
import be.congregationchretienne.ticketsystem.api.model.User;
import be.congregationchretienne.ticketsystem.api.repository.UserRepository;
import be.congregationchretienne.ticketsystem.api.service.UserService;

import java.time.LocalDateTime;
import java.util.Optional;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Getter
@Transactional
public class UserServiceImpl extends AbstractServiceImpl<UserDTO> implements UserService {

  @Autowired private UserRepository repository;

  public UserServiceImpl(UserRepository repository) {
    super(repository);
  }

  @Override
  @Transactional(readOnly = true)
  public UserDTO get(String id) {
    var uuid = checkAndConvertID(id);

    Optional<User> user = repository.findById(uuid);

    return INSTANCE_USER.entityToDTO(user.get(), new CycleAvoidingMappingContext());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<UserDTO> getAll(Integer page, Integer pageSize, String orderBy, String sort) {

    var pageable = getPageable(page, pageSize, orderBy, sort);
    Page<User> user = repository.findAll(pageable);
    ValidationHelper.requireNonNull(user);

    //    return user.map(INSTANCE_USER::entityToDTO);
    return user.map(
        currentUser -> (INSTANCE_USER.entityToDTO(currentUser, new CycleAvoidingMappingContext())));
  }

  @Override
  public void create(UserDTO bean) {

    ValidationHelper.requireNonNull(bean);
    User user = INSTANCE_USER.dtoToEntity(bean);
    user.setCreatedAt(LocalDateTime.now());

    repository.save(user);
  }

  @Override
  public void update(UserDTO userDTO) {
    checkAndConvertID(userDTO.getId());

    repository.save(INSTANCE_USER.dtoToEntity(userDTO));
  }

  @Override
  public void delete(String id) {
    var uuid = checkAndConvertID(id);

    repository.deleteById(uuid);
  }
}
