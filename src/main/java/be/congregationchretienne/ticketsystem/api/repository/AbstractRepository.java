package be.congregationchretienne.ticketsystem.api.repository;

import be.congregationchretienne.ticketsystem.api.model.AbstractModel;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbstractRepository<T extends AbstractModel> extends JpaRepository<T, UUID> {}
