package be.congregationchretienne.ticketsystem.api.service.impl;

import be.congregationchretienne.ticketsystem.api.dto.AbstractDTO;
import be.congregationchretienne.ticketsystem.api.exception.IllegalArgumentException;
import be.congregationchretienne.ticketsystem.api.exception.NotFoundException;
import be.congregationchretienne.ticketsystem.api.helper.ValidationHelper;
import be.congregationchretienne.ticketsystem.api.repository.AbstractRepository;
import be.congregationchretienne.ticketsystem.api.service.AbstractService;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public abstract class AbstractServiceImpl<T extends AbstractDTO> implements AbstractService<T> {

  protected AbstractRepository repository;

  protected final UUID checkAndConvertID(String id) {
    if (!ValidationHelper.isNotBlank(id)) {
      throw new IllegalArgumentException(
          String.format("The resource reference [%s] must be not null.", id));
    }

    UUID uuid = null;
    try {
      uuid = UUID.fromString(id);
    } catch (RuntimeException exception) {
      throw new IllegalArgumentException(
          String.format("The resource reference [%s] is invalid, must be the UUID format.", id));
    }

    if (!repository.existsById(uuid)) {
      throw new NotFoundException(
          String.format("The resource with reference [%s] was not found.", uuid));
    }
    return uuid;
  }

  protected final Pageable getPageable(Integer page, Integer pageSize, String orderBy, String sort)
      throws RuntimeException {

    Pageable pageable = null;

    if (pageSize <= 0 || pageSize > 50) {
      throw new IllegalArgumentException(
          "The number of items per page should be between 1 and 50.");
    } else {
      PageRequest.of(page, pageSize);
    }

    if (ValidationHelper.isNotBlank(orderBy) && orderBy != null) {

      pageable = PageRequest.of(page, pageSize).withSort(Sort.by(getSortDirection(sort), orderBy));

    } else {

      throw new IllegalArgumentException(
          String.format("The parameter orderBy [%s] is invalid.", orderBy));
    }

    return pageable;
  }

  protected final Sort.Direction getSortDirection(String sort) {

    if (ValidationHelper.isNotBlank(sort)) {
      try {

        return Sort.Direction.fromString(sort);

      } catch (RuntimeException exception) {

        return Sort.Direction.ASC;
      }
    }
    return Sort.Direction.ASC;
  }
}
