package com.example.api.mapper;

import com.example.api.dto.TripDto;
import com.example.api.entity.Trip;
import org.springframework.stereotype.Component;

@Component
public class TripMapper {

    public static TripDto toTripDto(Trip trip) {
        TripDto tripDto = new TripDto();
        tripDto.setId(trip.getId());
        tripDto.setAgencyCode(trip.getAgency().getCode());
        tripDto.setSourceStopCode(trip.getSourceStop().getCode());
        tripDto.setSourceStopName(trip.getSourceStop().getName());
        tripDto.setDestinationStopCode(trip.getDestStop().getCode());
        tripDto.setDestinationStopName(trip.getDestStop().getName());
        tripDto.setBusCode(trip.getBus().getCode());
        tripDto.setJourneyTime(trip.getJourneyTime());
        tripDto.setFare(trip.getFare());
        return tripDto;


    }

}
