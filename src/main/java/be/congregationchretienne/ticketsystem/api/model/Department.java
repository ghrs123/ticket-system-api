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
@Table(name = "departments")
public class Department extends AbstractModelForCreatedBy {

  public static final long serialVersionUID = 1L;

  @Column(name = "name", length = 255, nullable = false, unique = true)
  private String name;

  @ManyToMany(
      fetch = FetchType.EAGER,
      cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH, CascadeType.REMOVE})
  @JoinTable(
      name = "departments_categories",
      joinColumns = @JoinColumn(name = "department_fk", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "category_fk", referencedColumnName = "id"))
  private Set<Category> categories = new HashSet<>();

  @ManyToMany(
      fetch = FetchType.EAGER,
      cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH, CascadeType.REMOVE})
  @JoinTable(
      name = "departments_users",
      joinColumns = @JoinColumn(name = "department_fk", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "user_fk", referencedColumnName = "id"))
  private Set<User> users = new HashSet<>();

  @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private Set<Ticket> tickets = new HashSet<>();
}
