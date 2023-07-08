package be.congregationchretienne.ticketsystem.api.dto.request;

import javax.validation.constraints.NotNull;

public class ReqTicketDTO {

  @NotNull private String title;

  private String description;

  @NotNull private String assignedUserId;
}
