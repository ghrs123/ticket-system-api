package be.congregationchretienne.ticketsystem.api.model;

import be.congregationchretienne.ticketsystem.api.model.type.Priority;
import be.congregationchretienne.ticketsystem.api.model.type.Status;
import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "tickets")
public class Ticket extends AbstractModelForCreatedBy {

  public static final long serialVersionUID = 1L;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "description", length = 5000)
  private String description;

  @Column(name = "reference", unique = true, nullable = false)
  private String reference;

  @Enumerated(EnumType.STRING)
  @Column(name = "priority", length = 10)
  private Priority priority;

  @ManyToOne
  @JoinColumn(name = "assigned_to_fk")
  private User assignedTo;

  @ManyToOne
  @JoinColumn(name = "department_fk")
  private Department department;

  @ManyToOne
  @JoinColumn(name = "category_fk")
  private Category category;

  @Column(name = "estimation")
  private Integer estimation;

  @Column(name = "started_on")
  private LocalDateTime startedOn = LocalDateTime.now();

  @Column(name = "resolved_on")
  private LocalDateTime resolvedOn;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", length = 15)
  private Status status;

  public Ticket() {
    reference = RandomStringUtils.randomAlphabetic(10, 11);
  }
}
