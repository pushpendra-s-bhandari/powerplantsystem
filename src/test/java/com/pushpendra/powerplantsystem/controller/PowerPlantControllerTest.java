/**
 * Test Class for PowerPlantController class
 *
 * @author pushpendra
 * @version 1.0
 */

package com.pushpendra.powerplantsystem.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pushpendra.powerplantsystem.dto.BatteryStat;
import com.pushpendra.powerplantsystem.dto.CapacityStat;
import com.pushpendra.powerplantsystem.entity.Battery;
import com.pushpendra.powerplantsystem.service.BatteryServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@WebMvcTest(PowerPlantController.class)
public class PowerPlantControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    BatteryServiceImpl batteryService;

    @Autowired
    private ObjectMapper objectMapper;

    static List<Battery> batteryList = new ArrayList<>();
    static List<Battery> batteryListWithoutID = new ArrayList<>();

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
    public void when_saveBatteries_thenCorrect() throws Exception {

        Mockito.when(batteryService.saveBatteries(batteryListWithoutID)).thenReturn(batteryList);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/powerplant/battery/add")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(batteryListWithoutID));

        MvcResult result = mockMvc.perform(mockRequest).andReturn();

        MockHttpServletResponse response = result.getResponse();
        List<Battery> batteriesActual = objectMapper.readValue(response.getContentAsString(), new TypeReference<List<Battery>>() {
        });

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(5, batteriesActual.size());

        AtomicReference<Integer> idExpected = new AtomicReference<>(1);
        batteriesActual.forEach(battery -> {
            assertEquals(idExpected.toString(), battery.getId().toString());
            idExpected.getAndSet(idExpected.get() + 1);
        });
    }

    @Test
    @DisplayName("Test if Batteries sources Search between ranges work when if fall in the valid ranges")
    public void when_getBatteriesBetweenPostcodeRangesValidRanges_thenCorrect() throws Exception {
        BatteryStat batteryStatExpected = new BatteryStat();
        CapacityStat capacityStat = new CapacityStat();

        Double averageCapacityExpected = batteryList.stream()
                .collect(Collectors.averagingLong(Battery::getCapacity));

        Long sumCapacityExpected = batteryList.stream().mapToLong(Battery::getCapacity).sum();
        capacityStat.setTotal(sumCapacityExpected);
        capacityStat.setAverage(averageCapacityExpected);

        batteryStatExpected.setBatteryList(batteryList);
        batteryStatExpected.setCapacityStat(capacityStat);

        StringBuffer sortedListExpected = new StringBuffer();
        batteryList.forEach(battery -> {
            sortedListExpected.append(battery.getId().toString());
        });

        Mockito.when(batteryService.getBatteriesBetweenPostcodeRanges(100l, 1500l)).thenReturn(batteryStatExpected);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/powerplant/battery/range/postcode")
                .param("postCodeStart", "100")
                .param("postCodeEnd", "1500")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(mockRequest).andReturn();

        MockHttpServletResponse response = result.getResponse();
        BatteryStat batteryStatActual = objectMapper.readValue(response.getContentAsString(), new TypeReference<BatteryStat>() {
        });

        List<Battery> batteriesActual = batteryStatActual.getBatteryList();

        StringBuffer sortedListActual = new StringBuffer();
        batteriesActual.forEach(battery -> {
            sortedListActual.append(battery.getId().toString());
        });

        Double averageCapacityActual = batteryStatActual.getCapacityStat().getAverage();
        Long sumCapacityActual = batteryStatActual.getCapacityStat().getTotal();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(sortedListExpected.toString(), sortedListActual.toString());
        assertEquals(averageCapacityExpected, averageCapacityActual);
        assertEquals(sumCapacityExpected, sumCapacityActual);

    }

    @Test
    @DisplayName("Test if Batteries sources Search between ranges work when if does not fall in the valid ranges")
    public void when_getBatteriesBetweenPostcodeRangesNotInValidRanges_thenCorrect() throws Exception {
        BatteryStat batteryStatExpected = new BatteryStat();

        Mockito.when(batteryService.getBatteriesBetweenPostcodeRanges(100l, 1500l)).thenReturn(batteryStatExpected);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/powerplant/battery/range/postcode")
                .param("postCodeStart", "100")
                .param("postCodeEnd", "1500")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(mockRequest).andReturn();

        MockHttpServletResponse response = result.getResponse();
        BatteryStat batteryStatActual = objectMapper.readValue(response.getContentAsString(), new TypeReference<BatteryStat>() {
        });

        List<Battery> batteryListActual = batteryStatActual.getBatteryList();

        CapacityStat capacityStatActual = batteryStatActual.getCapacityStat();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNull(batteryListActual);
        assertNull(capacityStatActual);
    }
}