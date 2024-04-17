package com.example.api.mapper;

import com.example.api.dto.TripScheduleDto;
import com.example.api.entity.Trip;
import com.example.api.entity.TripSchedule;
import org.springframework.stereotype.Component;

@Component
public class TripScheduleMapper {

    public static TripScheduleDto toTripScheduleDto(TripSchedule tripSchedule) {

        Trip tripDetails = tripSchedule.getTripDetail();
        return new TripScheduleDto()
                .setId(tripSchedule.getId())
                .setTripId(tripDetails.getId())
                .setBusCode(tripDetails.getBus().getCode())
                .setAvailableSeats(tripSchedule.getAvailableSeats())
                .setFare(tripDetails.getFare())
                .setJourneyTime(tripDetails.getJourneyTime())
                .setSourceStop(tripDetails.getSourceStop().getName())
                .setDestinationStop(tripDetails.getDestStop().getName());
    }

    }
