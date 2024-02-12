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

  @OneToMany(
      mappedBy = "assignedTo",
      cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH, CascadeType.REMOVE})
  private Set<Ticket> ticketsAssignedToMe = new HashSet<>();

  @OneToMany(
      mappedBy = "createdBy",
      cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH, CascadeType.REMOVE})
  private Set<Ticket> ticketsCreatedByMe = new HashSet<>();

  @ManyToMany(
      mappedBy = "users",
      cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH, CascadeType.REMOVE})
  private Set<Department> myDepartments = new HashSet<>();
}
