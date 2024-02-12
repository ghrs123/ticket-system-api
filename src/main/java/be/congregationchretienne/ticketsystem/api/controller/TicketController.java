package be.congregationchretienne.ticketsystem.api.controller;

import be.congregationchretienne.ticketsystem.api.dto.TicketDTO;
import be.congregationchretienne.ticketsystem.api.exception.NotFoundException;
import be.congregationchretienne.ticketsystem.api.service.TicketService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TicketController {

  private static final String TICKET_ENDPOINT = "/tickets";
  private static final String TICKET_ENDPOINT_ID = TICKET_ENDPOINT + "/{id}";

  @Autowired private TicketService ticketService;

  @GetMapping(path = TICKET_ENDPOINT_ID)
  public ResponseEntity<Object> getTicket(@PathVariable(value = "id") String id)
      throws NotFoundException {

    TicketDTO ticket = ticketService.get(id);

    return ResponseEntity.status(HttpStatus.OK).body(ticket);
  }

  @GetMapping(path = TICKET_ENDPOINT)
  public ResponseEntity<Object> getAllTickets(
      @RequestParam(required = false, defaultValue = "0") int page,
      @RequestParam(required = false, defaultValue = "20") int pageSize,
      @RequestParam(required = false, defaultValue = "") String orderBy,
      @RequestParam(required = false, defaultValue = "") String sort) {

    var tickets = ticketService.getAll(page, pageSize, orderBy, sort);

    return ResponseEntity.status(HttpStatus.OK).body(tickets);
  }

  @PostMapping(path = TICKET_ENDPOINT)
  public ResponseEntity createTicket(@Valid TicketDTO TicketDTO) {

    ticketService.create(TicketDTO);

    return new ResponseEntity<>("The resource was successfully created.", HttpStatus.CREATED);
  }

  @PutMapping(path = TICKET_ENDPOINT_ID)
  public ResponseEntity updateTicket(@Valid TicketDTO ticketDTO) {
    ticketService.update(ticketDTO);

    return new ResponseEntity<>("The resource was successfully updated.", HttpStatus.OK);
  }

  @DeleteMapping(path = TICKET_ENDPOINT_ID)
  public ResponseEntity deleteTicket(@PathVariable(value = "id") String id) {
    ticketService.delete(id);

    return new ResponseEntity<>("The resource was successfully deleted.", HttpStatus.OK);
  }
}
