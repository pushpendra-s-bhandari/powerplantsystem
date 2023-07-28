package com.pushpendra.powerplantsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CapacityStat {
    private Long total;
    private Double average;
}
