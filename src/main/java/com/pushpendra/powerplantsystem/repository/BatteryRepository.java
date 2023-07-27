package com.pushpendra.powerplantsystem.repository;

import com.pushpendra.powerplantsystem.entity.Battery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatteryRepository extends JpaRepository<Battery,Long> {
}
