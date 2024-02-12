package be.congregationchretienne.ticketsystem.api.dto;

import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class AbstractDTO {
  String id;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  LocalDateTime createdAt;
}
