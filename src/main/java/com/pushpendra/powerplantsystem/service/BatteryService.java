package com.pushpendra.powerplantsystem.service;

import com.pushpendra.powerplantsystem.dto.BatteryStat;
import com.pushpendra.powerplantsystem.entity.Battery;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface BatteryService {
    List<Battery> saveBatteries(List<Battery> batteries);
    Battery getBatteryByName(String name);
    BatteryStat getBatteriesBetweenPostcodeRanges(Long postCodeStart, Long postCodeEnd);
}
