package com.reicar.services;

import com.reicar.dtos.ServiceItemDTO;
import com.reicar.dtos.ServiceOrderDTO;
import com.reicar.entities.Customer;
import com.reicar.entities.MechanicServiceOrder;
import com.reicar.entities.ServiceItem;
import com.reicar.entities.ServiceOrder;
import com.reicar.entities.enums.ServiceStatus;
import com.reicar.repositories.CustomerRepository;
import com.reicar.repositories.ServiceOrderRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceOrderServiceTest {

    @Mock
    private ServiceOrderRepository serviceOrderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private ServiceOrderService service;

    @Test
    @DisplayName("It should return a detailed service order when the id exists")
    void mustReturnServiceOrderWhenIdExists(){
        // AAA
        Long id = 1L;
        ServiceOrder order = new MechanicServiceOrder();
        order.setId(id);
        when(serviceOrderRepository.findByIdWithDetails(id)).thenReturn(Optional.of(order));

        ServiceOrder result = service.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(serviceOrderRepository).findByIdWithDetails(id);
    }

    @Test
    @DisplayName("it should throw an EntityNotFoundException when the id does not exist")
    void mustThrowExceptionWhenIsDoesNotExist(){
        Long id = 90L;
        when(serviceOrderRepository.findByIdWithDetails(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.findById(id));
    }

    /**
     * O sistema não insere o markup no banco mantendo a adição de markup na lógica de négocios.
     * * @param markup Fator de acréscimo (ex: new BigDecimal("1.30") para 30%).
     */
    @Test
    @DisplayName("Must create the MechanicSeviceOder with a 30% markup for item")
    void mustCreateMechanicServiceOrderWithMarkup(){
        // A-A-A
        BigDecimal labor = new BigDecimal("90");
        BigDecimal itemPrice = new BigDecimal("100");
        ServiceItemDTO serviceItem = new ServiceItemDTO(1,"auto part", itemPrice);

        ServiceOrderDTO dto = new ServiceOrderDTO(
                "Josué Vítor", "+55 61 994043465",
                "Ceilândia DF", String.valueOf(ServiceStatus.OPEN),
                "MECHANIC", "Pastilha de freio",
                5000, null, labor, List.of(serviceItem)
        );
        when(customerRepository.save(any(Customer.class))).thenAnswer(i -> i.getArgument(0));
        when(serviceOrderRepository.save(any(ServiceOrder.class))).thenAnswer(i -> i.getArgument(0));

        ServiceOrder result = service.saveFromDto(dto);

        assertEquals(itemPrice, dto.items().get(0).unitPrice());
        assertTrue(result instanceof MechanicServiceOrder);
        assertEquals(ServiceStatus.OPEN, result.getStatus());
        assertEquals(labor, result.getServiceValue());
        assertEquals(new BigDecimal("220.00"), result.getTotalValue()); //90 + (100 * 1.30) = 220
        verify(customerRepository).save(any(Customer.class));
    }
}