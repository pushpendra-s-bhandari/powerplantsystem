package com.pushpendra.powerplantsystem.service;

import com.pushpendra.powerplantsystem.repository.BatteryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BatteryService {
    @Autowired
    private BatteryRepository batteryRepository;
}
