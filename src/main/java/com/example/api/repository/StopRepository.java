package com.example.api.repository;

import com.example.api.entity.Stop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StopRepository extends JpaRepository<Stop,Long> {

    Stop findByCode(String code);
}
