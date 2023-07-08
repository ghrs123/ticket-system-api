package be.congregationchretienne.ticketsystem.api.repository;

import be.congregationchretienne.ticketsystem.api.model.Ticket;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends AbstractRepository<Ticket> {}
