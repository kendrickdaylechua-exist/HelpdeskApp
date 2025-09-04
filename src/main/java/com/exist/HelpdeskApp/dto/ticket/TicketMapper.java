package com.exist.HelpdeskApp.dto.ticket;

import com.exist.HelpdeskApp.model.Ticket;
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

    @Mapping(source = "assignee.name", target = "assigneeName")
    @Mapping(source = "createdBy.name", target = "createdByEmployeeName")
    @Mapping(source = "updatedBy.name", target = "updatedByEmployeeName")
    TicketResponse toResponse(Ticket entity);

    List<TicketResponse> toResponseList(List<Ticket> tickets);

    void toUpdate(TicketRequest ticketRequest, @MappingTarget Ticket ticket);
}
