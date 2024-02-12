package be.congregationchretienne.ticketsystem.api.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PropertyReferenceException extends TechnicalException {
  public PropertyReferenceException(String message) {
    super(message);
  }
}
