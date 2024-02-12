package be.congregationchretienne.ticketsystem.api.dto;

import java.util.Set;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO extends AbstractDTO {

  @NotNull String name;
  Set<TicketDTO> tickets;
  Set<DepartmentDTO> departments;
  UserDTO createdBy;
}
