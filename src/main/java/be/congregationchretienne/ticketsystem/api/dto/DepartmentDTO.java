package be.congregationchretienne.ticketsystem.api.dto;

import be.congregationchretienne.ticketsystem.api.model.Category;
import be.congregationchretienne.ticketsystem.api.model.Ticket;
import be.congregationchretienne.ticketsystem.api.model.User;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDTO extends AbstractDTO {

  @NotNull
  @Size(max = 255)
  String name;

  UserDTO createdBy;

  Set<Category> categories;
  Set<User> users;
  Set<Ticket> tickets;
}
