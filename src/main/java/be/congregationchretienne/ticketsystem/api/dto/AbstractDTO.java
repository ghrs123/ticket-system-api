package be.congregationchretienne.ticketsystem.api.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AbstractDTO {
  @NotNull String id;
  LocalDateTime createdAt;
}
