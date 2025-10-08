package com.exist.HelpdeskApp.dto.ticket;

import com.exist.HelpdeskApp.model.Ticket;
import com.exist.HelpdeskApp.model.embeddable.Name;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TicketMapper {
    @Mapping(target = "assignee", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    Ticket toEntity(TicketRequest request);

    List<TicketResponse> toResponseList(List<Ticket> tickets);

    @Mapping(target = "assigneeName", expression = "java(concatName(entity.getAssignee().getName()))")
    @Mapping(target = "createdByEmployeeName", expression = "java(concatName(entity.getCreatedBy().getName()))")
    @Mapping(target = "updatedByEmployeeName", expression = "java(concatName(entity.getUpdatedBy().getName()))")
    @Mapping(target = "assigneeId", source = "assignee.id")
    @Mapping(target = "createdByEmployeeId", source = "createdBy.id")
    @Mapping(target = "updatedByEmployeeId", source = "updatedBy.id")
    TicketResponse toResponse(Ticket entity);

    void toUpdate(TicketRequest ticketRequest, @MappingTarget Ticket ticket);

    default String concatName(Name name) {
        if (name == null) return null;
        StringBuilder fullName = new StringBuilder();
        if (name.getFirstName() != null) fullName.append(name.getFirstName());
        if (name.getMiddleName() != null && !name.getMiddleName().isBlank())
            fullName.append(" ").append(name.getMiddleName());
        if (name.getLastName() != null) fullName.append(" ").append(name.getLastName());
        return fullName.toString().trim();
    }
}
