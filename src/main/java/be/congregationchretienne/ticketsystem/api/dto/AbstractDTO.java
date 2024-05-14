package be.congregationchretienne.ticketsystem.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class AbstractDTO {

    String id;

    LocalDateTime createdAt;
}
