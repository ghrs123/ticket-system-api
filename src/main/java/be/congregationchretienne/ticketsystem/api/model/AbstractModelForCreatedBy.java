package be.congregationchretienne.ticketsystem.api.model;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@MappedSuperclass
public abstract class AbstractModelForCreatedBy extends AbstractModel {

  @ManyToOne
  @JoinColumn(name = "created_by_fk")
  private User createdBy;
}
