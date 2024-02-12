package be.congregationchretienne.ticketsystem.api.service;

import org.springframework.data.domain.Page;

public interface AbstractService<T> {

  T get(String id);

  Page<T> getAll(Integer page, Integer pageSize, String orderBy, String sort);

  void create(T bean);

  void update(T bean);

  void delete(String id);
}
