package com.example.api.mapper;

import com.example.api.dto.TicketDto;
import com.example.api.entity.Ticket;
import org.springframework.stereotype.Component;

@Component
public class TicketMapper {

    public static TicketDto toTicketDto(Ticket ticket) {
        TicketDto ticketDto = new TicketDto();
        ticketDto.setId(ticket.getId());
        ticketDto.setBusCode(ticket.getTripSchedule().getTripDetail().getBus().getCode());
        ticketDto.setSeatNumber(ticket.getSeatNumber());
        ticketDto.setSourceStop(ticket.getTripSchedule().getTripDetail().getSourceStop().getName());
        ticketDto.setDestinationStop(ticket.getTripSchedule().getTripDetail().getDestStop().getName());
        ticketDto.setCancellable(false);
        ticketDto.setJourneyDate(ticket.getJourneyDate());
        ticketDto.setPassengerName(ticket.getPassenger().getFullName());
        ticketDto.setPassengerMobileNumber(ticket.getPassenger().getMobileNumber());
        return ticketDto;


    }


}
