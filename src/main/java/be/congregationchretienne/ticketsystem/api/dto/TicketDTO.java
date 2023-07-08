package be.congregationchretienne.ticketsystem.api.dto;

import be.congregationchretienne.ticketsystem.api.dto.type.PriorityDTO;
import be.congregationchretienne.ticketsystem.api.dto.type.StatusDTO;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TicketDTO extends AbstractDTO {

  @NotNull
  @Size(max = 255)
  String title;

  @Size(max = 5000)
  String description;

  @NotNull String reference;

  @Size(max = 100)
  PriorityDTO priority;

  @NotNull UserDTO assignedTo;
  @NotNull DepartmentDTO department;
  CategoryDTO category;
  Integer estimation;
  LocalDateTime startedOn;
  LocalDateTime resolvedOn;
  StatusDTO status;
  UserDTO createdBy;
}
