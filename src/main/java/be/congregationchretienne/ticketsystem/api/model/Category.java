package be.congregationchretienne.ticketsystem.api.model;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class Category extends AbstractModelForCreatedBy {

  public static final long serialVersionUID = 1L;

  @Column(name = "name", nullable = false)
  private String name;

  @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
  private Set<Ticket> tickets = new HashSet<>();

  @ManyToMany(mappedBy = "categories", cascade = CascadeType.REMOVE)
  private Set<Department> departments = new HashSet<>();
}
