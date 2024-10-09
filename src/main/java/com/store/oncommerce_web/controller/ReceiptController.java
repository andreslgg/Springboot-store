package com.store.oncommerce_web.controller;
import com.store.oncommerce_web.model.Receipt;
import com.store.oncommerce_web.repository.ReceiptRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Controller
public class ReceiptController {

    @Autowired
    private ReceiptRepository receiptRepository;

    @GetMapping("/receipt/{id}")
    public String getReceipt(@PathVariable UUID id, Model model) {
        // Buscar el recibo en la base de datos
        Receipt receipt = receiptRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Receipt not found"));
        // Agregar datos al modelo
        model.addAttribute("invoiceNumber", receipt.getInvoiceNumber());
        model.addAttribute("invoiceDate", receipt.getInvoiceDate());
        model.addAttribute("customerName", receipt.getCustomerName());
        model.addAttribute("customerAddress", receipt.getCustomerAddress());
        model.addAttribute("orderItems", receipt.getOrderItems());
        model.addAttribute("subtotal", receipt.getSubtotal());
        model.addAttribute("tax", receipt.getTax());
        model.addAttribute("shippingCost", receipt.getShippingCost());
        model.addAttribute("total", receipt.getTotal());
        model.addAttribute("QrCodeUrl", receipt.getQrCodeUrl());
        return "receipt"; // Renderiza el template Thymeleaf
    }
}
