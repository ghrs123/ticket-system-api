package be.congregationchretienne.ticketsystem.api.repository;

import be.congregationchretienne.ticketsystem.api.model.Ticket;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends AbstractRepository<Ticket> {
  Optional<Ticket> findByReference(String reference);
}
