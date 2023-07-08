package be.congregationchretienne.ticketsystem.api.mapping;

import be.congregationchretienne.ticketsystem.api.dto.TicketDTO;
import be.congregationchretienne.ticketsystem.api.model.Ticket;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TicketMapping {

  TicketMapping INSTANCE_TICKET = Mappers.getMapper(TicketMapping.class);

  @Mapping(source = "createdBy", target = "createdBy")
  @Mapping(source = "assignedTo", target = "assignedTo")
  TicketDTO entityToDTO(Ticket ticket, @Context CycleAvoidingMappingContext context);

  // Set<TicketDTO> entityToDTO(Set<Ticket> tickets);

  Ticket dtoToEntity(TicketDTO ticketDTO, @Context CycleAvoidingMappingContext context);

  // Set<Ticket> ListdtoToEntity(Set<TicketDTO> ticketsDTO);
  //  @AfterMapping
  //  default void ignoreFathersChildren(Ticket ticket, @MappingTarget TicketDTO ticketDTO) {
  //    ticketDTO.category().setTickets(null);
  //    ticketDTO.department().setTickets(null);
  //  }
}
