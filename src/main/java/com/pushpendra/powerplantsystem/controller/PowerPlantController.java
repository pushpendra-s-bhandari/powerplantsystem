package com.pushpendra.powerplantsystem.controller;

import com.pushpendra.powerplantsystem.service.BatteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/powerplant")
public class PowerPlantController {
    @Autowired
    private BatteryService batteryService;
}
