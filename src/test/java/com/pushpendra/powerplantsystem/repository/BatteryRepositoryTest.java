/**
 * Test Class for BatterRepository class
 *
 * @author pushpendra
 * @version 1.0
 */
package com.pushpendra.powerplantsystem.repository;

import com.pushpendra.powerplantsystem.entity.Battery;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BatteryRepositoryTest {
    @Autowired
    BatteryRepository batteryRepository;

    static List<Battery> batteryList = new ArrayList<>();
    static List<Battery> batteryListWithoutIDAndSort = new ArrayList<>();

    @BeforeAll
    public static void createListofBatteries() {

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


        Battery battery1WithoutIdAndSort = new Battery(null, "Kent Town", 400l, 789l);
        Battery battery2WithoutIdAndSort = new Battery(null, "Northgate Mc", 600l, 456l);
        Battery battery3WithoutIdAndSort = new Battery(null, "Cannington", 1500l, 2345l);
        Battery battery4WithoutIdAndSort = new Battery(null, "Mildland", 1200l, 3425l);
        Battery battery5WithoutIdAndSort = new Battery(null, "Kalamunda", 700l, 3456l);
        Battery battery6WithoutIdAndSort = new Battery(null, "Armadale", 1700l, 3456l);

        batteryListWithoutIDAndSort.add(battery1WithoutIdAndSort);
        batteryListWithoutIDAndSort.add(battery2WithoutIdAndSort);
        batteryListWithoutIDAndSort.add(battery3WithoutIdAndSort);
        batteryListWithoutIDAndSort.add(battery4WithoutIdAndSort);
        batteryListWithoutIDAndSort.add(battery5WithoutIdAndSort);
        batteryListWithoutIDAndSort.add(battery6WithoutIdAndSort);

    }

    @Test
    @DisplayName("Test if Batteries sources Search between ranges work when if fall in the valid ranges")
    @Order(1)
    public void when_findByPostcodeBetweenValidRanges_thenCorrect() {
        String sortedListId600To1500Expected = "3542";
        List<Battery> batteryListSaved = batteryRepository.saveAll(batteryListWithoutIDAndSort);
        Sort sort = Sort.by("name").ascending();
        List<Battery> batteryListSearched = batteryRepository.findByPostcodeBetween(600l, 1500l, sort);

        List<Battery> batteryListAll = batteryRepository.findAll();

        StringBuffer sortedListId600To1500Actual = new StringBuffer();
        batteryListSearched.forEach(battery -> {
            sortedListId600To1500Actual.append(battery.getId().toString());
        });

        assertEquals(sortedListId600To1500Expected, sortedListId600To1500Actual.toString());
    }

    @Test
    @DisplayName("Test if Batteries sources can be saved in db using save method or not")
    @Order(2)
    public void when_saveAll_thenCorrect() {
        Battery batterySaved = batteryRepository.save(batteryListWithoutIDAndSort.get(0));
        assertNotNull(batterySaved.getId());

    }

    @Test
    @DisplayName("Test if Batteries sources Search between ranges work when if it does not fall in the valid ranges")
    @Order(3)
    public void when_findByPostcodeBetweenNotInValidRanges_thenCorrect() {
        List<Battery> batteryListSaved = batteryRepository.saveAll(batteryListWithoutIDAndSort);
        Sort sort = Sort.by("name").ascending();
        List<Battery> batteryListSearched = batteryRepository.findByPostcodeBetween(1800l, 2500l, sort);

        assertEquals(0, batteryListSearched.size());
    }

    @Test
    @DisplayName("Test if Batteries can fetched using findByName method or not when matching record is in db")
    @Order(4)
    public void when_findByNameFound_thenCorrect() {
        Battery batterySaved = batteryRepository.save(batteryListWithoutIDAndSort.get(0));

        Optional<Battery> batteryFetchedOptional = batteryRepository.findByName(batteryListWithoutIDAndSort.get(0).getName());

        assertNotNull(batteryFetchedOptional.get().getId());

    }

    @Test
    @DisplayName("Test if Batteries can fetched using findByName method or not when matching record is not in db")
    @Order(4)
    public void when_findByNameNotFound_thenCorrect() {
        Battery batterySaved = batteryRepository.save(batteryListWithoutIDAndSort.get(0));

        Optional<Battery> batteryFetchedOptional = batteryRepository.findByName("NotinDB");
        Battery batteryFetched = null;

        if (batteryFetchedOptional.isPresent()){
            batteryFetched = batteryFetchedOptional.get();
        }
        assertNull(batteryFetched);

    }
}