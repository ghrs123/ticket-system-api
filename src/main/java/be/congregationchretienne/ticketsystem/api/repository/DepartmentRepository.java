package be.congregationchretienne.ticketsystem.api.repository;

import be.congregationchretienne.ticketsystem.api.model.Department;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends AbstractRepository<Department> {

  Optional<Department> findByNameIgnoreCase(String name);
}
