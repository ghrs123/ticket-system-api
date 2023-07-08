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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends AbstractModel {

  public static final long serialVersionUID = 1L;

  @Column(name = "name", length = 255, nullable = false)
  private String name;

  @Column(name = "email", length = 255, unique = true)
  private String email;

  @OneToMany(mappedBy = "assignedTo", cascade = CascadeType.ALL)
  private Set<Ticket> ticketsAssignedToMe = new HashSet<>();

  @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL)
  private Set<Ticket> ticketsCreatedByMe;

  @ManyToMany(mappedBy = "users", cascade = CascadeType.ALL)
  private Set<Department> myDepartments = new HashSet<>();
}
