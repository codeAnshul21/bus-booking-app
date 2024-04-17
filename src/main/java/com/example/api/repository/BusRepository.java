package com.example.api.repository;

import com.example.api.entity.Agency;
import com.example.api.entity.Bus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusRepository extends JpaRepository<Bus,Long> {

    Bus findByCode(String busCode);

    Bus findByCodeAndAgency(String busCode, Agency agency);
}
