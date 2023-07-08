package be.congregationchretienne.ticketsystem.api.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IllegalArgumentException extends TechnicalException {

  public IllegalArgumentException(String message) {
    super(message);
  }
}
