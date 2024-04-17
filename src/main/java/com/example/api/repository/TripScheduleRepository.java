package com.example.api.repository;

import com.example.api.entity.Trip;
import com.example.api.entity.TripSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripScheduleRepository extends JpaRepository<TripSchedule,Long> {

    TripSchedule findByTripDetailAndTripDate(Trip tripDetail, String tripDate);
}
