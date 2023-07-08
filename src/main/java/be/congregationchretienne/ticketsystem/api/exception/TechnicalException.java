package be.congregationchretienne.ticketsystem.api.exception;

import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public abstract class TechnicalException extends RuntimeException {

  protected String eventId;

  public TechnicalException(String message) {
    super(message);

    this.eventId = UUID.randomUUID().toString();
  }
}
