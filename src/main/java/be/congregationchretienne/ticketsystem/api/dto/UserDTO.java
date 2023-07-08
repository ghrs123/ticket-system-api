package be.congregationchretienne.ticketsystem.api.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO extends AbstractDTO {

  @NotNull
  @Size(max = 255)
  String name;

  @NotNull
  @Size(max = 255)
  @Email(message = "Email should be valid.")
  String email;
}
