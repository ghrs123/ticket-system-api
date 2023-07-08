package be.congregationchretienne.ticketsystem.api.mapping;

import be.congregationchretienne.ticketsystem.api.dto.UserDTO;
import be.congregationchretienne.ticketsystem.api.model.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {DepartmentMapping.class, TicketMapping.class})
public interface UserMapping {

  UserMapping INSTANCE_USER = Mappers.getMapper(UserMapping.class);

  UserDTO entityToDTO(User user, @Context CycleAvoidingMappingContext context);

  User dtoToEntity(UserDTO user);
}
