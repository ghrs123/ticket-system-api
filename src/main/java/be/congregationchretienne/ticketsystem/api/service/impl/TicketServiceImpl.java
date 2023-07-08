package be.congregationchretienne.ticketsystem.api.service.impl;

import static be.congregationchretienne.ticketsystem.api.mapping.TicketMapping.INSTANCE_TICKET;

import be.congregationchretienne.ticketsystem.api.dto.TicketDTO;
import be.congregationchretienne.ticketsystem.api.helper.ValidationHelper;
import be.congregationchretienne.ticketsystem.api.mapping.CycleAvoidingMappingContext;
import be.congregationchretienne.ticketsystem.api.model.Ticket;
import be.congregationchretienne.ticketsystem.api.repository.TicketRepository;
import be.congregationchretienne.ticketsystem.api.service.TicketService;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Getter
@Setter
@Transactional
public class TicketServiceImpl extends AbstractServiceImpl<TicketDTO> implements TicketService {

  @Autowired private TicketRepository repository;

  public TicketServiceImpl(TicketRepository repository) {
    super(repository);
  }

  @Override
  @Transactional(readOnly = true)
  public TicketDTO get(String id) {
    var uuid = checkAndConvertID(id);

    Optional<Ticket> ticket = repository.findById(uuid);

    return INSTANCE_TICKET.entityToDTO(ticket.get(), new CycleAvoidingMappingContext());
  }

  @Transactional(readOnly = true)
  @Override
  public Page<TicketDTO> getAll(Integer page, Integer pageSize, String orderBy, String sort) {

    var pageable = getPageable(page, pageSize, orderBy, sort);

    Page<Ticket> ticket = repository.findAll(pageable);
    ValidationHelper.requireNonNull(ticket);

    return ticket.map(
        currentTicket ->
            (INSTANCE_TICKET.entityToDTO(currentTicket, new CycleAvoidingMappingContext())));
  }

  @Override
  public void create(TicketDTO bean) {

    ValidationHelper.requireNonNull(bean);
    Ticket ticket = INSTANCE_TICKET.dtoToEntity(bean, new CycleAvoidingMappingContext());
    ticket.setCreatedAt(LocalDateTime.now());

    repository.save(ticket);
  }

  @Override
  public void update(TicketDTO ticketDTO) {
    checkAndConvertID(ticketDTO.getId());

    repository.save(INSTANCE_TICKET.dtoToEntity(ticketDTO, new CycleAvoidingMappingContext()));
  }

  @Override
  public void delete(String id) {
    var uuid = checkAndConvertID(id);

    repository.deleteById(uuid);
  }
}
