/**
 * Power Plant Controller Class
 * This contains two rest endpoint, one for saving batter sources and other for searching battery sources based on postcode range
 *
 * @author  pushpendra
 * @version 1.0
 */
package com.pushpendra.powerplantsystem.controller;

import com.pushpendra.powerplantsystem.dto.BatteryStat;
import com.pushpendra.powerplantsystem.entity.Battery;
import com.pushpendra.powerplantsystem.service.BatteryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/powerplant")
public class PowerPlantController {
    @Autowired
    private BatteryServiceImpl batteryService;

    /**
     * This is the rest end point used to persist batteries source to the db.
     * if the battery source is new then create it otherwise update it.
     * @param batteries This is the list of Batteries sources to be saved in db
     * @return ResponseEntity<List<Battery>> This will return all the saved Batteries sources as ResponseEntity
     */
    @PostMapping("/battery/add")
    public ResponseEntity<List<Battery>> saveBatteries(@RequestBody List<Battery> batteries) {
        List<Battery> savedBatteryList = batteryService.saveBatteries(batteries);
        return new ResponseEntity<>(savedBatteryList, HttpStatus.OK);
    }

    /**
     * This is the rest endpoint to find batteries sources that fall within the given postcode ranges.
     * The batteries list are sorted alphabetically along with statistics of average and total watt capacities
     * @param postCodeStart This is the first param which is used as starting postCode
     * @param postCodeEnd   This is the second param which is used as ending postCode
     * @return BatteryStat This will return all the batteries sources which fall withing the postcode range sorted alphabetically with statistics of total and average watt capacity
     */
    @GetMapping("/battery/range/postcode")
    public BatteryStat getBatteriesBetweenPostcodeRanges(@RequestParam Long postCodeStart, @RequestParam Long postCodeEnd){
       return batteryService.getBatteriesBetweenPostcodeRanges(postCodeStart,postCodeEnd);
    }
}