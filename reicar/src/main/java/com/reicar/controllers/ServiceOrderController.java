package com.reicar.controllers;

import com.reicar.dtos.ServiceOrderDTO;
import com.reicar.entities.ServiceOrder;
import com.reicar.services.PdfGeneratorService; // Importação necessária
import com.reicar.services.ServiceOrderService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/so")
@RequiredArgsConstructor
public class ServiceOrderController {

    private final ServiceOrderService service;
    private final PdfGeneratorService pdfGeneratorService;

    @GetMapping("/register")
    public String showForm(@RequestParam(name = "type", defaultValue = "MECHANIC") String type, Model model) {
        model.addAttribute("type", type);
        model.addAttribute("order", new ServiceOrderDTO());
        return "screens/service-order-form";
    }

    @PostMapping("/register")
    public String saveOrder(@ModelAttribute("order") ServiceOrderDTO dto, RedirectAttributes redirectAttributes) {
        service.saveFromDto(dto);
        redirectAttributes.addFlashAttribute("message", "Ordem de Serviço salva com sucesso!");
        return "redirect:/dashboard";
    }

    @GetMapping("/export-pdf/{id}")
    public void exportToPDF(@PathVariable Long id, HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=OS_Reicar_" + id + ".pdf";
        response.setHeader(headerKey, headerValue);

        ServiceOrder order = service.findById(id);
        pdfGeneratorService.export(response, order);
    }
}