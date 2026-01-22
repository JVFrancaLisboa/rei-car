package com.reicar.repositories;

import com.reicar.entities.RepairOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepairOrderRepository extends JpaRepository<RepairOrder, Long> {
}
