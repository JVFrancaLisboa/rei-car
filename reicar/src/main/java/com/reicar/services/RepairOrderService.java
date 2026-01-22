package com.reicar.services;

import com.reicar.repositories.RepairOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RepairOrderService {

    @Autowired
    private RepairOrderRepository repairOrderRepository;
}
