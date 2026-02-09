package com.reicar.services;

import com.reicar.dtos.ServiceOrderDTO;
import com.reicar.entities.*;
import com.reicar.entities.enums.ServiceStatus;
import com.reicar.repositories.CustomerRepository;
import com.reicar.repositories.ServiceOrderRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceOrderService {

    private final ServiceOrderRepository repository;
    private final CustomerRepository customerRepository;

    public List<ServiceOrder> findAll() {
        return repository.findAllWithCustomer();
    }

    public ServiceOrder findById(Long id){
        return repository.findByIdWithDetails(id).orElseThrow(() -> new EntityNotFoundException("Ordem de serviço não encotrada: id = "+id));
    }

    @Transactional
    public ServiceOrder saveFromDto(ServiceOrderDTO dto) {
        ServiceOrder order = "MECHANIC".equalsIgnoreCase(dto.type())
                ? new MechanicServiceOrder()
                : new TireShopServiceOrder();

        Customer customer = new Customer();
        customer.setName(dto.customerName());
        customer.setPhone(dto.customerPhone());
        customer.setCity(dto.customerCity());
        customer.setState(dto.customerState());
        customerRepository.save(customer);

        order.setCustomer(customer);
        order.setOrderNumber(generateNextOrderNumber());
        order.setEntryDate(LocalDate.now());
        order.setStatus(ServiceStatus.OPEN);

        order.setServiceValue(dto.serviceValue() != null ? dto.serviceValue() : BigDecimal.ZERO);

        if (order instanceof MechanicServiceOrder mso) {
            mso.setTechnicalDiagnosis(dto.technicalDiagnosis());
            mso.setVehicleKm(dto.vehicleKm());
        } else if (order instanceof TireShopServiceOrder tso) {
            tso.setTirePosition(dto.tirePosition());
        }

        if (dto.items() != null && !dto.items().isEmpty()) {
            List<ServiceItem> entityItems = dto.items().stream()
                    .map(itemDto -> {
                        ServiceItem item = new ServiceItem();
                        item.setQuantity(itemDto.quantity());
                        item.setDescription(itemDto.description());
                        item.setUnitPrice(itemDto.unitPrice());
                        item.setServiceOrder(order);
                        return item;
                    }).toList();
            order.getItems().addAll(entityItems);
        }

        double markup = (order instanceof MechanicServiceOrder) ? 1.30 : 1.0;
        order.calculateTotalValue(markup);

        return repository.save(order);
    }

    private String generateNextOrderNumber() {
        long count = repository.count() + 1;
        return String.format("REICAR-%d-%04d", LocalDate.now().getYear(), count);
    }
}