package com.reicar.dtos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public record ServiceOrderDTO(
        String customerName,
        String customerPhone,
        String customerCity,
        String customerState,
        String type,
        String technicalDiagnosis,
        Integer vehicleKm,
        String tirePosition,
        BigDecimal serviceValue,
        List<ServiceItemDTO> items
) {
    public ServiceOrderDTO() {
        this(null, null, "Sol Nascente", "DF", null, null, null, null, BigDecimal.ZERO, new ArrayList<>());
    }
}