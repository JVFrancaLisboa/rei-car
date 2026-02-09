package com.reicar.config;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.math.BigDecimal;

@ControllerAdvice
public class WebConfig {
    // Conversor de moeda
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(BigDecimal.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (text == null || text.trim().isEmpty()) {
                    setValue(null);
                    return;
                }

                try {
                    String formatado = text.trim();

                    if (formatado.contains(",")) {
                        formatado = formatado.replace(".", "").replace(",", ".");
                    }

                    setValue(new BigDecimal(formatado));
                } catch (Exception e) {
                    setValue(BigDecimal.ZERO);
                }
            }
        });
    }

}
