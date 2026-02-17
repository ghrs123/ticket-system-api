package be.congregationchretienne.ticketsystem.api.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class AbstractDTO {

  String id;

  LocalDateTime createdAt;
}
