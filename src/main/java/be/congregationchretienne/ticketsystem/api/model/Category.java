package be.congregationchretienne.ticketsystem.api.model;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class Category extends AbstractModelForCreatedBy {

  public static final long serialVersionUID = 1L;

  @Column(name = "name", nullable = false, unique = true)
  private String name;

  @OneToMany(mappedBy = "category", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private Set<Ticket> tickets = new HashSet<>();

  @ManyToMany(
      mappedBy = "categories",
      fetch = FetchType.EAGER,
      cascade = {CascadeType.REFRESH, CascadeType.DETACH, CascadeType.REMOVE})
  private Set<Department> departments = new HashSet<>();
}
