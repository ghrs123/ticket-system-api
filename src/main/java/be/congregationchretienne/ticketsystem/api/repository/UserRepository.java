package be.congregationchretienne.ticketsystem.api.repository;

import be.congregationchretienne.ticketsystem.api.model.User;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends AbstractRepository<User> {
  Optional<User> findByEmail(String email);
}
