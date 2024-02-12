package be.congregationchretienne.ticketsystem.api.service.impl;

import static be.congregationchretienne.ticketsystem.api.mapping.UserMapping.INSTANCE_USER;

import be.congregationchretienne.ticketsystem.api.dto.UserDTO;
import be.congregationchretienne.ticketsystem.api.exception.IllegalArgumentException;
import be.congregationchretienne.ticketsystem.api.exception.NotFoundException;
import be.congregationchretienne.ticketsystem.api.exception.PropertyReferenceException;
import be.congregationchretienne.ticketsystem.api.helper.ValidationHelper;
import be.congregationchretienne.ticketsystem.api.mapping.CycleAvoidingMappingContext;
import be.congregationchretienne.ticketsystem.api.model.User;
import be.congregationchretienne.ticketsystem.api.repository.UserRepository;
import be.congregationchretienne.ticketsystem.api.service.UserService;
import java.time.LocalDateTime;
import java.util.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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

    Optional<User> user =
        Optional.ofNullable(
            repository
                .findById(uuid)
                .orElseThrow(() -> new NotFoundException("User not founded.")));

    return INSTANCE_USER.entityToDTO(user.get(), new CycleAvoidingMappingContext());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<UserDTO> getAll(Integer page, Integer pageSize, String orderBy, String sort) {

    Pageable pageable = null;
    Page<User> users = null;

    try {

      pageable = getPageable(page, pageSize, orderBy, sort);
      users = repository.findAll(pageable);

    } catch (IllegalArgumentException exception) {

      throw new IllegalArgumentException(exception.getMessage());
    } catch (PropertyReferenceException exception) {

      throw new PropertyReferenceException(
          String.format("No property [%s] found for type 'User'.", orderBy));
    }

    ValidationHelper.requireNonNull(users);

    return users.map(
        currentUser -> (INSTANCE_USER.entityToDTO(currentUser, new CycleAvoidingMappingContext())));
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void create(UserDTO bean) throws IllegalArgumentException {

    ValidationHelper.requireNonNull(bean);
    User user = INSTANCE_USER.dtoToEntity(bean);
    user.setCreatedAt(LocalDateTime.now());

    boolean present = repository.findByEmail(user.getEmail()).isPresent();

    if (present) {
      throw new IllegalArgumentException(
          String.format("User with email [%s] already exists.", user.getEmail()));
    }

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

    // TODO - remover as duas linhas abaixo
    // var user = repository.findById(UUID.fromString(id));
    // user.get().setCreatedAt(LocalDateTime.parse(user.get().getCreatedAt().toString()));

    repository.deleteById(uuid);
  }
}
