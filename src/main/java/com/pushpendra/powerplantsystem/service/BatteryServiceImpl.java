/**
 * Battery Service Implementation class
 * This contains all the functionalities implementation defined in the  BatteryService Interface
 *
 * @author  pushpendra
 * @version 1.0
 */
package com.pushpendra.powerplantsystem.service;

import com.pushpendra.powerplantsystem.dto.BatteryStat;
import com.pushpendra.powerplantsystem.dto.CapacityStat;
import com.pushpendra.powerplantsystem.entity.Battery;
import com.pushpendra.powerplantsystem.repository.BatteryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BatteryServiceImpl implements BatteryService {
    @Autowired
    private BatteryRepository batteryRepository;

    /**
     * This method is used to persist batteries source to the db.
     * if the battery source is new then create it otherwise update it.
     * @param batteries This is the list of Batteries sources to be saved in db
     * @return List<Battery> This will return all the saved Batteries sources
     */
    public List<Battery> saveBatteries(List<Battery> batteries) {
        // iterate list of batteries with forEach
        List<Battery> savedList = new ArrayList<>();
        batteries.forEach(battery -> {

            Battery existingBattery= getBatteryByName(battery.getName());

            if (existingBattery !=null){
                existingBattery.setCapacity(battery.getCapacity());
                existingBattery.setPostcode(battery.getPostcode());
                batteryRepository.save(existingBattery);
                savedList.add(existingBattery);

            }
            else{
                Battery newBattery = new Battery();
                newBattery.setName(battery.getName());
                newBattery.setPostcode(battery.getPostcode());
                newBattery.setCapacity(battery.getCapacity());
                batteryRepository.save(newBattery);
                savedList.add(newBattery);
            }

        });

        return savedList;
    }

    /**
     * This method is used to find batteries sources that fall within the given postcode ranges.
     * The batteries list are sorted alphabetically along with statistics of average and total watt capacities
     * @param postCodeStart This is the first param which is used as starting postCode
     * @param postCodeEnd   This is the second param which is used as ending postCode
     * @return BatteryStat This will return all the batteries sources which fall withing the postcode range sorted alphabetically with statistics of total and average watt capacity
     */
    public BatteryStat getBatteriesBetweenPostcodeRanges(Long postCodeStart, Long postCodeEnd){

        Sort sort = Sort.by("name").ascending();
        List<Battery> batteries = batteryRepository.findByPostcodeBetween(postCodeStart, postCodeEnd, sort);

        BatteryStat batteryStat = new BatteryStat();
        batteryStat.setBatteryList(batteries);

        Double averageCapacity = batteries.stream()
                .collect(Collectors.averagingLong(Battery::getCapacity));
        Long sumCapacity = batteries.stream().mapToLong(Battery::getCapacity).sum();

        CapacityStat capacityStat = new CapacityStat();
        capacityStat.setAverage(averageCapacity);
        capacityStat.setTotal(sumCapacity);

        batteryStat.setCapacityStat(capacityStat);

        return batteryStat;
    }
    /**
     * This method is used to find Battery sources by name
     * If the battery source is not available in db, it returns null.
     * @param name This is the name param by which the battery source needs to be find
     * @return Battery This will return the desired battery based on the name passed.
     */
    public Battery getBatteryByName(String name){
        Optional<Battery> batteryOptional = batteryRepository.findByName(name);
        return batteryOptional.orElse(null);
    }
}