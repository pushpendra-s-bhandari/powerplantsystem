/**
 * Test Class for BatterServiceImpl class
 * @author  pushpendra
 * @version 1.0
 */
package com.pushpendra.powerplantsystem.service;

import com.pushpendra.powerplantsystem.dto.BatteryStat;
import com.pushpendra.powerplantsystem.dto.CapacityStat;
import com.pushpendra.powerplantsystem.entity.Battery;
import com.pushpendra.powerplantsystem.repository.BatteryRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BatteryServiceImplTest {
    @InjectMocks
    BatteryServiceImpl batteryService;

    @Mock
    BatteryRepository batteryRepository;
    static List<Battery> batteryList = new ArrayList<>();
    static List<Battery> batteryListWithoutID = new ArrayList<>();
    @BeforeAll
    public void createListofBatteries() {

        Battery battery1 = new Battery(1l, "Cannington", 1500l, 2345l);
        Battery battery2 = new Battery(2l, "Kalamunda", 700l, 3456l);
        Battery battery3 = new Battery(3l, "Kent Town", 400l, 789l);
        Battery battery4 = new Battery(4l, "Mildland", 1200l, 3425l);
        Battery battery5 = new Battery(5l, "Northgate Mc", 600l, 456l);

        batteryList.add(battery1);
        batteryList.add(battery2);
        batteryList.add(battery3);
        batteryList.add(battery4);
        batteryList.add(battery5);

        Battery battery1WithoutId = new Battery(null, "Cannington", 1500l, 2345l);
        Battery battery2WithoutId = new Battery(null, "Kalamunda", 700l, 3456l);
        Battery battery3WithoutId = new Battery(null, "Kent Town", 400l, 789l);
        Battery battery4WithoutId = new Battery(null, "Mildland", 1200l, 3425l);
        Battery battery5WithoutId = new Battery(null, "Northgate Mc", 600l, 456l);


        batteryListWithoutID.add(battery1WithoutId);
        batteryListWithoutID.add(battery2WithoutId);
        batteryListWithoutID.add(battery3WithoutId);
        batteryListWithoutID.add(battery4WithoutId);
        batteryListWithoutID.add(battery5WithoutId);

    }

    @Test
    @DisplayName("Test if Batteries sources can be saved in db or not")
    public void when_saveBatteries_thenCorrect() {

        Mockito.when(batteryRepository.saveAll(batteryListWithoutID)).thenReturn(batteryList);
        List<Battery> batteriesActual = batteryService.saveBatteries(batteryListWithoutID);

        assertEquals(5, batteriesActual.size());
        AtomicReference<Integer> idExpected = new AtomicReference<>(1);
        batteriesActual.forEach(battery -> {
            assertEquals(idExpected.toString(),battery.getId().toString());
            idExpected.getAndSet(idExpected.get() + 1);
        });
    }
    @Test
    @DisplayName("Test if Batteries sources Search between ranges work when if fall in the valid ranges")
    public void when_getBatteriesBetweenPostcodeRangesValidRanges_thenCorrect() {

        Sort sort = Sort.by("name").ascending();

        StringBuffer sortedListExpected = new StringBuffer();
        batteryList.forEach(battery -> {
            sortedListExpected.append(battery.getId().toString());
        });

        Double averageCapacityExpected = batteryList.stream()
                .collect(Collectors.averagingLong(Battery::getCapacity));

        Long sumCapacityExpected = batteryList.stream().mapToLong(Battery::getCapacity).sum();

        Mockito.when(batteryRepository.findByPostcodeBetween(100l, 1500l, sort)).thenReturn(batteryList);

        BatteryStat batteryStat = batteryService.getBatteriesBetweenPostcodeRanges(100l, 1500l);

        List<Battery> batteryListActual = batteryStat.getBatteryList();

        StringBuffer sortedListActual = new StringBuffer();
        batteryList.forEach(battery -> {
            sortedListActual.append(battery.getId().toString());
        });

        Double averageCapacityActual = batteryStat.getCapacityStat().getAverage();
        Long sumCapacityActual = batteryStat.getCapacityStat().getTotal();

        assertEquals(sortedListExpected.toString(), sortedListActual.toString());
        assertEquals(averageCapacityExpected, averageCapacityActual);
        assertEquals(sumCapacityExpected, sumCapacityActual);

    }

    @Test
    @DisplayName("Test if Batteries sources Search between ranges work when if does not fall in the valid ranges")
    public void when_getBatteriesBetweenPostcodeRangesNotInValidRanges_thenCorrect() {

        Sort sort = Sort.by("name").ascending();

        Mockito.when(batteryRepository.findByPostcodeBetween(100l, 1500l, sort)).thenReturn(null);

        BatteryStat batteryStat = batteryService.getBatteriesBetweenPostcodeRanges(100l, 1500l);

        List<Battery> batteryListActual = batteryStat.getBatteryList();

        CapacityStat capacityStatActual = batteryStat.getCapacityStat();

        assertNull(batteryListActual);
        assertNull(capacityStatActual);



    }
}
