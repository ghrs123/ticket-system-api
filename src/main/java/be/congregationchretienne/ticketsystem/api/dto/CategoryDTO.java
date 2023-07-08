package be.congregationchretienne.ticketsystem.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Set;

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
