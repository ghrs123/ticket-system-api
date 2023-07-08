package be.congregationchretienne.ticketsystem.api.service.impl;

import be.congregationchretienne.ticketsystem.api.dto.AbstractDTO;
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
      throw new IllegalArgumentException("The resource reference must be not null.");
    }

    UUID uuid = null;
    try {
      uuid = UUID.fromString(id);
    } catch (Exception exception) {
      throw new IllegalArgumentException("The resource reference is invalid.");
    }

    if (!repository.existsById(uuid)) {
      throw new NotFoundException(
          String.format("The resource with reference [%s] was not found.", uuid));
    }
    return uuid;
  }

  protected final Pageable getPageable(
      Integer page, Integer pageSize, String orderBy, String sort) {
    Pageable pageable = PageRequest.of(page, pageSize);

    if (pageSize <= 0 || pageSize > 50) {
      throw new IllegalArgumentException("The number items per page should be between 1 and 50.");
    }

    if (!ValidationHelper.isNotBlank(orderBy)) {
      try {
        pageable =
            PageRequest.of(page, pageSize).withSort(Sort.by(getSortDirection(sort), orderBy));
      } catch (Exception e) {
        throw new IllegalArgumentException(
            String.format("The parameter orderBy [%s] is invalid.", orderBy));
      }
    }

    return pageable;
  }

  protected final Sort.Direction getSortDirection(String sort) {
    if (ValidationHelper.isNotBlank(sort)) {
      try {
        return Sort.Direction.fromString(sort);
      } catch (Exception exception) {
        return Sort.Direction.ASC;
      }
    }
    return Sort.Direction.ASC;
  }
}
