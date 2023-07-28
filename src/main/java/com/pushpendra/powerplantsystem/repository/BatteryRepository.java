package com.pushpendra.powerplantsystem.repository;

import com.pushpendra.powerplantsystem.entity.Battery;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BatteryRepository extends JpaRepository<Battery,Long> {
    Optional<Battery> findByName(String name);
    List<Battery> findByPostcodeBetween(Long postCodeStart, Long postCodeEnd, Sort sort);
}
