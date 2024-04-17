package com.example.api.controller;

import com.example.api.dto.TicketDto;
import com.example.api.dto.TripDto;
import com.example.api.dto.TripScheduleDto;
import com.example.api.dto.UserDto;
import com.example.api.dto.request.BookTicketRequest;
import com.example.api.dto.request.GetTripSchedulesRequest;
import com.example.api.dto.response.Response;
import com.example.api.service.BusReservationService;
import com.example.api.service.UserService;
import com.example.api.utils.DateUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author Shad Ahmad
 */

@RestController
@RequestMapping("/api/v1/reservation")
public class BusReservationController {

    @Autowired
    private BusReservationService busReservationService;

    @Autowired
    private UserService userService;

    @GetMapping("/stops")
    public Response getAllStops() {
        return Response
                .ok()
                .setPayload(busReservationService.getAllStops());
    }

    @GetMapping("/tripsbystops")
    public Response getTripsByStops(@RequestBody @Valid GetTripSchedulesRequest getTripSchedulesRequest) {
        List<TripDto> tripDtos = busReservationService.getAvailableTripsBetweenStops(
                getTripSchedulesRequest.getSourceStop(),
                getTripSchedulesRequest.getDestinationStop());
        if (!tripDtos.isEmpty()) {
            return Response.ok().setPayload(tripDtos);
        }
        return Response.notFound()
                .setErrors(String.format("No trips between source stop - '%s' and destination stop - '%s' are available at this time.", getTripSchedulesRequest.getSourceStop(), getTripSchedulesRequest.getDestinationStop()));
    }

    @GetMapping("/tripschedules")
    public Response getTripSchedules(@RequestBody @Valid GetTripSchedulesRequest getTripSchedulesRequest) {
        List<TripScheduleDto> tripScheduleDtos = busReservationService.getAvailableTripSchedules(
                getTripSchedulesRequest.getSourceStop(),
                getTripSchedulesRequest.getDestinationStop(),
                DateUtils.formattedDate(getTripSchedulesRequest.getTripDate()));
        if (!tripScheduleDtos.isEmpty()) {
            return Response.ok().setPayload(tripScheduleDtos);
        }
        return Response.notFound()
                .setErrors(String.format("No trips between source stop - '%s' and destination stop - '%s' on date - '%s' are available at this time.", getTripSchedulesRequest.getSourceStop(), getTripSchedulesRequest.getDestinationStop(), DateUtils.formattedDate(getTripSchedulesRequest.getTripDate())));
    }

    @PostMapping("/bookticket")
    public Response bookTicket(@RequestBody @Valid BookTicketRequest bookTicketRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) auth.getPrincipal();
        Optional<UserDto> userDto = Optional.ofNullable(userService.findUserByEmail(email));
        if (userDto.isPresent()) {
            Optional<TripDto> tripDto = Optional
                    .ofNullable(busReservationService.getTripById(bookTicketRequest.getTripID()));
            if (tripDto.isPresent()) {
                Optional<TripScheduleDto> tripScheduleDto = Optional
                        .ofNullable(busReservationService.getTripSchedule(tripDto.get(), DateUtils.formattedDate(bookTicketRequest.getTripDate()), true));
                if (tripScheduleDto.isPresent()) {
                    Optional<TicketDto> ticketDto = Optional
                            .ofNullable(busReservationService.bookTicket(tripScheduleDto.get(), userDto.get()));
                    if (ticketDto.isPresent()) {
                        return Response.ok().setPayload(ticketDto.get());
                    }
                }
            }
        }
        return Response.badRequest().setErrors("Unable to process ticket booking.");
    }
}
