package be.congregationchretienne.ticketsystem.api.exception;

import lombok.*;

@Setter
@Getter
public class NotFoundException extends TechnicalException {

  public NotFoundException(String message) {
    super(message);
  }
}
